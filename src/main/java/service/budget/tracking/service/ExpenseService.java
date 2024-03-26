package service.budget.tracking.service;

import service.budget.tracking.model.ExpenseRequest;
import service.budget.tracking.model.ExpenseResponse;

import java.util.List;

public interface ExpenseService {
    long createExpense(ExpenseRequest request);

    List<ExpenseResponse> getAllExpenses();

    ExpenseResponse getExpenseById(long id);

    void deleteExpense(long id);

    long updateExpense(long id, ExpenseRequest request);
}
