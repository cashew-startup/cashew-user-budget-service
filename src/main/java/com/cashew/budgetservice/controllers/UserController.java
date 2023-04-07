package com.cashew.budgetservice.controllers;

import com.cashew.budgetservice.services.UsersService;
import com.cashew.budgetservice.DTO.UsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UsersService usersService;

    @Autowired
    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    public ResponseEntity<UsersDTO.Response.Created> createNewUser (@RequestBody UsersDTO.Request.Create request) {
        return usersService.createUser(request.getUsername(), request.getEmail());
    }

    @GetMapping
    public ResponseEntity<UsersDTO.Response.Found> getUserById(@RequestParam Long id){
        return usersService.getUserById(id);
    }

    @GetMapping(path = "/byUsername")
    public ResponseEntity<UsersDTO.Response.Found> getUserByUsername(@RequestParam String username){
        return  usersService.getUserByUsername(username);
    }

    @GetMapping("/byEmail")
    public ResponseEntity<UsersDTO.Response.Found> getUserByEmail(@RequestParam String email){
        return  usersService.getUserByEmail(email);
    }

    @PutMapping
    public ResponseEntity<UsersDTO.Response.Updated> updateUser(@RequestBody UsersDTO.Request.UpdateUser request){
        return usersService.updateUser(request.getId(), request.getUsername(), request.getEmail());
    }

    @DeleteMapping
    public ResponseEntity<UsersDTO.Response.Deleted> deleteUserById (@RequestBody UsersDTO.Request.Delete request) {
        return usersService.deleteUserById(request.getId());
    }

}
