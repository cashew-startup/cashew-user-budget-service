package com.cashew.budgetservice.DAO.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@EqualsAndHashCode
public class UserDetails {

    @Id
    @GeneratedValue
    @Getter @Setter
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @Getter @Setter
    private User user;

    @OneToMany(cascade=CascadeType.PERSIST)
    @Getter
    @Setter
    private Set<UserCheck> userChecks;

    @OneToMany
    @Getter
    @Setter
    private Set<User> IncomingFriendRequests;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @Getter
    @Setter
    private Set<User> friends;

    @ManyToMany(cascade=CascadeType.PERSIST)
    @Getter
    @Setter
    private Set<Party> parties;
}
