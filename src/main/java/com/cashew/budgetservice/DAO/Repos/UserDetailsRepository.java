package com.cashew.budgetservice.DAO.Repos;

import com.cashew.budgetservice.DAO.Entities.UserDetails;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserDetailsRepository extends CrudRepository<UserDetails, Long> {
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM user_details_incoming_friend_requests " +
            "where user_details_id = ?1 " +
            "or incoming_friend_request_user_id = ?1")
    void deleteFriendRequestsByUserDetailsId(Long id);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM user_details_friends " +
            "where user_details_id = ?1 " +
            "or friend_user_id = ?1")
    void deleteFriendsByUserDetailsId(Long id);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM user_details_parties " +
            "where user_details_id = ?1 ")
    void deleteUserDetailsFromAllParties(Long id);
}
