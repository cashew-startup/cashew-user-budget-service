package com.cashew.budgetservice.DAO.Interfaces;

import com.cashew.budgetservice.DTO.ExpensesDTO.Response.RequestedChecks;

import java.time.LocalDateTime;

public interface ExpensesDAO {
    RequestedChecks getExpensesPerLastDay(String username);
    RequestedChecks getExpensesPerLastWeek(String username);
    RequestedChecks getExpensesPerLastMonth(String username);
    RequestedChecks getExpensesPerLastYear(String username);
    RequestedChecks getExpensesPerCustomPeriod(String username, LocalDateTime from, LocalDateTime to);
}
