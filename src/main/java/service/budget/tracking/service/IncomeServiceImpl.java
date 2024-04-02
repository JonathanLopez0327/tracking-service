package service.budget.tracking.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import service.budget.tracking.entity.Income;
import service.budget.tracking.exception.ServiceCustomException;
import service.budget.tracking.model.IncomeRequest;
import service.budget.tracking.model.IncomeResponse;
import service.budget.tracking.repository.IncomeRepository;
import service.budget.tracking.service.mediator.TrackingMediator;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
@Log4j2
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository repository;
    private final TrackingMediator trackingMediator;

    @Autowired
    public IncomeServiceImpl(IncomeRepository repository, TrackingMediator trackingMediator) {
        this.repository = repository;
        this.trackingMediator = trackingMediator;
    }

    private String getCurrentPeriod() {
        String period = "";
        try {
            Instant currentInstant = Instant.now();
            YearMonth yearMonth = YearMonth.from(currentInstant.atZone(ZoneId.systemDefault()));
            // Formatear el YearMonth como "yyyyMM"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
            period = yearMonth.format(formatter);
        } catch (Exception var3) {
            log.error("Error getting current period", var3);
        }

        return period;
    }

    @Override
    public long createIncome(IncomeRequest request) {
        log.info("Creating new income...");


        Income income = Income.builder()
                .incomeAmount(request.getIncomeAmount())
                .accountId(request.getAccountId())
                .incomeCategory(request.getIncomeCategory())
                .incomeDescription(request.getIncomeDescription())
                .incomeDate(Instant.now())
                .incomePeriod(getCurrentPeriod())
                .build();

        // Credit amount to account
        trackingMediator.creditAmountToAccount(request.getAccountId(), request.getIncomeAmount());
        log.info("Income creadited to account: {}", request.getAccountId());

        repository.save(income);
        log.info("Income created!");
        return income.getIncomeId();
    }

    @Override
    public List<IncomeResponse> getAllIncome() {
        log.info("Getting all incomes...");

        List<Income> incomeList = repository.findAll();

        return incomeList
                .stream()
                .map(income -> {
                    IncomeResponse incomeResponse = new IncomeResponse();
                    BeanUtils.copyProperties(income, incomeResponse);
                    return incomeResponse;
                }).toList();
    }

    @Override
    public IncomeResponse getIncomeById(long id) {
        log.info("Getting income with given id: {}", id);

        Income income = repository.findById(id)
                .orElseThrow(() -> new ServiceCustomException(
                        "Income with given id not found",
                        "INCOME_NOT_FOUND",
                        404
                ));

        IncomeResponse incomeResponse = new IncomeResponse();
        BeanUtils.copyProperties(income, incomeResponse);
        return incomeResponse;
    }

    @Override
    public void deleteIncomeById(long id) {
        log.info("Getting income with given id: {}", id);

        Income income = repository.findById(id)
                .orElseThrow(() -> new ServiceCustomException(
                        "Income with given id not found",
                        "INCOME_NOT_FOUND",
                        404
                ));

        log.info("Removing income...");

        // Remove from account
        trackingMediator.debitAmountToAccount(income.getAccountId(), income.getIncomeAmount());
        log.info("Income removed from account: {}", income.getAccountId());


        repository.delete(income);
        log.info("Income removed!");
    }

    @Override
    public void updateIncomeById(long id, IncomeRequest request) {
        log.info("Getting income with given id: {}", id);

        Income income = repository.findById(id)
                .orElseThrow(() -> new ServiceCustomException(
                        "Income with given id not found",
                        "INCOME_NOT_FOUND",
                        404
                ));

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<IncomeRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ServiceCustomException(
                    "Error modifying income",
                    "BAD_REQUEST",
                    500
            );
        }

        if (!request.getIncomeCategory().toString().isEmpty()) {
            income.setIncomeCategory(request.getIncomeCategory());
        }
        if (!request.getIncomeDescription().isEmpty()) {
            income.setIncomeDescription(request.getIncomeDescription());
        }


        if (request.getAccountId() != 0 && request.getIncomeAmount() != 0) {
            trackingMediator.debitAmountToAccount(income.getAccountId(), income.getIncomeAmount());

            income.setAccountId(request.getAccountId());
            income.setIncomeAmount(request.getIncomeAmount());


            try {
                trackingMediator.creditAmountToAccount(income.getAccountId(), income.getIncomeAmount());
                log.info("Account updated!");
                repository.save(income);
                log.info("Income updated!");
            } catch (ServiceCustomException e) {
                log.error("Error updating income: ", e);

                throw new ServiceCustomException(
                        "Error modifying income",
                        "BAD_REQUEST",
                        500
                );
            }
        }
    }

    @Override
    public List<IncomeResponse> getIncomeByAccount(long id) {
        log.info("Getting incomes with given account id: {}", id);
        List<Income> incomeList = repository.findByAccountId(id);

        return incomeList
                .stream()
                .map(entity -> {
                    IncomeResponse response = new IncomeResponse();
                    BeanUtils.copyProperties(entity, response);
                    return response;
                }).toList();
    }
}
