package com.cashew.budgetservice.controllers;

import com.cashew.budgetservice.DAO.BudgetDAO;
import com.cashew.budgetservice.DAO.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/budget")
public class BudgetController {
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private UserDetailsRepository userDetailsRepository;
//    @Autowired
//    private PartyRepository partyRepository;
//    @Autowired
//    private ReceiptRepository ReceiptRepository;

    @Autowired
    private BudgetDAO dao;


    @PostMapping(path="/users/add")
    public @ResponseBody ResponseEntity<String> createNewUser (
            @RequestParam String username,
            @RequestParam String email) {
        try{
            dao.createUser(username,email);
            return new ResponseEntity<>("User created",HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error:"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(path="/users/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return dao.getAllUsers();
    }

    @GetMapping(path = "/users/{id}")
    public @ResponseBody
    ResponseEntity<User> getUserById(@PathVariable(value = "id") Long id){
//        try {
//            User user = userRepository.findById(id).orElseThrow();
//            return new ResponseEntity<>(user, HttpStatus.OK);
//        } catch (NoSuchElementException e)
//        {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
        return new ResponseEntity<>(new User(),HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping(path = "/users/byemail")
    public @ResponseBody ResponseEntity<User> getUserByEmail(@RequestParam String email){
        return new ResponseEntity<>(new User(),HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping(path="/users/{Id}")
    public @ResponseBody ResponseEntity<?> deleteUserById (@PathVariable(value = "Id") Long userId) {

        return new ResponseEntity<>("Not implemented yet",HttpStatus.NOT_IMPLEMENTED);
    }
}
