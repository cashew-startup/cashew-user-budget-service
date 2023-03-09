package com.cashew.budgetservice.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToOne(cascade = CascadeType.PERSIST)
    private User user;

    @OneToMany(cascade=CascadeType.PERSIST)
    @JsonManagedReference
    private Set<UserCheck> userChecks;

    @OneToMany
    private Set<User> IncomingFriendRequests;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<User> friends;

    @ManyToMany(cascade=CascadeType.PERSIST)
    private Set<Party> parties;
}