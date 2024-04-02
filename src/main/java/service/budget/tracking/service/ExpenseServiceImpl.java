package service.budget.tracking.service;

import jakarta.validation.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.budget.tracking.entity.Expense;
import service.budget.tracking.exception.ServiceCustomException;
import service.budget.tracking.model.ExpenseRequest;
import service.budget.tracking.model.ExpenseResponse;
import service.budget.tracking.repository.ExpenseRepository;
import service.budget.tracking.service.mediator.TrackingMediator;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Log4j2
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository repository;
    private final TrackingMediator trackingMediator;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository repository, TrackingMediator trackingMediator) {
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
    public long createExpense(ExpenseRequest request) {
        log.info("Creating new expense...");

        Expense expense = Expense.builder()
                .expenseAmount(request.getExpenseAmount())
                .accountId(request.getAccountId())
                .expenseCategory(request.getExpenseCategory())
                .expenseDescription(request.getExpenseDescription())
                .expenseDate(Instant.now())
                .expensePeriod(getCurrentPeriod())
                .build();

        // Debit expense
        trackingMediator.debitAmountToAccount(request.getAccountId(), request.getExpenseAmount());
        log.info("Expense credited to account: {}", request.getAccountId());

        repository.save(expense);
        log.info("Expense created!");
        return expense.getExpenseId();
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        log.info("Getting all expenses...");

        List<Expense> expenseList = repository.findAll();

        return expenseList
                .stream()
                .map(expense -> {
                    ExpenseResponse expenseResponse = new ExpenseResponse();
                    copyProperties(expense, expenseResponse);
                    return expenseResponse;
                }).toList();
    }

    @Override
    public ExpenseResponse getExpenseById(long id) {
        log.info("Getting income with give id: {}", id);

        Expense expense = repository.findById(id)
                .orElseThrow(() -> new ServiceCustomException(
                        "Expense with given id not found",
                        "EXPENSE_NOT_FOUND",
                        404
                ));

        ExpenseResponse expenseResponse = new ExpenseResponse();
        copyProperties(expense, expenseResponse);
        return expenseResponse;
    }

    @Override
    public void deleteExpense(long id) {
        log.info("Deleting expense with given id: {}", id);

        Expense expense = repository.findById(id)
                .orElseThrow(() -> new ServiceCustomException(
                        "Expense with given id not found",
                        "EXPENSE_NOT_FOUND",
                        404
                ));

        trackingMediator.creditAmountToAccount(expense.getAccountId(), expense.getExpenseAmount());
        log.info("Expense removed from account: {}", expense.getAccountId());

        repository.delete(expense);
        log.info("Expense deleted!");
    }

    @Override
    public void updateExpense(long id, ExpenseRequest request) {
        log.info("Getting expense with given id: {}", id);

        Expense expense = repository.findById(id)
                .orElseThrow(() -> new ServiceCustomException(
                        "Expense with given id not found",
                        "EXPENSE_NOT_FOUND",
                        404
                ));

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<ExpenseRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new ServiceCustomException(
                    "Error modifying expense",
                    "BAD_REQUEST",
                    500
            );
        }

        trackingMediator.creditAmountToAccount(expense.getAccountId(), expense.getExpenseAmount());

        if (!request.getExpenseCategory().toString().isEmpty()) {
            expense.setExpenseCategory(request.getExpenseCategory());
        }
        if (!request.getExpenseDescription().isEmpty()) {
            expense.setExpenseDescription(request.getExpenseDescription());
        }

        if (request.getExpenseAmount() != 0 && request.getExpenseAmount() != 0) {
            expense.setAccountId(request.getAccountId());
            expense.setExpenseAmount(request.getExpenseAmount());

            try {
                trackingMediator.debitAmountToAccount(expense.getAccountId(), expense.getExpenseAmount());
                log.info("Account updated!");
                repository.save(expense);
                log.info("Expense updated!");
            } catch (ServiceCustomException e) {
                log.error("Error updating expense: ", e);

                throw new ServiceCustomException(
                        "Error modifying expense",
                        "BAD_REQUEST",
                        500
                );
            }
        }
    }

    @Override
    public List<ExpenseResponse> getExpenseByAccount(long id) {
        log.info("Getting expenses with given account id: {}", id);
        List<Expense> expenseList = repository.findByAccountId(id);

        return expenseList
                .stream()
                .map(expense -> {
                    ExpenseResponse response = new ExpenseResponse();
                    BeanUtils.copyProperties(expense, response);
                    return response;
                }).toList();
    }

}
