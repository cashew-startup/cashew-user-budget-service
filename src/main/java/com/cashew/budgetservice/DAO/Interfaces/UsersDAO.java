package com.cashew.budgetservice.DAO.Interfaces;

import com.cashew.budgetservice.DAO.Entities.User;

import java.util.Optional;

public interface UsersDAO {
    Long createUser(String username, String email);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String email);
    Optional<User> updateUser(Long id, String username, String email);
    Iterable<User> getAllUsers();
    void deleteUser(Long id);
}
