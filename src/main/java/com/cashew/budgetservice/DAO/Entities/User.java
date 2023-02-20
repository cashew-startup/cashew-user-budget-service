package com.cashew.budgetservice.DAO.Entities;

import com.cashew.budgetservice.DAO.CustomSerializers.UserSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Entity
@JsonSerialize(using = UserSerializer.class)
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String email;

    @CreatedDate
    @Getter
    @Setter
    private LocalDate date;

    @OneToOne(cascade=CascadeType.ALL)
    @Getter
    @Setter
    private UserDetails userDetails;
}

