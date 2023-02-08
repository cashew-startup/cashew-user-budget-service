package com.cashew.budgetservice.controllers;

import com.cashew.budgetservice.accessingDataJPA.Party;
import com.cashew.budgetservice.accessingDataJPA.PartyRepository;
import com.cashew.budgetservice.accessingDataJPA.User;
import com.cashew.budgetservice.accessingDataJPA.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller // This means that this class is a Controller
@RequestMapping(path="/budget") // This means URL's start with /budget (after Application path)
public class BudgetController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PartyRepository partyRepository;

    @GetMapping(path="/users/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/users/{id}")
    public @ResponseBody
    ResponseEntity<User> getUserById(@PathVariable(value = "id") Long id){
        try {
            User user = userRepository.findById(id).orElseThrow();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NoSuchElementException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/users/byemail")
    public @ResponseBody ResponseEntity<User> getUserByEmail(@RequestParam String email){
        try {
            User user = userRepository.findByEmail(email.toLowerCase().trim()).orElseThrow();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NoSuchElementException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path="/users/add")
    public @ResponseBody ResponseEntity<?> createNewUser (
            @RequestParam String nickname,
            @RequestParam String email) {
        String preparedEmail = email.toLowerCase().trim();
        if (userRepository.findByEmail(preparedEmail).isPresent()){
            return new ResponseEntity<>("User with such email already exists",HttpStatus.BAD_REQUEST);
        }
        User n = new User();
        n.setName(nickname);
        n.setEmail(preparedEmail);
        userRepository.save(n);
        return new ResponseEntity<>("New user saved",HttpStatus.OK);
    }

    @DeleteMapping(path="/users/{Id}")
    public @ResponseBody String deleteUserById (@PathVariable(value = "Id") Long userId) {
        if (userRepository.existsById(userId)){
            userRepository.deleteById(userId);
            return "User was deleted";
        } else return "No user with such id";
    }

    @GetMapping(path="/groups/all")
    public @ResponseBody Iterable<Party> getAllParties() {
        return partyRepository.findAll();
    }

    @GetMapping(path="/groups/{id}")
    public @ResponseBody ResponseEntity<Party> getPartyById(@PathVariable(value = "id") Long partyId) {
        try {
            Party party = partyRepository.findById(partyId).orElseThrow();
            return new ResponseEntity<>(party, HttpStatus.OK);
        } catch (NoSuchElementException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path="/groups/find")
    public @ResponseBody ResponseEntity<Party> getPartyByNameAndCreatorId(
            @RequestParam String name,
            @RequestParam Long creatorId
    ) {
        try {
            Party party = partyRepository
                    .findPartyByNameAndCreatorId(name.toLowerCase().trim(), creatorId).orElseThrow();
            return new ResponseEntity<>(party, HttpStatus.OK);
        } catch (NoSuchElementException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path="/groups/add")
    public @ResponseBody ResponseEntity<?> createNewParty (
            @RequestParam String name,
            @RequestParam Long creatorId) {
        User user = userRepository.findById(creatorId).orElse(null);
        if (user == null){
            return new ResponseEntity<>("Cannot create group. User with such id not found",HttpStatus.BAD_REQUEST);
        }
        Party p = new Party();
        p.setName(name.toLowerCase(Locale.ROOT).trim());
        p.setCreatorId(creatorId);
        partyRepository.save(p);
        user.addUserToParty(
                partyRepository.findPartyByNameAndCreatorId(name, user.getId()).get()
        );
        userRepository.save(user);
        return new ResponseEntity<>("Group created",HttpStatus.OK);
    }

    @PostMapping(path="/groups/{partyId}/adduser/{userId}")
    public @ResponseBody ResponseEntity<?> addUserToParty (
            @PathVariable(value = "partyId") Long partyId,
            @PathVariable(value = "userId") Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Party> party = partyRepository.findById(partyId);
        if (user.isPresent()){
            if (party.isPresent()){
                user.get().addUserToParty(party.get());
            } else return new ResponseEntity<>("No party with such id",HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>("No user with such id",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path="/groups/{groupId}")
    public @ResponseBody ResponseEntity<?> deletePartyById (@PathVariable(value = "groupId") Long partyId) {
        if (partyRepository.existsById(partyId)){
            partyRepository.deleteById(partyId);
            return new ResponseEntity<>("Group was deleted",HttpStatus.OK);
        } else return new ResponseEntity<>("No group with such id",HttpStatus.OK);
    }
}
