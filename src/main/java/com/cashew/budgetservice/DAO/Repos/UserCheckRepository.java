package com.cashew.budgetservice.DAO.Repos;

import com.cashew.budgetservice.DAO.Entities.UserCheck;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface UserCheckRepository extends CrudRepository<UserCheck,Long> {
    @Query(value = "SELECT uc FROM UserCheck uc " +
            "JOIN uc.receipt r " +
            "where r.date >= ?2 " +
            "and uc.userDetails.id = ?1 " +
            "and uc.isDisabled = false ")
    Iterable<UserCheck> findAllByUserDetailsAndDateAfter(Long userId, LocalDate date);
}
