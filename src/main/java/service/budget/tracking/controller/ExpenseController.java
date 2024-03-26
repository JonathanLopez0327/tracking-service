package service.budget.tracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<Long> createExpense(@RequestBody ExpenseRequest request) {
        long id = expenseService.createExpense(request);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        List<ExpenseResponse> expenseResponses =
                expenseService.getAllExpenses();
        return new ResponseEntity<>(expenseResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable("id") long id) {
        ExpenseResponse expenseResponse =
                expenseService.getExpenseById(id);
        return new ResponseEntity<>(expenseResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("id") long id) {
        expenseService.deleteExpense(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateExpense(@PathVariable("id") long id,
                                              @RequestBody ExpenseRequest request) {
        long expenseId = expenseService.updateExpense(id, request);
        return new ResponseEntity<>(expenseId, HttpStatus.OK);
    }
}
