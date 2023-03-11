package com.cashew.budgetservice.services;

import com.cashew.budgetservice.DAO.Interfaces.ExpensesDAO;
import com.cashew.budgetservice.DTO.DTO;
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

    public ResponseEntity<DTO> getExpensesPerLastDay(String username){
        return new ResponseEntity<>(expensesDAO.getExpensesPerLastDay(username), HttpStatus.OK);
    }

    public ResponseEntity<DTO> getExpensesPerLastWeek(String username){
        return new ResponseEntity<>(expensesDAO.getExpensesPerLastWeek(username), HttpStatus.OK);
    }

    public ResponseEntity<DTO> getExpensesPerLastMonth(String username){
        return new ResponseEntity<>(expensesDAO.getExpensesPerLastMonth(username), HttpStatus.OK);
    }

    public ResponseEntity<DTO> getExpensesPerLastYear(String username){
        return new ResponseEntity<>(expensesDAO.getExpensesPerLastYear(username), HttpStatus.OK);
    }

    public ResponseEntity<DTO> getExpensesPerCustomPeriod(String username, LocalDateTime from, LocalDateTime to){

        return new ResponseEntity<>(expensesDAO.getExpensesPerCustomPeriod(username, from, to), HttpStatus.OK);
    }
}
