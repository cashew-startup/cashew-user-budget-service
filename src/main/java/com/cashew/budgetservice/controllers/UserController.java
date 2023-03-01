package com.cashew.budgetservice.controllers;

import com.cashew.budgetservice.DAO.Entities.User;
import com.cashew.budgetservice.DAO.Interfaces.UsersDAO;
import com.cashew.budgetservice.DTO.StatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UsersDAO dao;


    @PostMapping(path="/add")
    public ResponseEntity<?> createNewUser (
                        @RequestParam String username,
                        @RequestParam String email) {
        try {
            Long userId = dao.createUser(username, email);
            return new ResponseEntity<>(userId, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new StatusDTO(409,"User with such username not found").toJson(),
                    HttpStatus.CONFLICT
            );
        }
    }

    @GetMapping(path="/all")
    public Iterable<User> getAllUsers() {
        return dao.getAllUsers();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getUserById(@PathVariable(value = "id") Long id){
        Optional<User> response = dao.getUserById(id);
        if (response.isPresent()) {
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new StatusDTO(404,"User with such id not found").toJson(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping(path = "/byUsername/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable(value = "username") String username){
        Optional<User> response = dao.getUserByUsername(username);
        if (response.isPresent()) {
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new StatusDTO(400,"User with such username not found").toJson(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/byEmail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email){
        Optional<User> response = dao.getUserByEmail(email);
        if (response.isPresent()) {
            return new ResponseEntity<>(response.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new StatusDTO(400,"User with such email not found").toJson(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<?> updateUser(
                        @PathVariable(value="id") Long id,
                        @RequestParam String username,
                        @RequestParam String email){
        Optional<User> response = dao.updateUser(id, username, email);
        if (response.isPresent()){
            return new ResponseEntity<>(response.get(),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    new StatusDTO(400,"No user with such id").toJson(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<String> deleteUserById (@PathVariable(value = "id") Long id) {
        try {
            dao.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new StatusDTO(404,"User with such id not found").toJson(),
                    HttpStatus.NOT_FOUND
            );
        }
    }

}
