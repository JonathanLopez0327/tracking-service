package service.budget.tracking.service;

import service.budget.tracking.entity.Account;
import service.budget.tracking.model.AccountDetailsReponse;
import service.budget.tracking.model.AccountRequest;
import service.budget.tracking.model.AccountResponse;

import java.util.List;

public interface AccountService {
    long createAccount(AccountRequest request);

    List<AccountResponse> getAllAccounts();

    AccountResponse getAccountById(long id);

    Account getAccount(long id);

    void deleteAccountById(long id);

    void creditAmount(long id, double amount);

    void debitAmount(long id, double amount);

    void updateAccount(long id, AccountRequest request);

    AccountDetailsReponse getAccountByIdWithDetails(long id);

    List<AccountDetailsReponse> getAccountWithDetails();
}
