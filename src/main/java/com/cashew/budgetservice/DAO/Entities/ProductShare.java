package com.cashew.budgetservice.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Entity
@Data
@Accessors(chain = true)
public class ProductShare {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_details_id")
    private UserDetails userDetails;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn
    private Product product;

    boolean isPaid = false;
    private BigDecimal sharedPrice;
}
