package com.cashew.budgetservice.DAO.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class UserCheck {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @ManyToOne
    @Getter
    @Setter
    private UserDetails userDetails;

    @ManyToOne
    @Getter
    @Setter
    private Receipt receipt;

    @Getter
    @Setter
    private Boolean isDisabled = false;
}
