package com.cashew.budgetservice.DAO.Entities;

import com.cashew.budgetservice.DAO.CustomSerializers.PartySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@JsonSerialize(using = PartySerializer.class)
@Data
public class Party {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String name;

    private Long ownerId;

    private String ownerUsername;

    @ManyToMany(mappedBy = "parties")
    @ToString.Exclude
    private List<UserDetails> listOfUserDetails;

    @CreatedDate
    private LocalDateTime date;
}
