package service.budget.tracking.service;

import service.budget.tracking.model.AccountRequest;
import service.budget.tracking.model.AccountResponse;

import java.util.List;

public interface AccountService {
    long createAccount(AccountRequest request);

    List<AccountResponse> getAllAccounts();

    AccountResponse getAccountById(long id);

    void deleteAccountById(long id);
}
