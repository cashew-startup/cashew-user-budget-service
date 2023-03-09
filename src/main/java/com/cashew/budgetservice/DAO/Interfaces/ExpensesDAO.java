package com.cashew.budgetservice.DAO.Interfaces;

import com.cashew.budgetservice.DTO.ExpensesDTO.Response.Expenses;

import java.time.LocalDateTime;

public interface ExpensesDAO {
    Expenses getExpensesPerLastDay(String username);
    Expenses getExpensesPerLastWeek(String username);
    Expenses getExpensesPerLastMonth(String username);
    Expenses getExpensesPerLastYear(String username);
    Expenses getExpensesPerCustomPeriod(String username, LocalDateTime from, LocalDateTime to);
}
