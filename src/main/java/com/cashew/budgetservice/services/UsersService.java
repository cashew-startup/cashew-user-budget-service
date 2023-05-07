package com.cashew.budgetservice.services;

import com.cashew.budgetservice.DAO.Entities.User;
import com.cashew.budgetservice.DAO.Entities.UserDetails;
import com.cashew.budgetservice.DAO.Repos.UserDetailsRepository;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import com.cashew.budgetservice.DTO.UsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.NoSuchElementException;

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
                new UsersDTO.Response.Created(true),
                HttpStatus.CREATED);
    }

    @Deprecated
    public ResponseEntity<UsersDTO.Response.Found> getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No user with such id"));
        return new ResponseEntity<>(
                new UsersDTO.Response.Found(user.getUsername(), user.getEmail()),
                HttpStatus.OK);
    }

    public ResponseEntity<UsersDTO.Response.Found> getUserByUsername(String username) {
        String preparedUsername = username.toLowerCase().trim();
        User user = userRepository
                .findByUsername(preparedUsername)
                .orElseThrow(() -> new NoSuchElementException("No user with username="+ preparedUsername));
        return new ResponseEntity<>(
                new UsersDTO.Response.Found(user.getUsername(), user.getEmail()),
                HttpStatus.OK);
    }

    public ResponseEntity<UsersDTO.Response.Found> getUserByEmail(String email) {
        String preparedEmail = email.toLowerCase().trim();
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("No user with such email="+ preparedEmail));
        return new ResponseEntity<>(
                new UsersDTO.Response.Found(user.getUsername(), user.getEmail()),
                HttpStatus.OK);
    }

    public ResponseEntity<UsersDTO.Response.Updated> updateUsername(String newUsername, String email) {
        newUsername = newUsername.toLowerCase().trim();
        String preparedEmail = email.toLowerCase().trim();
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("No user with such email="+ preparedEmail));
        user.setUsername(newUsername);
        userRepository.save(user);
        return new ResponseEntity<>(
                new UsersDTO.Response.Updated(user.getUsername(), user.getEmail()),
                HttpStatus.OK);
    }

    public ResponseEntity<UsersDTO.Response.Updated> updateEmail(String username, String newEmail) {
        String preparedUsername = username.toLowerCase().trim();
        newEmail = newEmail.toLowerCase(Locale.ROOT).trim();
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("No user with such username="+ preparedUsername));
        user.setEmail(newEmail);
        userRepository.save(user);
        return new ResponseEntity<>(
                new UsersDTO.Response.Updated(user.getUsername(), user.getEmail()),
                HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<UsersDTO.Response.Deleted> deleteUserByUsername(String username) {
        User user = userRepository
                .findByUsername(username.toLowerCase(Locale.ROOT).trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username=" + username));
        UserDetails userDetails = userDetailsRepository
                .findById(user.getUserDetails().getId())
                .orElseThrow(() -> new DataRetrievalFailureException(""));
        Long userDetailsId = userDetails.getId();
        userDetailsRepository.deleteFriendRequestsByUserDetailsId(userDetailsId);
        userDetailsRepository.deleteFriendsByUserDetailsId(userDetailsId);
        userDetailsRepository.deleteUserDetailsFromAllParties(userDetailsId);
        userDetailsRepository.deleteAllUserChecksOfUserDetails(userDetailsId);
        userDetailsRepository.save(userDetails);
        userDetailsRepository.delete(userDetails);
        userRepository.deleteById(user.getId());
        return new ResponseEntity<>(
                new UsersDTO.Response.Deleted(true),
                HttpStatus.OK
        );
    }
}
