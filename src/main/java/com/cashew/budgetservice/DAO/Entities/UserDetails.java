package com.cashew.budgetservice.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@EqualsAndHashCode
@Data
public class UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private User user;

    @OneToMany(cascade=CascadeType.ALL)
    @JsonBackReference
    private List<UserCheck> userChecks;

    @OneToMany
    private List<User> IncomingFriendRequests;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> friends;

    @ManyToMany(cascade=CascadeType.ALL)
    private List<Party> parties;
}