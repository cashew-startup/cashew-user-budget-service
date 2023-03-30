package com.cashew.budgetservice.services;

import com.cashew.budgetservice.DAO.Entities.Receipt;
import com.cashew.budgetservice.DAO.Entities.UserCheck;
import com.cashew.budgetservice.DAO.Repos.ProductRepository;
import com.cashew.budgetservice.DAO.Repos.ReceiptRepository;
import com.cashew.budgetservice.DAO.Repos.UserCheckRepository;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import com.cashew.budgetservice.DTO.ExpensesDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Component
@Slf4j
public class ExpensesService {
    private UserCheckRepository userCheckRepository;
    private ReceiptRepository receiptRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private FetchReceiptService fetchReceiptService;

    @Autowired
    public ExpensesService(UserCheckRepository userCheckRepository,
                           ReceiptRepository receiptRepository,
                           UserRepository userRepository,
                           FetchReceiptService fetchReceiptService) {
        this.userCheckRepository = userCheckRepository;
        this.receiptRepository = receiptRepository;
        this.userRepository = userRepository;
        this.fetchReceiptService = fetchReceiptService;
    }

    public List<UserCheck> getUserChecks(String username, ZonedDateTime from) throws NoSuchElementException {
        long userDetailsId = userRepository
                .findTopByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("No user with such username"))
                .getUserDetails()
                .getId();
        return userCheckRepository.findAllByUserDetailsAndDateAfter(userDetailsId, from);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerLastDay(String username) {
        List<UserCheck> checks = getUserChecks(username, ZonedDateTime.now().minusDays(1L));
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerLastWeek(String username) {
        List<UserCheck> checks = getUserChecks(username, ZonedDateTime.now().minusDays(6));
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerLastMonth(String username) {
        Iterable<UserCheck> checks = getUserChecks(username, ZonedDateTime.now().minusDays(30));
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerLastYear(String username) {
        Iterable<UserCheck> checks = getUserChecks(username, ZonedDateTime.now().minusYears(1));
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerCustomPeriod(String username, String from, String to) {
        long userDetailsId = userRepository
                .findTopByUsername(username)
                .orElseThrow()
                .getUserDetails()
                .getId();
        List<UserCheck> checks = userCheckRepository.findAllByUserDetailsAndDateIn(
                userDetailsId,
                ZonedDateTime.parse(from),
                ZonedDateTime.parse(to));
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ExpensesDTO.Response.Success> addReceipt(String username, String token) {
        Receipt receipt = fetchReceiptService.fetchReceipt(username, token);
        receiptRepository.save(receipt);
        UserCheck userCheck = new UserCheck();
        userCheck.setReceipt(receipt);
        userCheck.setUserDetails(
                userRepository
                        .findTopByUsername(username)
                        .orElseThrow(() -> new NoSuchElementException("No user with such username"))
                        .getUserDetails());
        userCheckRepository.save(userCheck);
        return new ResponseEntity<>(new ExpensesDTO.Response.Success(true), HttpStatus.OK);
    }
}