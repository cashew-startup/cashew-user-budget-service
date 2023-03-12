package com.cashew.budgetservice.services;

import com.cashew.budgetservice.DAO.Entities.User;
import com.cashew.budgetservice.DAO.Interfaces.UsersDAO;
import com.cashew.budgetservice.DTO.DTO;
import com.cashew.budgetservice.DTO.StatusDTO;
import com.cashew.budgetservice.DTO.UsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UsersService {
    private UsersDAO dao;

    @Autowired
    public UsersService(UsersDAO dao) {
        this.dao = dao;
    }

    public ResponseEntity<DTO> createUser(String username, String email){
        try {
            Long userId = dao.createUser(username, email);
            return new ResponseEntity<>(new UsersDTO.Response.Created(userId), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new StatusDTO(409, e.getMessage()), HttpStatus.CONFLICT
            );
        }
    }

    public ResponseEntity<DTO> getUserById(Long id){
        try {
            User user = dao.getUserById(id).orElseThrow();
            DTO response = new UsersDTO.Response.Found(user.getId(), user.getUsername(), user.getEmail());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(
                    new StatusDTO(404,"User with such id not found"), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<DTO> getUserByUsername(String username){
        try {
            User user = dao.getUserByUsername(username).orElseThrow();
            DTO response = new UsersDTO.Response.Found(user.getId(), user.getUsername(), user.getEmail());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(
                    new StatusDTO(404,"User with such username not found"), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<DTO> getUserByEmail(String email){
        try {
            User user = dao.getUserByEmail(email).orElseThrow();
            DTO response = new UsersDTO.Response.Found(user.getId(), user.getUsername(), user.getEmail());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(
                    new StatusDTO(404,"User with such email not found"), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<DTO> updateUser(Long id, String username, String email){
        try {
            User updatedUser = dao.updateUser(id, username, email).orElseThrow();
            UsersDTO.Response.Updated response = new UsersDTO.Response.Updated(
                    updatedUser.getId(),
                    updatedUser.getUsername(),
                    updatedUser.getEmail()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(
                    new StatusDTO(404,"User with such id not found"), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<DTO> deleteUserById(Long id){
        try {
            dao.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new StatusDTO(404,"User with such id not found"),
                    HttpStatus.NOT_FOUND
            );
        }
    }
}