package service.budget.tracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public ResponseEntity<Long> createAccount(@RequestBody AccountRequest request) {
        long accountId = accountService.createAccount(request);
        return new ResponseEntity<>(accountId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> accountResponseList = accountService.getAllAccounts();
        return new ResponseEntity<>(accountResponseList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable("id") long id) {
        AccountResponse accountResponse = accountService.getAccountById(id);
        return new ResponseEntity<>(accountResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountById(@PathVariable("id") long id) {
        accountService.deleteAccountById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
