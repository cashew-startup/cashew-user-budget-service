package com.cashew.budgetservice.DAO.Repos;

import com.cashew.budgetservice.DAO.Entities.UserCheck;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserCheckRepository extends CrudRepository<UserCheck,Long> {
    @Query(value = "SELECT uc FROM UserCheck uc " +
            "JOIN uc.receipt r " +
            "where r.date >= ?2 " +
            "and uc.userDetails.id = ?1 " +
            "and uc.isDisabled = false ")
    List<UserCheck> findAllByUserDetailsAndDateAfter(Long userId, LocalDateTime date);

    @Query(value = "SELECT uc FROM UserCheck uc " +
            "JOIN uc.receipt r " +
            "where r.date between ?2 and ?3 " +
            "and uc.userDetails.id = ?1 " +
            "and uc.isDisabled = false ")
    List<UserCheck> findAllByUserDetailsAndDateIn(Long userId, LocalDateTime from, LocalDateTime to);


}
