package com.cashew.budgetservice.controllers;

import com.cashew.budgetservice.DTO.ExpensesDTO.Request;
import com.cashew.budgetservice.services.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpensesController {
    private ExpensesService expensesService;

    @Autowired
    public ExpensesController(ExpensesService expensesService) {
        this.expensesService = expensesService;
    }

    @GetMapping(path = "/day")
    public ResponseEntity<?> getExpensesForToday(@RequestBody Request.perLastDay requestDTO){
        return expensesService.getExpensesPerLastDay(requestDTO.getUsername());
    }

    @GetMapping(path = "/week")
    public ResponseEntity<?> getExpensesPerWeek(@RequestBody Request.perLastWeek requestDTO){
        return expensesService.getExpensesPerLastWeek(requestDTO.getUsername());
    }

    @GetMapping(path = "/month")
    public ResponseEntity<?> getExpensesPerMonth(@RequestBody Request.perLastMonth requestDTO){
        return expensesService.getExpensesPerLastMonth(requestDTO.getUsername());
    }

    @GetMapping(path = "/year")
    public ResponseEntity<?> getExpensesPerYear(@RequestBody Request.perLastYear requestDTO){
        return expensesService.getExpensesPerLastYear(requestDTO.getUsername());
    }

    @GetMapping(path = "/period")
    public ResponseEntity<?> getExpensesPerCustomPeriod(@RequestBody
                                                            @DateTimeFormat(pattern = "dd.MM.yyyy")
                                                                    Request.perCustomPeriod requestDTO){
        return expensesService.getExpensesPerCustomPeriod(requestDTO.getUsername(), requestDTO.getFrom(), requestDTO.getTo());
    }
}
