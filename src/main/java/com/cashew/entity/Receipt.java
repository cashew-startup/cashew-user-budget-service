package com.cashew.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import static jakarta.persistence.GenerationType.AUTO;

@Data
@Entity
public class Receipt {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String company;
    private String address;
    private String INN;
    private String date;
    private String receiptNumber;
    private String shift;
    private String cashier;
    private BigDecimal total;
    private BigDecimal cash;
    private BigDecimal card;
    private BigDecimal VAT20;
    private BigDecimal VAT10;
    private String taxation;

    @OneToMany
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private List<Product> products = new ArrayList<>();


}
