package com.cashew.budgetservice.services;

import com.cashew.budgetservice.DAO.Entities.Receipt;
import com.cashew.budgetservice.DAO.Entities.UserCheck;
import com.cashew.budgetservice.DAO.Repos.UserCheckRepository;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import com.cashew.budgetservice.DTO.ExpensesDTO;
import com.cashew.budgetservice.exceptions.FetchDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class ExpensesService {
    private UserCheckRepository userCheckRepository;
    private UserRepository userRepository;
    private RestTemplate restTemplate;
    private String receiptServiceUrl;

    @Autowired
    public ExpensesService(UserCheckRepository userCheckRepository,
                           UserRepository userRepository,
                           RestTemplate restTemplate) {
        this.userCheckRepository = userCheckRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        receiptServiceUrl = System.getenv("RECEIPT-SERVICE-URL");
    }

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

    public ResponseEntity<ExpensesDTO.Response.Success> addReceipt(String username, String token){
        Receipt receipt = fetchReceiptDataFromReceiptService(username, token);
        UserCheck userCheck = new UserCheck();
        userCheck.setReceipt(receipt);
        userCheck.setUserDetails(
                userRepository
                        .findTopByUsername(username)
                        .orElseThrow(() -> new NoSuchElementException("No user with such username"))
                        .getUserDetails());
        userCheck.setIsDisabled(false);
        userCheckRepository.save(userCheck);
        return new ResponseEntity<>(new ExpensesDTO.Response.Success(), HttpStatus.OK);
    }

    public Receipt fetchReceiptDataFromReceiptService(String username, String token) {
        String url = receiptServiceUrl + "/receipt";
        HttpEntity<ExpensesDTO.Request.Fetch> request = new HttpEntity<>(new ExpensesDTO.Request.Fetch(username, token));
        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Object.class);
        if(response.getStatusCode() == HttpStatus.OK){
            return ((ExpensesDTO.Response.FetchedReceiptInfo) response.getBody()).getFetchedReceipt();
        } else {
            throw new FetchDataException(response.getBody().toString());
        }
    }

}
