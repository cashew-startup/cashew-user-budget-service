package com.cashew.budgetservice.services;

import com.cashew.budgetservice.DAO.Entities.*;
import com.cashew.budgetservice.DAO.Repos.ProductRepository;
import com.cashew.budgetservice.DAO.Repos.ReceiptRepository;
import com.cashew.budgetservice.DAO.Repos.UserCheckRepository;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import com.cashew.budgetservice.DTO.ExpensesDTO;
import com.cashew.budgetservice.DTO.UserExpensesDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

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
        long userDetailsId = getUserDetailsByUsername(username).getId();
        return userCheckRepository.findAllByUserDetailsIdAndDateAfter(userDetailsId, from);
    }

    public ResponseEntity<ExpensesDTO.Response.RequestedChecks> getAllExpenses(String username) {
        long userDetailsId = getUserDetailsByUsername(username).getId();
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
        long userDetailsId = getUserDetailsByUsername(username).getId();
        List<UserCheck> checks = userCheckRepository.findAllByUserDetailsIdAndDateIn(
                userDetailsId,
                prepareZonedDateTime(from),
                prepareZonedDateTime(to));
        return new ResponseEntity<>(new ExpensesDTO.Response.RequestedChecks().setExpensesAsChecks(checks), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ExpensesDTO.Response.Success> addReceipt(String username, String token, List<UserExpensesDTO> userExpenses) {
        String finalUsername = username.toLowerCase(Locale.ROOT);
        Receipt receipt = fetchReceiptService.fetchReceipt(finalUsername, token);
        processProductSharesForProducts(receipt.getProducts(), userExpenses);
        createUserChecks(receipt, userExpenses, finalUsername);
        List<UserCheck> allСhecks = userCheckRepository.findAllByUserDetailsId(getUserDetailsByUsername(username).getId());
        return new ResponseEntity<>(new ExpensesDTO.Response.Success(true), HttpStatus.OK);
    }

    private void processProductSharesForProducts(List<Product> products, List<UserExpensesDTO> userExpenses) {
        Map<String, List<ProductShare>> productNamesToShares = new HashMap<>();
        products.forEach(product -> {
            productNamesToShares.put(product.getName(), new ArrayList<>());
        });
        userExpenses.forEach(userExpensesDTO -> {
            userExpensesDTO.getExpenses().forEach(productShareDTO -> {
                productNamesToShares.get(productShareDTO.getName()).add(
                        new ProductShare()
                                .setSharedPrice(new BigDecimal(productShareDTO.getPrice()))
                                .setUserDetails(
                                        getUserDetailsByUsername(userExpensesDTO.getUsername())
                                ));
            });
        });
        productNamesToShares.forEach((productName, productShares) -> {
            products.stream().filter(product -> product.getName().equals(productName)).findFirst().ifPresent(product -> {
                product.setShares(productShares);
            });
        });
    }

    private void createUserChecks(Receipt receipt, List<UserExpensesDTO> userExpenses, String ownerUsername) {
        userExpenses.forEach(userExpensesDTO -> {
            String username = userExpensesDTO.getUsername().toLowerCase(Locale.ROOT);
            UserCheck userCheck = new UserCheck()
                    .setReceipt(receipt)
                    .setPaid(username.equals(ownerUsername))
                    .setUserDetails(
                    getUserDetailsByUsername(username));
            userCheckRepository.save(userCheck);
        });
    }


    public ResponseEntity<ExpensesDTO.Response.Success> disableReceiptForUser(String username, Long receiptId) {
        List<UserCheck> allChecks = userCheckRepository.findAllByUserDetailsId(getUserDetailsByUsername(username).getId());
        UserCheck check = userCheckRepository.findByUserDetailsIdAndReceiptId(getUserDetailsByUsername(username).getId(), receiptId).orElseThrow(() -> new NoSuchElementException("Cannot find receipt with id:" + receiptId));
        check.setDisabled(true);
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

    private UserDetails getUserDetailsByUsername(String username){
        return userRepository
                .findByUsername(username.toLowerCase(Locale.ROOT).trim())
                .orElseThrow(() -> new NoSuchElementException("No user with such username"))
                .getUserDetails();
    }
}