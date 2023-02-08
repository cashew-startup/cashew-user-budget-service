package com.cashew.budgetservice.accessingDataJPA;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}