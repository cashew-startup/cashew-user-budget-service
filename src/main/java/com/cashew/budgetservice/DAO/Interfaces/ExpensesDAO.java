package com.cashew.budgetservice.DAO.Interfaces;

import com.cashew.budgetservice.DTO.ExpensesDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public interface ExpensesDAO {
    ExpensesDTO getExpensesForLastDay();
    ExpensesDTO getExpencesForLastWeek();
    ExpensesDTO getExpensesForLastMonth();
    ExpensesDTO getExpencesForLastYear();
    ExpensesDTO getExpencesForCustomPeriod(LocalDateTime from, LocalDateTime to);
}
