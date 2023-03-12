package com.cashew.budgetservice.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private UserDetails userDetails;

    @ManyToOne(cascade = CascadeType.ALL)
    private Receipt receipt;

    private Boolean isDisabled = false;
}
