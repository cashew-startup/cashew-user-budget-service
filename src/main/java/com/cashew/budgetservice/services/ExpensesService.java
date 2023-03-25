package com.cashew.budgetservice.services;

import com.cashew.budgetservice.DAO.Entities.UserCheck;
import com.cashew.budgetservice.DAO.Repos.UserCheckRepository;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import com.cashew.budgetservice.DTO.ExpensesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class ExpensesService {
    @Autowired
    UserCheckRepository userCheckRepository;
    @Autowired
    UserRepository userRepository;

    public List<UserCheck> getUserChecks(String username, LocalDateTime from) throws NoSuchElementException {
        long userDetailsId = userRepository
                .findTopByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("No user with such username"))
                .getUserDetails()
                .getId();
        return userCheckRepository.findAllByUserDetailsAndDateAfter(userDetailsId, from);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerLastDay(String username) {
        List<UserCheck> checks = getUserChecks(username, LocalDateTime.now().minusDays(1L));
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerLastWeek(String username) {
        List<UserCheck> checks = getUserChecks(username, LocalDateTime.now().minusDays(6));
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerLastMonth(String username) {
        Iterable<UserCheck> checks = getUserChecks(username, LocalDateTime.now().minusDays(30));
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerLastYear(String username) {
        Iterable<UserCheck> checks = getUserChecks(username, LocalDateTime.now().minusYears(1));
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerCustomPeriod(String username, LocalDateTime from, LocalDateTime to) {
        long userDetailsId = userRepository
                .findTopByUsername(username)
                .orElseThrow()
                .getUserDetails()
                .getId();
        List<UserCheck> checks = userCheckRepository.findAllByUserDetailsAndDateIn(userDetailsId, from, to);
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }
}
