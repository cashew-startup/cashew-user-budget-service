package com.cashew.budgetservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/expenses")
public class ExpensesController {
//    @Autowired
//    private ExpensesDAO dao;

    @GetMapping(path = "/{id}/day")
    public ResponseEntity<?> getExpensesPerDay(@PathVariable(value = "id") Long id){
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping(path = "/{id}/week")
    public ResponseEntity<?> getExpensesPerWeek(@PathVariable(value = "id") Long id){
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping(path = "/{id}/month")
    public ResponseEntity<?> getExpensesPerMonth(@PathVariable(value = "id") Long id){
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping(path = "/{id}/year")
    public ResponseEntity<?> getExpensesPerYear(@PathVariable(value = "id") Long id){
        // list of expenses for year(expenses for each month)
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping(path = "/{id}/period")
    public ResponseEntity<?> getExpensesPerCustomPeriod(
            @PathVariable(value = "id") Long id,
            @RequestParam String from,
            @RequestParam String to){
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
