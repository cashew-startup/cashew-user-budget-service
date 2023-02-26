package com.cashew.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.AUTO;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String name;
    private BigDecimal price;
    private String count;
    private BigDecimal sumPrice;

}
