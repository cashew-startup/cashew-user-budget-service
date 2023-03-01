package com.cashew.budgetservice.DAO.Entities;

import com.cashew.budgetservice.DAO.CustomSerializers.PartySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@JsonSerialize(using = PartySerializer.class)
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

    @ManyToMany(cascade = CascadeType.MERGE, mappedBy = "parties")
    @Getter
    @Setter
    private Set<UserDetails> setOfUserDetails;

    @CreatedDate
    @Getter
    @Setter
    private LocalDateTime date;
}
