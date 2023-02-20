package com.cashew.budgetservice.DAO.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.*;

@Entity
public class Party {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Long ownerId;

    @ManyToMany(cascade=CascadeType.ALL)
    @Getter
    @Setter
    private List<UserDetails> userDetails;

    @CreatedDate
    @Getter
    @Setter
    private LocalDate date;
}
