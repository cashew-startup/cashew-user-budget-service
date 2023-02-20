package com.cashew.budgetservice.DAO.Repos;

import com.cashew.budgetservice.DAO.Entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}