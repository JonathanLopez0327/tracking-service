package service.budget.tracking.service;

import jakarta.validation.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.budget.tracking.entity.Expense;
import service.budget.tracking.exception.ServiceCustomException;
import service.budget.tracking.model.ExpenseRequest;
import service.budget.tracking.model.ExpenseResponse;
import service.budget.tracking.repository.ExpenseRepository;

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

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository repository) {
        this.repository = repository;
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
                .expenseCategory(request.getExpenseCategory())
                .expenseDescription(request.getExpenseDescription())
                .expenseDate(Instant.now())
                .period(getCurrentPeriod())
                .build();

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
        repository.delete(expense);
        log.info("Expense deleted!");
    }

    @Override
    public long updateExpense(long id, ExpenseRequest request) {
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

        if (request.getExpenseAmount() != 0) {
            expense.setExpenseAmount(request.getExpenseAmount());
        }
        if (!request.getExpenseCategory().isEmpty()) {
            expense.setExpenseCategory(request.getExpenseCategory());
        }
        if (!request.getExpenseCategory().isEmpty()) {
            expense.setExpenseDescription(request.getExpenseDescription());
        }

        try {
            repository.save(expense);
            log.info("Expense updated!");
            return expense.getExpenseId();
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
