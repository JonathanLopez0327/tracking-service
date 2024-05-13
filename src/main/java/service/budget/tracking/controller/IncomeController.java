package service.budget.tracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import service.budget.tracking.model.IncomeRequest;
import service.budget.tracking.model.IncomeResponse;
import service.budget.tracking.service.IncomeService;

import java.util.List;

@RestController
@RequestMapping("/income")
public class IncomeController {

    private final IncomeService incomeService;

    @Autowired
    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users')")
    @PostMapping
    public ResponseEntity<Long> createIncome(@RequestBody IncomeRequest request) {
        long incomeId = incomeService.createIncome(request);
        return new ResponseEntity<>(incomeId, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users') or hasAuthority('ROLE_budget-viewers')")
    @GetMapping
    public ResponseEntity<List<IncomeResponse>> getAllIncome() {
        List<IncomeResponse> incomeResponses = incomeService.getAllIncome();
        return new ResponseEntity<>(incomeResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users') or hasAuthority('ROLE_budget-viewers')")
    @GetMapping("/accounts/{id}")
    public ResponseEntity<List<IncomeResponse>> getIncomeByAccount(@PathVariable("id") long id) {
        List<IncomeResponse> incomeResponses = incomeService.getIncomeByAccount(id);
        return new ResponseEntity<>(incomeResponses, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users') or hasAuthority('ROLE_budget-viewers')")
    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponse> getIncomeById(@PathVariable("id") long id) {
         IncomeResponse incomeResponse = incomeService.getIncomeById(id);
         return new ResponseEntity<>(incomeResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncomeById(@PathVariable("id") long id) {
        incomeService.deleteIncomeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_budget-manager') or hasAuthority('ROLE_budget-users')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateIncomeById(@PathVariable("id") long id,
                                                 @RequestBody IncomeRequest request) {
        incomeService.updateIncomeById(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
