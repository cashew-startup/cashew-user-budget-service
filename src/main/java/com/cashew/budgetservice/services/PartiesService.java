package com.cashew.budgetservice.services;

import com.cashew.budgetservice.DAO.Entities.Party;
import com.cashew.budgetservice.DAO.Entities.UserDetails;
import com.cashew.budgetservice.DAO.Interfaces.PartiesDAO;
import com.cashew.budgetservice.DTO.DTO;
import com.cashew.budgetservice.DTO.PartiesDTO;
import com.cashew.budgetservice.DTO.StatusDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PartiesService {
    private PartiesDAO dao;

    @Autowired
    public PartiesService(PartiesDAO dao) {
        this.dao = dao;
    }

    public ResponseEntity<DTO> createParty(String name, Long ownerId){
        try {
            Long partyId = dao.createParty(name, ownerId);
            return new ResponseEntity<>(new PartiesDTO.Response.Created(partyId), HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new StatusDTO(404, "No user with id = ownerId"), HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<DTO> getUsersOfParty(Long id) {
        Optional<Party> party = dao.getParty(id);
        if (dao.getParty(id).isPresent()) {
            List<UserDetails> userDetails = dao.getParty(id).get().getListOfUserDetails();
            List<String> userNames = new ArrayList<>();
            for (UserDetails ud : userDetails) {
                userNames.add(ud.getUser().getUsername());
            }
            return new ResponseEntity<>(new PartiesDTO.Response.UsersList(userNames), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new StatusDTO(400, "No party with such id"), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<DTO> getFullInfoOfParty(Long id) {
        try{
            Party party = dao.getParty(id).orElseThrow();
            return new ResponseEntity(new PartiesDTO.Response.FullInfo(party), HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(new StatusDTO(400, "No party with such id"), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<DTO> getPartiesOfUser(String username) {
        try {
            return new ResponseEntity<>(new PartiesDTO.Response.PartiesList(dao.getPartiesOfUser(username)), HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(new StatusDTO(404, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<DTO> deleteParty(Long partyId) {
        try {
            dao.deleteParty(partyId);
            return new ResponseEntity<>(new PartiesDTO.Response.Success(true), HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<> (
                    new StatusDTO(404,"No party with such id"), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<DTO> addUserToParty(Long partyId, String username) {
        try {
            dao.addUserToParty(partyId, username);
            return new ResponseEntity<>(new PartiesDTO.Response.Success(true), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(new StatusDTO(404, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new StatusDTO(409, e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<DTO> removeUserFromParty(Long partyId, String username) {
        try {
            dao.removeUserFromParty(partyId, username);
            return new ResponseEntity<>(new PartiesDTO.Response.Success(true), HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(new StatusDTO(404, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new StatusDTO(409, e.getMessage()), HttpStatus.CONFLICT);
        }
    }
}
