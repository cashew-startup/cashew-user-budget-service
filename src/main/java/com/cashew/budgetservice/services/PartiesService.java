package com.cashew.budgetservice.services;

import com.cashew.budgetservice.DAO.Entities.Party;
import com.cashew.budgetservice.DAO.Entities.User;
import com.cashew.budgetservice.DAO.Entities.UserDetails;
import com.cashew.budgetservice.DAO.Repos.PartyRepository;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import com.cashew.budgetservice.DTO.PartiesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Component
public class PartiesService {
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ResponseEntity<PartiesDTO.Response.Created> createParty(String name, Long ownerId) {
        name = name.toLowerCase();
        User u = userRepository.findById(ownerId).orElseThrow(() -> new NoSuchElementException("No user with such id"));
        Party p = new Party();
        p.setName(name);
        p.setDate(LocalDateTime.now());
        p.setOwnerId(ownerId);
        p.setListOfUserDetails(new ArrayList<>());
        p.getListOfUserDetails().add(u.getUserDetails());
        partyRepository.save(p);
        u.getUserDetails()
                .getParties()
                .add(p);
        userRepository.save(u);
        return new ResponseEntity<>(new PartiesDTO.Response.Created(p.getId()), HttpStatus.CREATED);
    }

    public ResponseEntity<PartiesDTO.Response.UsersList> getUsersOfParty(Long id) {
        Party party = partyRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No party with such id"));
        List<UserDetails> userDetails = party.getListOfUserDetails();
        List<String> userNames = new ArrayList<>();
        for (UserDetails ud : userDetails) {
            userNames.add(ud.getUser().getUsername());
        }
        return new ResponseEntity<>(new PartiesDTO.Response.UsersList(userNames), HttpStatus.OK);
    }

    public ResponseEntity<PartiesDTO.Response.FullInfo> getFullInfoOfParty(Long id) {
        Party party = partyRepository.findPartyById(id).orElseThrow(() -> new NoSuchElementException("No party with such id"));
        return new ResponseEntity<>(new PartiesDTO.Response.FullInfo(party), HttpStatus.OK);
    }

    public ResponseEntity<PartiesDTO.Response.PartiesList> getPartiesOfUser(String username) {
        List<Party> parties = userRepository
                .findTopByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("No user with such username"))
                .getUserDetails()
                .getParties();
        return new ResponseEntity<>(new PartiesDTO.Response.PartiesList(parties), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<PartiesDTO.Response.Success> addUserToParty(Long partyId, String username) {
        username = username.toLowerCase(Locale.ROOT).trim();
        Party p = partyRepository
                .findById(partyId)
                .orElseThrow(() -> new NoSuchElementException("No party with such id"));
        User u = userRepository
                .findTopByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("No user with such username"));
        if (u.getUserDetails().getParties().contains(p)
                ||
                p.getListOfUserDetails().contains(u.getUserDetails())) {
            throw new IllegalArgumentException("User is already in the party");
        } else {
            u.getUserDetails().getParties().add(p);
            p.getListOfUserDetails().add(u.getUserDetails());
        }
        userRepository.save(u);
        partyRepository.save(p);
        return new ResponseEntity<>(new PartiesDTO.Response.Success(true), HttpStatus.OK);

    }

    public ResponseEntity<PartiesDTO.Response.Success> removeUserFromParty(Long partyId, String username) {
        Party p = partyRepository
                .findById(partyId)
                .orElseThrow(()->new NoSuchElementException("No party with such id"));
        User u = userRepository
                .findTopByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("No user with such id"));
        if (username.equals(userRepository.findById(p.getOwnerId()).get().getUsername())){
            throw new IllegalArgumentException("Can't remove owner from party");
        }
        p.getListOfUserDetails().remove(u.getUserDetails());
        partyRepository.save(p);
        return new ResponseEntity<>(new PartiesDTO.Response.Success(true), HttpStatus.OK);
    }

    public ResponseEntity<PartiesDTO.Response.Success> changeName(Long partyId, String name) {
        Party p = partyRepository
                .findPartyById(partyId)
                .orElseThrow(() -> new NoSuchElementException("No party with such id"));
        p.setName(name);
        partyRepository.save(p);
        return new ResponseEntity<>(new PartiesDTO.Response.Success(true), HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<PartiesDTO.Response.Success> deleteParty(Long partyId) {
        Party p = partyRepository
                .findById(partyId)
                .orElseThrow(() -> new NoSuchElementException("No party with such id"));
        List<UserDetails> listOfUserDetails = p.getListOfUserDetails();
        listOfUserDetails.forEach((ud) -> ud.getParties().remove(p));
        partyRepository.deleteById(partyId);
        return new ResponseEntity<>(new PartiesDTO.Response.Success(true), HttpStatus.OK);
    }
}
