package service.budget.tracking.service.mediator;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import service.budget.tracking.model.ExpenseRequest;
import service.budget.tracking.model.ExpenseResponse;
import service.budget.tracking.model.IncomeRequest;
import service.budget.tracking.model.IncomeResponse;
import service.budget.tracking.service.ExpenseService;
import service.budget.tracking.service.IncomeService;

import java.util.List;

@Service
@Log4j2
public class AccountMediatorImpl implements AccountMediator {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @Autowired
    public AccountMediatorImpl(
            @Lazy IncomeService incomeService,
            @Lazy ExpenseService expenseService
    ) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
    }


    @Override
    public void createIncomeDifference(IncomeRequest request) {
        log.info("Registering income through mediator!");
        incomeService.createIncome(request);
    }

    @Override
    public void createExpenseDifference(ExpenseRequest request) {
        log.info("Registering expense through mediator!");
        expenseService.createExpense(request);
    }

    @Override
    public List<IncomeResponse> getIncomesFromAccount(long id) {
        log.info("Getting income through mediator!");
        return incomeService.getIncomeByAccount(id);
    }

    @Override
    public List<ExpenseResponse> getExpensesFromAccount(long id) {
        log.info("Getting expense through mediator!");
        return expenseService.getExpenseByAccount(id);
    }
}
