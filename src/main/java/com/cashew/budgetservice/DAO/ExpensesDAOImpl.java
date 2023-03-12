package com.cashew.budgetservice.DAO;

import com.cashew.budgetservice.DAO.Entities.UserCheck;
import com.cashew.budgetservice.DAO.Interfaces.ExpensesDAO;
import com.cashew.budgetservice.DAO.Repos.UserCheckRepository;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import com.cashew.budgetservice.DTO.ExpensesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class ExpensesDAOImpl implements ExpensesDAO {
    @Autowired
    UserCheckRepository userCheckRepository;
    @Autowired
    UserRepository userRepository;

    public List<UserCheck> getUserChecks(String username, LocalDateTime from) throws NoSuchElementException {
        long userDetailsId = userRepository
                .findUserByUsername(username)
                .orElseThrow()
                .getUserDetails()
                .getId();
        return userCheckRepository.findAllByUserDetailsAndDateAfter(userDetailsId, from);
    }

    @Override
    public ExpensesDTO.Response.RequestedChecks getExpensesPerLastDay(String username) {
        List<UserCheck> checks = getUserChecks(username, LocalDateTime.now().minusDays(1L));
        return new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks);
    }

    @Override
    public ExpensesDTO.Response.RequestedChecks getExpensesPerLastWeek(String username) {
        List<UserCheck> checks = getUserChecks(username, LocalDateTime.now().minusDays(6));
        return new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks);
    }

    @Override
    public ExpensesDTO.Response.RequestedChecks getExpensesPerLastMonth(String username) {
        Iterable<UserCheck> checks = getUserChecks(username, LocalDateTime.now().minusDays(30));
        return new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks);
    }

    @Override
    public ExpensesDTO.Response.RequestedChecks getExpensesPerLastYear(String username) {
        Iterable<UserCheck> checks = getUserChecks(username, LocalDateTime.now().minusYears(1));
        return new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks);
    }

    @Override
    public ExpensesDTO.Response.RequestedChecks getExpensesPerCustomPeriod(String username, LocalDateTime from, LocalDateTime to) {
        long userDetailsId = userRepository
                .findUserByUsername(username)
                .orElseThrow()
                .getUserDetails()
                .getId();
        List<UserCheck> checks = userCheckRepository.findAllByUserDetailsAndDateIn(userDetailsId, from, to);
        return new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks);
    }
}
