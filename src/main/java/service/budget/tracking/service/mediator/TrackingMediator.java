package service.budget.tracking.service.mediator;

public interface TrackingMediator {
    void debitAmountToAccount(long id, double amount);
    void creditAmountToAccount(long id, double amount);
}
