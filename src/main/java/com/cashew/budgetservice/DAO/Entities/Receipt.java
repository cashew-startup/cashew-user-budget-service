package com.cashew.budgetservice.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
@Accessors(chain = true)
public class Receipt {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String company;
    private String address;
    private String INN;
    private ZonedDateTime date;
    private String receiptNumber;
    private String shift;
    private String cashier;
    private BigDecimal total;
    private BigDecimal cash;
    private BigDecimal card;
    private BigDecimal VAT20;
    private BigDecimal VAT10;
    private String taxation;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products;
}
