package com.cashew.budgetservice.DAO.Entities;

import com.cashew.budgetservice.DAO.CustomSerializers.UserSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@JsonSerialize(using = UserSerializer.class)
@Data
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String username;

    private String email;

    @CreatedDate
    private LocalDateTime date;

    @OneToOne(cascade=CascadeType.PERSIST)
    private UserDetails userDetails;
}

