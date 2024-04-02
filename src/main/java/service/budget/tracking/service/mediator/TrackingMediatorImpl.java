package service.budget.tracking.service.mediator;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import service.budget.tracking.service.AccountService;

@Service
@Log4j2
public class TrackingMediatorImpl implements TrackingMediator {

    private final AccountService accountService;

    @Autowired
    public TrackingMediatorImpl(@Lazy AccountService accountService) {
        this.accountService = accountService;
    }


    @Override
    public void debitAmountToAccount(long id, double amount) {
        log.info("Debiting through mediator...");
        accountService.debitAmount(id, amount);
    }

    @Override
    public void creditAmountToAccount(long id, double amount) {
        log.info("Crediting through mediator...");
        accountService.creditAmount(id, amount);
    }
}
