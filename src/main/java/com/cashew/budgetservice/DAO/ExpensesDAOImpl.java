package com.cashew.budgetservice.DAO;

import com.cashew.budgetservice.DAO.Entities.UserCheck;
import com.cashew.budgetservice.DAO.Interfaces.ExpensesDAO;
import com.cashew.budgetservice.DAO.Repos.UserCheckRepository;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import com.cashew.budgetservice.DTO.ExpensesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Component
public class ExpensesDAOImpl implements ExpensesDAO {
    @Autowired
    UserCheckRepository userCheckRepository;
    @Autowired
    UserRepository userRepository;

    public Iterable<UserCheck> getUserChecks(String username, LocalDate from) throws NoSuchElementException {
        Iterable<UserCheck> userChecks = userCheckRepository.findAllByUserDetailsAndDateAfter(
                userRepository
                .findUserByUsername(username)
                .orElseThrow()
                .getUserDetails()
                        .getId(), from);
        return userChecks;
    }

    @Override
    public ExpensesDTO getExpensesForToday(String username) {
        Iterable<UserCheck> checks = getUserChecks(username, LocalDateTime.now().toLocalDate());
        ExpensesDTO result = new ExpensesDTO();
        result.setExpenses(checks);
        return result;
    }

    @Override
    public ExpensesDTO getExpencesForLastWeek(String username) {
        Iterable<UserCheck> checks = getUserChecks(username, LocalDateTime.now().minusDays(6).toLocalDate());
        return null;
    }

    @Override
    public ExpensesDTO getExpensesForLastMonth(String username) {
        Iterable<UserCheck> checks = getUserChecks(username, LocalDateTime.now().minusDays(30).toLocalDate());
        return null;
    }

    @Override
    public ExpensesDTO getExpencesForLastYear(String username) {
        Iterable<UserCheck> checks = getUserChecks(username, LocalDateTime.now().minusYears(1).toLocalDate());
        return null;
    }

    @Override
    public ExpensesDTO getExpencesForCustomPeriod(String username, LocalDateTime from, LocalDateTime to) {
        return null;
    }
}
