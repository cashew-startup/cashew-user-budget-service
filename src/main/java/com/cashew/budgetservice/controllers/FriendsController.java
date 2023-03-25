package com.cashew.budgetservice.controllers;

import com.cashew.budgetservice.DTO.FriendsDTO;
import com.cashew.budgetservice.services.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friends")
public class FriendsController {
    private FriendsService friendsService;

    @Autowired
    public FriendsController(FriendsService friendsService) {
        this.friendsService = friendsService;
    }

    @GetMapping
    public ResponseEntity<FriendsDTO.Response.GetFriends> getFriends(@RequestBody FriendsDTO.Request.Get request){
        return friendsService.getFriends(request.getUsername());
    }

    @GetMapping(path = "/requests")
    public ResponseEntity<FriendsDTO.Response.GetFriendRequests> getFriendRequests(@RequestBody FriendsDTO.Request.Get request){
        return friendsService.getFriendRequests(request.getUsername());
    }

    @PostMapping("/requests/send")
    public ResponseEntity<FriendsDTO.Response.Success> sendRequest(@RequestBody FriendsDTO.Request.FriendRequest request){
        return friendsService.sendRequest(request.getSender(), request.getReceiver());
    }

    @PatchMapping("/requests/accept")
    public ResponseEntity<FriendsDTO.Response.Success> acceptRequest(@RequestBody FriendsDTO.Request.FriendRequest request){
        return friendsService.acceptRequest(request.getSender(), request.getReceiver());
    }

    @PatchMapping("/requests/decline")
    public ResponseEntity<FriendsDTO.Response.Success> declineRequest(@RequestBody FriendsDTO.Request.FriendRequest request){
        return friendsService.declineRequest(request.getSender(), request.getReceiver());
    }

    @DeleteMapping
    public ResponseEntity<FriendsDTO.Response.Success> removeFromFriends(@RequestBody FriendsDTO.Request.RemoveFriend request){
        return friendsService.removeFromFriends(request.getDeleter(), request.getDeleted());
    }
}
