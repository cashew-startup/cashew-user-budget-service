package com.cashew.budgetservice.DAO.Interfaces;

import com.cashew.budgetservice.DTO.ExpensesDTO;

import java.time.LocalDateTime;

public interface ExpensesDAO {
    ExpensesDTO getExpensesForToday(String username);
    ExpensesDTO getExpencesForLastWeek(String username);
    ExpensesDTO getExpensesForLastMonth(String username);
    ExpensesDTO getExpencesForLastYear(String username);
    ExpensesDTO getExpencesForCustomPeriod(String username,  LocalDateTime from, LocalDateTime to);
}
