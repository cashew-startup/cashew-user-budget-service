package com.cashew.budgetservice.DAO.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Receipt {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @OneToMany
    @Getter
    @Setter
    private List<Product> products;

    @Getter
    @Setter
    private LocalDate date;
}
