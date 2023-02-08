package com.cashew.budgetservice.accessingDataJPA;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonSerialize(using = CustomPartiesSerializer.class)
    private List<Party> parties = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Party> getParties() {
        return parties;
    }

    public void setParties(List<Party> parties) {
        this.parties = parties;
    }


    public void addUserToParty (Party party){
        parties.add(party);
        party.getUsers().add(this);
    }

    public void removeUserFromPartyByPartyId(long partyId) {
        Party party = this.parties.stream().filter(t -> t.getId() == partyId).findFirst().orElse(null);
        if (party != null) {
            this.parties.remove(party);
            party.getUsers().remove(this);
        }
    }
}
