package service.budget.tracking.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.budget.tracking.entity.Account;
import service.budget.tracking.exception.ServiceCustomException;
import service.budget.tracking.model.*;
import service.budget.tracking.repository.AccountRepository;
import service.budget.tracking.service.mediator.AccountMediator;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final AccountMediator accountMediator;

    @Autowired
    public AccountServiceImpl(AccountRepository repository, AccountMediator accountMediator) {
        this.repository = repository;
        this.accountMediator = accountMediator;
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

    @Override
    public void creditAmount(long id, double amount) {
        log.info("Getting account with given id: {}", id);
        Account account = repository.findById(id)
                .orElseThrow(() -> new ServiceCustomException(
                        "Account with given id not found",
                        "ACCOUNT_NOT_FOUND", 404));

        if (amount != 0) {
            account.setTotalAmount(account.getTotalAmount() + amount);
            repository.save(account);
            log.info("Total amount credited!");
        } else {
            throw new ServiceCustomException(
                    "Amount must not be null",
                    "BAD_REQUEST",
                    500
            );
        }
    }

    @Override
    public void debitAmount(long id, double amount) {
        log.info("Getting account with given id: {}", id);
        Account account = repository.findById(id)
                .orElseThrow(() -> new ServiceCustomException(
                        "Account with given id not found",
                        "ACCOUNT_NOT_FOUND", 404));

        if (amount != 0) {
            if (account.getTotalAmount() < amount) {
                throw new ServiceCustomException(
                        "Account does not have sufficient balance",
                        "INSUFFICIENT_BALANCE",
                        500
                );
            }

            account.setTotalAmount(account.getTotalAmount() - amount);
            repository.save(account);
            log.info("Total amount debited!");
        } else {
            throw new ServiceCustomException(
                    "Amount must not be null",
                    "BAD_REQUEST",
                    500
            );
        }
    }

    @Override
    public void updateAccount(long id, AccountRequest request) {
        log.info("Getting account with given id: {}", id);
        Account account = repository.findById(id)
                .orElseThrow(() -> new ServiceCustomException(
                        "Account with given id not found",
                        "ACCOUNT_NOT_FOUND", 404));

        if (request.getTotalAmount() != 0) {
            if (account.getTotalAmount() > request.getTotalAmount()) {
                log.info("Difference for expense, creating new expense...");
                double expenseDifference = account.getTotalAmount() - request.getTotalAmount();

                ExpenseRequest expenseRequest = ExpenseRequest.builder()
                        .expenseAmount(expenseDifference)
                        .accountId(id)
                        .expenseCategory(ExpenseCategory.DIFFERENCE)
                        .expenseDescription("DIFFERENCE")
                        .build();
                accountMediator.createExpenseDifference(expenseRequest);
                log.info("Expense created!");
            }

            if (account.getTotalAmount() < request.getTotalAmount()) {
                log.info("Difference for income, creating new income...");
                double incomeDifference = request.getTotalAmount() - account.getTotalAmount();

                IncomeRequest incomeRequest = IncomeRequest.builder()
                        .incomeAmount(incomeDifference)
                        .accountId(id)
                        .incomeCategory(IncomeCategory.DIFFERENCE)
                        .incomeDescription("DIFFERENCE")
                        .build();
                accountMediator.createIncomeDifference(incomeRequest);
                log.info("Income created!");
            }

            account.setTotalAmount(request.getTotalAmount());
        }

        if (!request.getAccountDescription().isEmpty()) {
            account.setAccountDescription(request.getAccountDescription());
        }

        if (!request.getAccountName().isEmpty()) {
            account.setAccountName(request.getAccountDescription());
        }

        if (!request.getAccountType().toString().isEmpty()) {
            account.setAccountType(request.getAccountType());
        }

        try {
            repository.save(account);
            log.info("Account updated!");
        } catch (ServiceCustomException e) {
            log.error("Error updating account: {}", e);

            throw new ServiceCustomException(
                    "Error modifying account",
                    "BAD_REQUEST",
                    500
            );
        }
    }

    @Override
    public AccountDetailsReponse getAccountByIdWithDetails(long id) {
        log.info("Getting account with given id: {}", id);
        Account account = repository.findById(id)
                .orElseThrow(() -> new ServiceCustomException(
                        "Account with given id not found",
                        "ACCOUNT_NOT_FOUND", 404));

        List<IncomeResponse> incomeResponses = accountMediator.getIncomesFromAccount(id);
        List<ExpenseResponse> expenseResponses = accountMediator.getExpensesFromAccount(id);

        return AccountDetailsReponse.builder()
                .accountId(account.getAccountId())
                .accountName(account.getAccountName())
                .accountDescription(account.getAccountDescription())
                .accountType(account.getAccountType())
                .totalAmount(account.getTotalAmount())
                .incomeDetails(incomeResponses)
                .expenseDetails(expenseResponses)
                .build();
    }

    @Override
    public List<AccountDetailsReponse> getAccountWithDetails() {
        log.info("Getting all accounts with details");

        List<Account> accounts = repository.findAll();

        List<AccountDetailsReponse> accountResponses = new ArrayList<>();

        for (Account account : accounts) {
            long id = account.getAccountId(); // Asumiendo que el id se llama "id"

            List<IncomeResponse> incomeResponses = accountMediator.getIncomesFromAccount(id);
            List<ExpenseResponse> expenseResponses = accountMediator.getExpensesFromAccount(id);

            AccountDetailsReponse response = AccountDetailsReponse.builder()
                    .accountId(account.getAccountId())
                    .accountName(account.getAccountName())
                    .accountDescription(account.getAccountDescription())
                    .accountType(account.getAccountType())
                    .totalAmount(account.getTotalAmount())
                    .incomeDetails(incomeResponses)
                    .expenseDetails(expenseResponses)
                    .build();

            accountResponses.add(response);
        }

        return accountResponses;
    }
}
