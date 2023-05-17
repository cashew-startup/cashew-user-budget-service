package com.cashew.budgetservice.DAO.Repos;

import com.cashew.budgetservice.DAO.Entities.UserCheck;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface UserCheckRepository extends CrudRepository<UserCheck,Long> {

    @Query(value = "SELECT uc FROM UserCheck uc " +
            "JOIN uc.receipt r " +
            "where uc.userDetails.id = ?1 " +
            "and uc.isDisabled = ?2 ")
    List<UserCheck> findAllByUserDetailsIdAndIsDisabled(Long UserDetailsId, boolean isDisabled);

    @Query(value = "SELECT uc FROM UserCheck uc " +
            "JOIN uc.receipt r " +
            "where uc.userDetails.id = ?1 " +
            "and uc.receipt.id = ?2 ")
    Optional<UserCheck> findByUserDetailsIdAndReceiptId(Long UserDetailsId, Long receiptId);

    @Query(value = "SELECT uc FROM UserCheck uc " +
            "JOIN uc.receipt r " +
            "where r.date >= ?2 " +
            "and uc.userDetails.id = ?1 " +
            "and uc.isDisabled = false ")
    List<UserCheck> findAllByUserDetailsIdAndDateAfter(Long userDetailsId, ZonedDateTime from);

    @Query(value = "SELECT uc FROM UserCheck uc " +
            "JOIN uc.receipt r " +
            "where r.date >= ?2 and r.date <= ?3 " +
            "and uc.userDetails.id = ?1 " +
            "and uc.isDisabled = false ")
    List<UserCheck> findAllByUserDetailsIdAndDateIn(Long userDetailsId, ZonedDateTime from, ZonedDateTime to);

    List<UserCheck> findAllByUserDetailsId(long userDetailsId);
}
