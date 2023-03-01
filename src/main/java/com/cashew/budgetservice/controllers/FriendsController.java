package com.cashew.budgetservice.controllers;

import com.cashew.budgetservice.DAO.Interfaces.FriendsDAO;
import com.cashew.budgetservice.DTO.StatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/friends")
public class FriendsController {
    @Autowired
    private FriendsDAO dao;

    @GetMapping(path = "/{username}")
    public ResponseEntity<?> getFriends(@PathVariable(value = "username") String username){
        try {
            return new ResponseEntity(dao.getFriends(username), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new StatusDTO(404,"User not found").toJson(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/{username}/requests")
    public ResponseEntity<?> getFriendRequests(@PathVariable(value = "username") String username){
        try {
            return new ResponseEntity(dao.getFriendRequests(username), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new StatusDTO(404,"User not found").toJson(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/request/send")
    public ResponseEntity<?> sendRequest(@RequestParam String sender, @RequestParam String recipient){
        try {
            dao.sendRequest(sender, recipient);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new StatusDTO(404,e.getMessage()).toJson(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/request/accept")
    public ResponseEntity<?> acceptRequest(@RequestParam String sender, @RequestParam String recipient){
        try {
            dao.acceptRequest(sender, recipient);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new StatusDTO(404,e.getMessage()).toJson(), HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/request/decline")
    public ResponseEntity<?> declineRequest(@RequestParam String sender, @RequestParam String recipient){
        try {
            dao.declineRequest(sender, recipient);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new StatusDTO(404,e.getMessage()).toJson(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/remove/")
    public ResponseEntity<?> removeFromFriends(@RequestParam String deleter, @RequestParam String deleted){
        try {
            dao.deleteFromFriends(deleter, deleted);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new StatusDTO(404,e.getMessage()).toJson(), HttpStatus.NOT_FOUND);
        }
    }
}
