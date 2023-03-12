package com.cashew.budgetservice.DAO.Interfaces;

import com.cashew.budgetservice.DAO.Entities.User;

import java.util.List;

public interface FriendsDAO {
    List<User> getFriends(String username);
    List<User> getFriendRequests(String username);
    void sendRequest(String senderUsername, String recipientUsername);
    void acceptRequest(String senderUsername, String recipientUsername);
    void declineRequest(String senderUsername, String recipientUsername);
    void removeFromFriends(String username1, String username2);
}
