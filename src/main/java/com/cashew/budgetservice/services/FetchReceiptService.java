package com.cashew.budgetservice.services;

import com.cashew.budgetservice.DAO.Entities.Receipt;
import com.cashew.budgetservice.DAO.Repos.ProductRepository;
import com.cashew.budgetservice.DAO.Repos.ReceiptRepository;
import com.cashew.budgetservice.DTO.ExpensesDTO;
import com.cashew.budgetservice.exceptions.FetchDataException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class FetchReceiptService {
    private ReceiptRepository receiptRepository;
    private ProductRepository productRepository;
    private RestTemplate restTemplate;
    private String receiptServiceIpAndPort;

    @Autowired
    public FetchReceiptService(ReceiptRepository receiptRepository,
                               ProductRepository productRepository,
                               RestTemplate restTemplate) {
        this.receiptRepository = receiptRepository;
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
        this.receiptServiceIpAndPort = System.getenv("RECEIPT_SERVICE_IP_AND_PORT");
    }

    public Receipt fetchReceipt(String username, String token) {
        String url = "http://" + receiptServiceIpAndPort + "/receipt";
        log.error(url);
        HttpEntity<ExpensesDTO.Request.Fetch> request = new HttpEntity<>(new ExpensesDTO.Request.Fetch(username, token));
        ResponseEntity<FetchedReceiptInfo> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                FetchedReceiptInfo.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody().prepareReceipt();
        } else {
            throw new FetchDataException("Failed to fetch receipt from receipt service");
        }
    }

    @Data
    public static class FetchedReceiptInfo{
        private String username;
        private String date;
        private ReceiptDTO receipt;

        private ZonedDateTime parseDate(String date){
            String pattern = "dd.MM.yyyy HH:mm";
            ArrayList<String> dateParts = new ArrayList<>(Arrays.asList(date.split("[.:\\s]")));
            return ZonedDateTime.of(
                    Integer.parseInt(dateParts.get(2)),
                    Integer.parseInt(dateParts.get(1)),
                    Integer.parseInt(dateParts.get(0)),
                    Integer.parseInt(dateParts.get(3)),
                    Integer.parseInt(dateParts.get(4)),
                    0,
                    0,
                    ZonedDateTime.now().getZone());
        }


        private Receipt prepareReceipt(){
            Receipt r = new Receipt();
            r.setCompany(Objects.requireNonNullElse(receipt.getCompany(), "null"));
            r.setAddress(Objects.requireNonNullElse(receipt.getAddress(), "null"));
            r.setINN(Objects.requireNonNullElse(receipt.getINN(), "null"));
            r.setDate(parseDate(receipt.getDate()));
            r.setReceiptNumber(Objects.requireNonNullElse(receipt.getReceiptNumber(), "null"));
            r.setShift(Objects.requireNonNullElse(receipt.getShift(), "null"));
            r.setCashier(Objects.requireNonNullElse(receipt.getCashier(), "null"));
            r.setTotal(new BigDecimal(Objects.requireNonNullElse(receipt.getTotal(), "0")));
            r.setCash(new BigDecimal(Objects.requireNonNullElse(receipt.getCash(), "0")));
            r.setCard(new BigDecimal(Objects.requireNonNullElse(receipt.getCard(), "0")));
            r.setVAT20(new BigDecimal(Objects.requireNonNullElse(receipt.getVAT20(), "0")));
            r.setVAT10(new BigDecimal(Objects.requireNonNullElse(receipt.getVAT10(), "0")));
            r.setTaxation(Objects.requireNonNullElse(receipt.getTaxation(), "null"));

            List<com.cashew.budgetservice.DAO.Entities.Product> products = new ArrayList<>();
            receipt.getProducts().forEach((product) -> {
                com.cashew.budgetservice.DAO.Entities.Product p = new com.cashew.budgetservice.DAO.Entities.Product();
                p.setName(product.getName());
                p.setDescription("");
                p.setCurrency("RUB");
                p.setPricePerUnit(product.getPrice());
                p.setQuantity(Double.valueOf(product.getCount()));
                p.setTotalPrice(product.getSumPrice());
                products.add(p);
            });
            r.setProducts(products);
            return r;
        }
    }
    @Data
    public static class ReceiptDTO {
        private String company;
        private String address;
        private String INN;
        private String date;
        private String receiptNumber;
        private String shift;
        private String cashier;
        private String total;
        private String cash;
        private String card;
        private String VAT20;
        private String VAT10;
        private String taxation;
        private List<Product> products;
    }
    @Data
    public static class Product {
        private String id;
        private String name;
        private BigDecimal price;
        private String count;
        private BigDecimal sumPrice;
    }
}
