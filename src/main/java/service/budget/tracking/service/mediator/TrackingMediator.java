package service.budget.tracking.service.mediator;

import service.budget.tracking.entity.Account;

public interface TrackingMediator {
    void debitAmountToAccount(long id, double amount);
    void creditAmountToAccount(long id, double amount);
    Account getAccount(long id);
}
