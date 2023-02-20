package com.cashew.budgetservice.DAO.Repos;

import com.cashew.budgetservice.DAO.Entities.UserCheck;
import org.springframework.data.repository.CrudRepository;

public interface UserCheckRepository extends CrudRepository<UserCheck,Long> {
}
