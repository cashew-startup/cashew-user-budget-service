package com.cashew.budgetservice.DAO.Interfaces;

import com.cashew.budgetservice.DAO.Entities.User;

public interface FriendsDAO {
    Iterable<User> getFriends(String username);
    Iterable<User> getFriendRequests(String username);
    void sendRequest(String senderUsername, String recipientUsername);
    void acceptRequest(String senderUsername, String recipientUsername);
    void declineRequest(String senderUsername, String recipientUsername);
    void deleteFromFriends(String username1, String username2);
}
