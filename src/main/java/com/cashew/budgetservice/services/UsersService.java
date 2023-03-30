package com.cashew.budgetservice.services;

import com.cashew.budgetservice.DAO.Entities.User;
import com.cashew.budgetservice.DAO.Entities.UserDetails;
import com.cashew.budgetservice.DAO.Repos.UserDetailsRepository;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import com.cashew.budgetservice.DTO.UsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UsersService {
    private UserRepository userRepository;
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    public UsersService(UserRepository userRepository,
                        UserDetailsRepository userDetailsRepository) {
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    @Transactional
    public ResponseEntity<UsersDTO.Response.Created> createUser(String username, String email) {
        username = username.toLowerCase().trim();
        email = email.toLowerCase().trim();
        if (userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("Not unique username");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Not unique email");
        }
        User u = new User();
        u.setEmail(email);
        u.setUsername(username);
        u.setDate(LocalDateTime.now());
        UserDetails ud = new UserDetails();
        ud.setUser(u);
        u.setUserDetails(ud);
        userRepository.save(u);
        return new ResponseEntity<>(
                new UsersDTO.Response.Created(u.getId()),
                HttpStatus.CREATED);
    }

    public ResponseEntity<UsersDTO.Response.Found> getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No user with such id"));
        return new ResponseEntity<>(
                new UsersDTO.Response.Found(user.getId(), user.getUsername(), user.getEmail()),
                HttpStatus.OK);
    }

    public ResponseEntity<UsersDTO.Response.Found> getUserByUsername(String username) {
        username = username.toLowerCase().trim();
        User user = userRepository.findTopByUsername(username).orElseThrow(() -> new NoSuchElementException("No user with such username"));
        return new ResponseEntity<>(
                new UsersDTO.Response.Found(user.getId(), user.getUsername(), user.getEmail()),
                HttpStatus.OK);
    }

    public ResponseEntity<UsersDTO.Response.Found> getUserByEmail(String email) {
        email = email.toLowerCase().trim();
        User user = userRepository.findTopByEmail(email).orElseThrow(() -> new NoSuchElementException("No user with such email"));
        return new ResponseEntity<>(
                new UsersDTO.Response.Found(user.getId(), user.getUsername(), user.getEmail()),
                HttpStatus.OK);
    }

    public ResponseEntity<UsersDTO.Response.Updated> updateUser(Long id, String username, String email) {
        username = username.toLowerCase().trim();
        email = email.toLowerCase().trim();
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No user with such id"));
        user.setUsername(username);
        user.setEmail(email);
        userRepository.save(user);
        return new ResponseEntity<>(
                new UsersDTO.Response.Updated(user.getId(), user.getUsername(), user.getEmail()),
                HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<UsersDTO.Response.Deleted> deleteUserById(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("No user with such id"));
        UserDetails userDetails = userDetailsRepository
                .findById(user.getUserDetails().getId())
                .orElseThrow(() -> new NoSuchElementException("Check database. UserID != UserDetailsID"));
        userDetailsRepository.deleteFriendRequestsByUserDetailsId(id);
        userDetailsRepository.deleteFriendsByUserDetailsId(id);
        userDetailsRepository.deleteUserDetailsFromAllParties(id);
        userDetailsRepository.save(userDetails);
        userDetailsRepository.delete(userDetails);
        userRepository.deleteById(id);
        return new ResponseEntity<>(
                new UsersDTO.Response.Deleted(true),
                HttpStatus.OK
        );
    }
}
