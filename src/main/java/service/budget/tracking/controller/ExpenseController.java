package service.budget.tracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import service.budget.tracking.model.ExpenseRequest;
import service.budget.tracking.model.ExpenseResponse;
import service.budget.tracking.service.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users')")
    @PostMapping
    public ResponseEntity<Long> createExpense(@RequestBody ExpenseRequest request) {
        long id = expenseService.createExpense(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users') or hasAuthority('ROLE_budget-viewers')")
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        List<ExpenseResponse> expenseResponses =
                expenseService.getAllExpenses();
        return new ResponseEntity<>(expenseResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users') or hasAuthority('ROLE_budget-viewers')")
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable("id") long id) {
        ExpenseResponse expenseResponse =
                expenseService.getExpenseById(id);
        return new ResponseEntity<>(expenseResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users') or hasAuthority('ROLE_budget-viewers')")
    @GetMapping("/accounts/{id}")
    public ResponseEntity<List<ExpenseResponse>> getExpenseByAccount(@PathVariable("id") long id) {
        List<ExpenseResponse> expenseResponses =
                expenseService.getExpenseByAccount(id);
        return new ResponseEntity<>(expenseResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("id") long id) {
        expenseService.deleteExpense(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateExpense(@PathVariable("id") long id,
                                              @RequestBody ExpenseRequest request) {
        expenseService.updateExpense(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
