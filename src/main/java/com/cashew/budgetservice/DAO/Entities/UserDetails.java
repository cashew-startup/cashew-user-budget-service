package com.cashew.budgetservice.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Fetch;

import java.util.List;

@Entity
@EqualsAndHashCode
@Data
public class UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @EqualsAndHashCode.Exclude
    private User user;

    @OneToMany
    @JsonBackReference
    private List<UserCheck> userChecks;


    @OneToMany
    @JoinTable(
            name = "user_details_incoming_friend_requests",
            joinColumns = @JoinColumn(name = "user_details_id"),
            inverseJoinColumns = @JoinColumn(name = "incoming_friend_request_user_id")
    )
    private List<User> incomingFriendRequests;

    @OneToMany
    @JoinTable(
            name = "user_details_friends",
            joinColumns = @JoinColumn(name = "user_details_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_user_id")
    )
    private List<User> friends;

    @ManyToMany
    @JoinTable(
            name = "user_details_parties",
            joinColumns = @JoinColumn(name = "user_details_id"),
            inverseJoinColumns = @JoinColumn(name = "party_id")
    )
    private List<Party> parties;
}