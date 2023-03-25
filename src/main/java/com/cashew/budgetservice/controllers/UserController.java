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
    public ResponseEntity<UsersDTO.Response.Found> getUserById(@RequestBody UsersDTO.Request.GetById request){
        return usersService.getUserById(request.getId());
    }

    @GetMapping(path = "/byUsername")
    public ResponseEntity<UsersDTO.Response.Found> getUserByUsername(@RequestBody UsersDTO.Request.GetByUsername request){
        return  usersService.getUserByUsername(request.getUsername());
    }

    @GetMapping("/byEmail")
    public ResponseEntity<UsersDTO.Response.Found> getUserByEmail(@RequestBody UsersDTO.Request.GetByEmail request){
        return  usersService.getUserByEmail(request.getEmail());
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
