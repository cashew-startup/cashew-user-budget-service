package com.cashew.budgetservice.controllers;

import com.cashew.budgetservice.DTO.ExpensesDTO;
import com.cashew.budgetservice.services.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpensesController {

    private ExpensesService expensesService;

    @Autowired
    public ExpensesController(ExpensesService expensesService) {
        this.expensesService = expensesService;
    }

    @PostMapping
    public ResponseEntity<ExpensesDTO.Response.Success> addReceipt(@RequestBody ExpensesDTO.Request.AddReceipt request) {
        return expensesService.addReceipt(request.getUsername(), request.getToken());
    }

    @GetMapping
    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpenses(@RequestParam String username){
        return expensesService.getAllExpenses(username);
    }

    @GetMapping(path = "/day")
    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerDay(@RequestParam String username){
        return expensesService.getExpensesPerLastDay(username);
    }

    @GetMapping(path = "/week")
    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerWeek(@RequestParam String username){
        return expensesService.getExpensesPerLastWeek(username);
    }

    @GetMapping(path = "/month")
    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerMonth(@RequestParam String username){
        return expensesService.getExpensesPerLastMonth(username);
    }

    @GetMapping(path = "/year")
    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerYear(@RequestParam String username){
        return expensesService.getExpensesPerLastYear(username);
    }

    @GetMapping(path = "/period")
    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerCustomPeriod(@RequestParam String username,
                                                                                           @RequestParam String from,
                                                                                           @RequestParam String to){
        return expensesService.getExpensesPerCustomPeriod(username, from, to);
    }

    @DeleteMapping
    public ResponseEntity<ExpensesDTO.Response.Success> disableReceiptForUser(@RequestParam String username,
                                                                              @RequestParam Long receiptId){
        return expensesService.disableReceiptForUser(username, receiptId);
    }
}
