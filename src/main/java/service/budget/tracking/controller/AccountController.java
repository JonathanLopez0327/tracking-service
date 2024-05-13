package service.budget.tracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import service.budget.tracking.model.AccountDetailsReponse;
import service.budget.tracking.model.AccountRequest;
import service.budget.tracking.model.AccountResponse;
import service.budget.tracking.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users')")
    @PostMapping
    public ResponseEntity<Long> createAccount(@RequestBody AccountRequest request) {
        long accountId = accountService.createAccount(request);
        return new ResponseEntity<>(accountId, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users') or hasAuthority('ROLE_budget-viewers')")
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> accountResponseList = accountService.getAllAccounts();
        return new ResponseEntity<>(accountResponseList, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users') or hasAuthority('ROLE_budget-viewers')")
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable("id") long id) {
        AccountResponse accountResponse = accountService.getAccountById(id);
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users') or hasAuthority('ROLE_budget-viewers')")
    @GetMapping("/details")
    public ResponseEntity<List<AccountDetailsReponse>> getAccountByIdWithDetails() {
        List<AccountDetailsReponse> accountResponse = accountService.getAccountWithDetails();
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users') or hasAuthority('ROLE_budget-viewers')")
    @GetMapping("/details/{id}")
    public ResponseEntity<AccountDetailsReponse> getAccountByIdWithDetails(@PathVariable("id") long id) {
        AccountDetailsReponse accountResponse = accountService.getAccountByIdWithDetails(id);
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountById(@PathVariable("id") long id) {
        accountService.deleteAccountById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users')")
    @PutMapping("/credit/{id}")
    public ResponseEntity<Void> creditAmount(@PathVariable("id") long id, @RequestParam double amount) {
        accountService.creditAmount(id, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users')")
    @PutMapping("/debit/{id}")
    public ResponseEntity<Void> debitAmount(@PathVariable("id") long id, @RequestParam double amount) {
        accountService.debitAmount(id, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAccount(@PathVariable("id") long id, @RequestBody AccountRequest request) {
        accountService.updateAccount(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
