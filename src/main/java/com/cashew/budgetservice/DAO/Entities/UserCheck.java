package com.cashew.budgetservice.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
public class UserCheck {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private UserDetails userDetails;

    @ManyToOne
    private Receipt receipt;

    private Boolean isDisabled = false;
}
