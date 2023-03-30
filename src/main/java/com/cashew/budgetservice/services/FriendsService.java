package com.cashew.budgetservice.services;

import com.cashew.budgetservice.DAO.Entities.User;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import com.cashew.budgetservice.DTO.FriendsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class FriendsService{
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<FriendsDTO.Response.GetFriends> getFriends(String username) {
        List<User> friends = userRepository
                .findTopByUsername(username.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("User not found"))
                .getUserDetails()
                .getFriends();
        List<String> usernames = new ArrayList<>();
        friends.forEach((user)-> usernames.add(user.getUsername()));
        return new ResponseEntity<>(new FriendsDTO.Response.GetFriends(usernames), HttpStatus.OK);
    }

    public ResponseEntity<FriendsDTO.Response.GetFriendRequests> getFriendRequests(String username) {
        List<User> friendRequests = userRepository
                .findTopByUsername(username.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("User not found"))
                .getUserDetails()
                .getIncomingFriendRequests();
        List<String> usernames = new ArrayList<>();
        friendRequests.forEach((user)-> usernames.add(user.getUsername()));
        return new ResponseEntity<>(new FriendsDTO.Response.GetFriendRequests(usernames), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<FriendsDTO.Response.Success> sendRequest(String senderUsername, String receiverUsername) {
        User sender = userRepository
                .findTopByUsername(senderUsername.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + senderUsername));
        User receiver = userRepository
                .findTopByUsername(receiverUsername.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + receiverUsername));
        receiver.getUserDetails().getIncomingFriendRequests().add(sender);
        userRepository.save(receiver);
        return new ResponseEntity<>(new FriendsDTO.Response.Success(true),HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<FriendsDTO.Response.Success> acceptRequest(String senderUsername, String receiverUsername) {
        User sender = userRepository
                .findTopByUsername(senderUsername.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + senderUsername));
        User recipient = userRepository
                .findTopByUsername(receiverUsername.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + receiverUsername));
        if (!recipient.getUserDetails().getIncomingFriendRequests().remove(sender)) {
            throw new NoSuchElementException("Such friend request was not found");
        }
        sender.getUserDetails().getIncomingFriendRequests().remove(recipient);
        sender.getUserDetails().getFriends().add(recipient);
        recipient.getUserDetails().getFriends().add(sender);
        userRepository.save(sender);
        userRepository.save(recipient);
        return new ResponseEntity<>(new FriendsDTO.Response.Success(true),HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<FriendsDTO.Response.Success> declineRequest(String senderUsername, String receiverUsername) {
        User sender = userRepository
                .findTopByUsername(senderUsername.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + senderUsername));
        User recipient = userRepository
                .findTopByUsername(receiverUsername.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + receiverUsername));
        if (!recipient.getUserDetails().getIncomingFriendRequests().remove(sender)) {
            throw new NoSuchElementException("Such friend request was not found");
        }
        userRepository.save(recipient);
        return new ResponseEntity<>(new FriendsDTO.Response.Success(true), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<FriendsDTO.Response.Success> removeFromFriends(String username1, String username2) {
        User user1 = userRepository
                .findTopByUsername(username1.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + username1));
        User user2 = userRepository
                .findTopByUsername(username2.toLowerCase().trim())
                .orElseThrow(() -> new NoSuchElementException("No user with username " + username2));
        if (!user1.getUserDetails().getFriends().remove(user2) ||
                !user2.getUserDetails().getFriends().remove(user1)) {
            throw new NoSuchElementException("Such friend pair was not found");
        }
        userRepository.save(user1);
        userRepository.save(user2);
        return new ResponseEntity<>(new FriendsDTO.Response.Success(true), HttpStatus.OK);
    }
}
