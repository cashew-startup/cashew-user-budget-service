package com.cashew.budgetservice.services;

import com.cashew.budgetservice.DAO.Interfaces.ExpensesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExpensesService {
    private ExpensesDAO expensesDAO;

    @Autowired
    public ExpensesService(ExpensesDAO expensesDAO) {
        this.expensesDAO = expensesDAO;
    }

    public ResponseEntity<String> getExpensesPerLastDay(String username){
        return new ResponseEntity<>(expensesDAO.getExpensesPerLastDay(username).toJson(), HttpStatus.OK);
    }

    public ResponseEntity<String> getExpensesPerLastWeek(String username){
        return new ResponseEntity<>(expensesDAO.getExpensesPerLastWeek(username).toJson(), HttpStatus.OK);
    }

    public ResponseEntity<String> getExpensesPerLastMonth(String username){
        return new ResponseEntity<>(expensesDAO.getExpensesPerLastMonth(username).toJson(), HttpStatus.OK);
    }

    public ResponseEntity<String> getExpensesPerLastYear(String username){
        return new ResponseEntity<>(expensesDAO.getExpensesPerLastYear(username).toJson(), HttpStatus.OK);
    }

    public ResponseEntity<String> getExpensesPerCustomPeriod(String username, LocalDateTime from, LocalDateTime to){

        return new ResponseEntity<>(expensesDAO.getExpensesPerCustomPeriod(username, from, to).toJson(), HttpStatus.OK);
    }
}
