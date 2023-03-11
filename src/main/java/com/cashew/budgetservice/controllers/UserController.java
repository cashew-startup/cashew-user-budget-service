package com.cashew.budgetservice.controllers;

import com.cashew.budgetservice.DAO.Interfaces.UsersDAO;
import com.cashew.budgetservice.DTO.DTO;
import com.cashew.budgetservice.DTO.UsersDTO;
import com.cashew.budgetservice.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UsersService usersService;
    @Autowired
    private UsersDAO dao;

    @Autowired
    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping(path="/add")
    public ResponseEntity<DTO> createNewUser (@RequestBody UsersDTO.Request.Create request) {
        return usersService.createUser(request.getUsername(), request.getEmail());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DTO> getUserById(@PathVariable(value = "id") Long id){
        return usersService.getUserById(id);
    }

    @GetMapping(path = "/byUsername")
    public ResponseEntity<?> getUserByUsername(@RequestBody UsersDTO.Request.GetByUsername request){
        return  usersService.getUserByUsername(request.getUsername());
    }

    @GetMapping("/byEmail")
    public ResponseEntity<DTO> getUserByEmail(@RequestBody UsersDTO.Request.GetByEmail request){
        return  usersService.getUserByEmail(request.getEmail());
    }

    @PutMapping(path="/{id}")
    public ResponseEntity<DTO> updateUser(
                        @PathVariable(value="id") Long id,
                        @RequestBody UsersDTO.Request.UpdateUser request){
        return usersService.updateUser(request);
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<DTO> deleteUserById (@PathVariable(value = "id") Long id) {
        return usersService.deleteUserById(id);
    }

}
