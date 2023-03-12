package com.cashew.budgetservice.services;

import com.cashew.budgetservice.DAO.Interfaces.FriendsDAO;
import com.cashew.budgetservice.DTO.DTO;
import com.cashew.budgetservice.DTO.FriendsDTO;
import com.cashew.budgetservice.DTO.StatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FriendsService {
    private FriendsDAO dao;

    @Autowired
    public FriendsService(FriendsDAO dao) {
        this.dao = dao;
    }


    public ResponseEntity<DTO> getFriends(String username) {
        try {
            List<String> usernames = new ArrayList<>();
            dao.getFriends(username).forEach((user)-> usernames.add(user.getUsername()));
            return new ResponseEntity<>(new FriendsDTO.Response.GetFriends(usernames), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new StatusDTO(404,"User not found"), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<DTO> getFriendRequests(String username) {
        try {
            List<String> usernames = new ArrayList<>();
            dao.getFriendRequests(username).forEach((user)-> usernames.add(user.getUsername()));
            return new ResponseEntity<>(new FriendsDTO.Response.GetFriends(usernames), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new StatusDTO(404,"User not found"), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<DTO> sendRequest(String sender, String receiver) {
        try {
            dao.sendRequest(sender, receiver);
            return new ResponseEntity<>(new FriendsDTO.Response.Success(true),HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new StatusDTO(404,e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<DTO> acceptRequest(String sender, String receiver) {
        try {
            dao.acceptRequest(sender, receiver);
            return new ResponseEntity<>(new FriendsDTO.Response.Success(true), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new StatusDTO(404,e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<DTO> declineRequest(String sender, String receiver) {
        try {
            dao.declineRequest(sender, receiver);
            return new ResponseEntity<>(new FriendsDTO.Response.Success(true), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new StatusDTO(404,e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<DTO> removeFromFriends(String deleter, String deleted) {
        try {
            dao.removeFromFriends(deleter, deleted);
            return new ResponseEntity<>(new FriendsDTO.Response.Success(true), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new StatusDTO(404,e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}
