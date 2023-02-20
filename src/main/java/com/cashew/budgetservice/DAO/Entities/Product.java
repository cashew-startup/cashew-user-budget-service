package com.cashew.budgetservice.DAO.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @OneToOne(cascade=CascadeType.ALL)
    @Getter
    @Setter
    private Price price;

    @Getter
    @Setter
    private LocalDate date;
}
