package service.budget.tracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<Long> createIncome(@RequestBody IncomeRequest request) {
        long incomeId = incomeService.createIncome(request);
        return new ResponseEntity<>(incomeId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponse>> getAllIncome() {
        List<IncomeResponse> incomeResponses = incomeService.getAllIncome();
        return new ResponseEntity<>(incomeResponses, HttpStatus.OK);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<List<IncomeResponse>> getIncomeByAccount(@PathVariable("id") long id) {
        List<IncomeResponse> incomeResponses = incomeService.getIncomeByAccount(id);
        return new ResponseEntity<>(incomeResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponse> getIncomeById(@PathVariable("id") long id) {
         IncomeResponse incomeResponse = incomeService.getIncomeById(id);
         return new ResponseEntity<>(incomeResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncomeById(@PathVariable("id") long id) {
        incomeService.deleteIncomeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateIncomeById(@PathVariable("id") long id,
                                                 @RequestBody IncomeRequest request) {
        incomeService.updateIncomeById(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
