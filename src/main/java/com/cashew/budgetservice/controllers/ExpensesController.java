package com.cashew.budgetservice.controllers;

import com.cashew.budgetservice.DAO.Interfaces.ExpensesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpensesController {
    @Autowired
    private ExpensesDAO dao;

    @GetMapping(path = "/{username}/day")
    public ResponseEntity<?> getExpensesforToday(@PathVariable(value = "username") String username){
        return new ResponseEntity<>(dao.getExpensesForToday(username),HttpStatus.OK);
    }

    @GetMapping(path = "/{username}/week")
    public ResponseEntity<?> getExpensesPerWeek(@PathVariable(value = "username") String username){
        return new ResponseEntity<>(dao.getExpensesForToday(username),HttpStatus.OK);
    }

    @GetMapping(path = "/{username}/month")
    public ResponseEntity<?> getExpensesPerMonth(@PathVariable(value = "username") String username){
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping(path = "/{username}/year")
    public ResponseEntity<?> getExpensesPerYear(@PathVariable(value = "username") String username){
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping(path = "/{username}/period")
    public ResponseEntity<?> getExpensesPerCustomPeriod(
            @PathVariable(value = "username") String username,
            @RequestParam String from,
            @RequestParam String to){
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
