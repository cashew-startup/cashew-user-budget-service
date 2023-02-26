package com.cashew.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

import static jakarta.persistence.GenerationType.AUTO;

@Data
@Entity
public class ReceiptHistory {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String username;

    private Date date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receipt_id", referencedColumnName = "id")
    private Receipt receipt;

}
