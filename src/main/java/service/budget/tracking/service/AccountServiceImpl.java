package service.budget.tracking.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.budget.tracking.entity.Account;
import service.budget.tracking.exception.ServiceCustomException;
import service.budget.tracking.model.AccountRequest;
import service.budget.tracking.model.AccountResponse;
import service.budget.tracking.repository.AccountRepository;

import java.util.List;

@Service
@Log4j2
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    @Autowired
    public AccountServiceImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public long createAccount(AccountRequest request) {
        log.info("Creating new account...");

        Account account = Account.builder()
                .accountName(request.getAccountName())
                .accountDescription(request.getAccountDescription())
                .accountType(request.getAccountType())
                .totalAmount(request.getTotalAmount())
                .build();

        repository.save(account);
        log.info("Account created!");
        return account.getAccountId();
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        log.info("Getting all accounts...");

        List<Account> accounts = repository.findAll();
        return accounts
                .stream()
                .map(account -> {
                    AccountResponse response = new AccountResponse();
                    BeanUtils.copyProperties(account, response);
                    return response;
                }).toList();
    }

    @Override
    public AccountResponse getAccountById(long id) {
        log.info("Getting account with given id: {}", id);

        Account account = repository.findById(id)
                .orElseThrow(() -> new ServiceCustomException(
                        "Account with given id not found",
                        "ACCOUNT_NOT_FOUND", 404));

        AccountResponse response = new AccountResponse();
        BeanUtils.copyProperties(account, response);
        return response;
    }

    @Override
    public void deleteAccountById(long id) {
        log.info("Getting account with given id: {}", id);

        Account account = repository.findById(id)
                .orElseThrow(() -> new ServiceCustomException(
                        "Account with given id not found",
                        "ACCOUNT_NOT_FOUND", 404));
        repository.delete(account);
        log.info("Account deleted!");
    }
}
