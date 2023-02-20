package com.cashew.budgetservice.DAO.Entities;

import com.cashew.budgetservice.DAO.CustomSerializers.UserDetailsSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@JsonSerialize(using = UserDetailsSerializer.class)
public class UserDetails {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @OneToOne(cascade=CascadeType.ALL)
    @Getter
    @Setter
    private User user;

    @OneToMany(cascade=CascadeType.ALL)
    @Getter
    @Setter
    private List<UserCheck> userChecks;

    @ManyToMany(cascade=CascadeType.ALL)
    @Getter
    @Setter
    private List<Party> parties;
}
