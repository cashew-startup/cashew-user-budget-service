package com.cashew.budgetservice.DAO.Repos;

import com.cashew.budgetservice.DAO.Entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findTopByEmail(String email);
    Optional<User> findTopByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}