package service.budget.tracking.service.mediator;

import service.budget.tracking.model.ExpenseRequest;
import service.budget.tracking.model.ExpenseResponse;
import service.budget.tracking.model.IncomeRequest;
import service.budget.tracking.model.IncomeResponse;

import java.util.List;

public interface AccountMediator {
    void createIncomeDifference(IncomeRequest request);
    void createExpenseDifference(ExpenseRequest request);
    List<IncomeResponse> getIncomesFromAccount(long id);
    List<ExpenseResponse> getExpensesFromAccount(long id);
}
