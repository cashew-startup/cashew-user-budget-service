package com.cashew.budgetservice.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Entity
@EqualsAndHashCode
@Data
public class UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToMany(cascade=CascadeType.ALL)
    @JsonBackReference
    private Set<UserCheck> userChecks;

    @OneToMany
    private Set<User> IncomingFriendRequests;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<User> friends;

    @ManyToMany(cascade=CascadeType.ALL)
    private Set<Party> parties;
}