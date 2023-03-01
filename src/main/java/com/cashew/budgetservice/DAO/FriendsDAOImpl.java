package com.cashew.budgetservice.DAO;

import com.cashew.budgetservice.DAO.Entities.User;
import com.cashew.budgetservice.DAO.Interfaces.FriendsDAO;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class FriendsDAOImpl implements FriendsDAO {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Iterable<User> getFriends(String username) {
        return userRepository
                .findUserByUsername(username.toLowerCase().trim())
                .orElseThrow()
                .getUserDetails()
                .getFriends();
    }

    @Override
    public Iterable<User> getFriendRequests(String username) {
        return userRepository
                .findUserByUsername(username.toLowerCase().trim())
                .orElseThrow()
                .getUserDetails()
                .getIncomingFriendRequests();
    }

    @Override
    public void sendRequest(String senderUsername, String recipientUsername) {
        User sender = userRepository
                .findUserByUsername(senderUsername.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + senderUsername));
        User recipient = userRepository
                .findUserByUsername(recipientUsername.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + recipientUsername));
        recipient.getUserDetails().getIncomingFriendRequests().add(sender);
        userRepository.save(recipient);
    }

    @Override
    public void acceptRequest(String senderUsername, String recipientUsername) {
        User sender = userRepository
                .findUserByUsername(senderUsername.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + senderUsername));
        User recipient = userRepository
                .findUserByUsername(recipientUsername.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + recipientUsername));
        if (recipient.getUserDetails().getIncomingFriendRequests().remove(sender) == false) {
            throw new NoSuchElementException("Such friend request was not found");
        }
        sender.getUserDetails().getIncomingFriendRequests().remove(recipient);
        sender.getUserDetails().getFriends().add(recipient);
        recipient.getUserDetails().getFriends().add(sender);
        userRepository.save(sender);
        userRepository.save(recipient);
    }

    @Override
    public void declineRequest(String senderUsername, String recipientUsername) {
        User sender = userRepository
                .findUserByUsername(senderUsername.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + senderUsername));
        User recipient = userRepository
                .findUserByUsername(recipientUsername.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + recipientUsername));
        if (recipient.getUserDetails().getIncomingFriendRequests().remove(sender) == false) {
            throw new NoSuchElementException("Such friend request was not found");
        }
        userRepository.save(recipient);
    }

    @Override
    public void deleteFromFriends(String username1, String username2) {
        User user1 = userRepository
                .findUserByUsername(username1.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + username1));
        User user2 = userRepository
                .findUserByUsername(username2.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + username2));
        if (user1.getUserDetails().getFriends().remove(user2) == false ||
            user2.getUserDetails().getFriends().remove(user1) == false) {
            throw new NoSuchElementException("Such friend was not found");
        }
        userRepository.save(user1);
        userRepository.save(user2);
    }
}
