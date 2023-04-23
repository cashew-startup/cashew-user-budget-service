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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
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

    public List<UserCheck> getUserChecksFromDate(String username, ZonedDateTime from) throws NoSuchElementException {
        long userDetailsId = getUserDetailsIdByUsername(username);
        return userCheckRepository.findAllByUserDetailsIdAndDateAfter(userDetailsId, from);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getAllExpenses(String username) {
        long userDetailsId = getUserDetailsIdByUsername(username);
        List<UserCheck> checks = userCheckRepository.findAllByUserDetailsIdAndIsDisabled(userDetailsId, false);
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerLastDay(String username) {
        List<UserCheck> checks = getUserChecksFromDate(username.toLowerCase(Locale.ROOT).trim(), ZonedDateTime.now().minusDays(1L));
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerLastWeek(String username) {
        List<UserCheck> checks = getUserChecksFromDate(username.toLowerCase(Locale.ROOT).trim(), ZonedDateTime.now().minusDays(6));
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerLastMonth(String username) {
        Iterable<UserCheck> checks = getUserChecksFromDate(username.toLowerCase(Locale.ROOT).trim(), ZonedDateTime.now().minusDays(30));
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerLastYear(String username) {
        Iterable<UserCheck> checks = getUserChecksFromDate(username.toLowerCase(Locale.ROOT).trim(), ZonedDateTime.now().minusYears(1));
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getExpensesPerCustomPeriod(String username, String from, String to) {
        long userDetailsId = getUserDetailsIdByUsername(username);
        List<UserCheck> checks = userCheckRepository.findAllByUserDetailsIdAndDateIn(
                userDetailsId,
                prepareZonedDateTime(from),
                prepareZonedDateTime(to));
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ExpensesDTO.Response.Success> addReceipt(String username, String token) {
        Receipt receipt = fetchReceiptService.fetchReceipt(username.toLowerCase(Locale.ROOT).trim(), token);
        UserCheck userCheck = new UserCheck();
        userCheck.setReceipt(receipt);
        userCheck.setUserDetails(
                userRepository
                        .findByUsername(username.toLowerCase(Locale.ROOT).trim())
                        .orElseThrow(() -> new NoSuchElementException("No user with such username"))
                        .getUserDetails());
        userCheckRepository.save(userCheck);
        return new ResponseEntity<>(new ExpensesDTO.Response.Success(true), HttpStatus.OK);
    }

    public ResponseEntity<ExpensesDTO.Response.Success> disableReceiptForUser(String username, Long userCheckId) {
        UserCheck check = userCheckRepository.findById(userCheckId).orElseThrow(() -> new NoSuchElementException("Cannot find receipt with id:" + userCheckId));
        check.setIsDisabled(true);
        userCheckRepository.save(check);
        return new ResponseEntity<>(new ExpensesDTO.Response.Success(true), HttpStatus.OK);
    }

    private ZonedDateTime prepareZonedDateTime(String date) {
        List<String> dateParts = Arrays.asList(date.split("-"));
        return ZonedDateTime.of(
                Integer.parseInt(dateParts.get(0)),
                Integer.parseInt(dateParts.get(1)),
                Integer.parseInt(dateParts.get(2)),
                0,
                0,
                0,
                0,
                ZonedDateTime.now().getZone());
    }

    private Long getUserDetailsIdByUsername(String username){
        return userRepository
                .findByUsername(username.toLowerCase(Locale.ROOT).trim())
                .orElseThrow(() -> new NoSuchElementException("No user with such username"))
                .getUserDetails()
                .getId();
    }
}