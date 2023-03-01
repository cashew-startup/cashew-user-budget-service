package com.cashew.budgetservice.controllers;

import com.cashew.budgetservice.DAO.Entities.Party;
import com.cashew.budgetservice.DAO.Entities.UserDetails;
import com.cashew.budgetservice.DAO.Interfaces.PartyDAO;
import com.cashew.budgetservice.DTO.StatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/parties")
public class PartyController {
    @Autowired
    private PartyDAO dao;

    @PostMapping(path="/add")
    private ResponseEntity<?> createNewParty (
            @RequestParam String name,
            @RequestParam Long ownerId) {
        try {
            Long partyId = dao.createParty(name, ownerId);
            return new ResponseEntity<>(partyId, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("No user with id = ownerId", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{id}")
    private ResponseEntity<?> getUsersOfParty(@PathVariable Long id){
        Optional<Party> party = dao.getParty(id);
        if (dao.getParty(id).isPresent()) {
            Set<UserDetails> userDetails = dao.getParty(id).get().getSetOfUserDetails();
            List<String> userNames = new ArrayList<>();
            for (UserDetails ud : userDetails) {
                userNames.add(ud.getUser().getUsername() + "@" + ud.getUser().getId());
            }
            return new ResponseEntity<>(userNames, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new StatusDTO(400, "No party with such id"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{id}/info")
    private ResponseEntity<?> getFullInfoOfParty(@PathVariable Long id){
            return ResponseEntity.of(dao.getParty(id));
    }

    @GetMapping(path = "/all")
    private ResponseEntity<?> getAllParties(){
        return new ResponseEntity<>(dao.getAllParties(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    private ResponseEntity<String> deleteParty(@PathVariable(value = "id") Long partyId){
        try {
            dao.deleteParty(partyId);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("");
        } catch (NoSuchElementException e){
            return new ResponseEntity<> (
                    new StatusDTO(404,"No party with such id").toJson(),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/of/{id}")
    private ResponseEntity<?> getPartiesOfUser(@PathVariable(value = "id") Long userId){
        try {
            return new ResponseEntity<>(dao.getPartiesOfUser(userId), HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(new StatusDTO(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "/{id}/addUser/{userId}")
    private ResponseEntity<?> addUserToParty(@PathVariable(value = "id") Long partyId,
                                             @PathVariable(value = "userId") Long userId) {
        try {
            dao.addUserToParty(partyId, userId);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("");
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "/{partyId}/remove/{userId}")
    private ResponseEntity<String> removeUserFromParty(
            @PathVariable(value = "partyId") Long partyId,
            @PathVariable(value = "userId") Long userId){
        try {
            dao.removeUserFromParty(partyId, userId);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("");
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Can't remove owner from party", HttpStatus.BAD_REQUEST);
        }
    }
}
