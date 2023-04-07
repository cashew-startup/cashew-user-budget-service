package com.cashew.budgetservice.controllers;

import com.cashew.budgetservice.DTO.PartiesDTO;
import com.cashew.budgetservice.services.PartiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/parties")
public class PartyController {
    private PartiesService partiesService;

    @Autowired
    public PartyController(PartiesService partiesService) {
        this.partiesService = partiesService;
    }

    @PostMapping
    private ResponseEntity<PartiesDTO.Response.Created> createParty(@RequestBody PartiesDTO.Request.Create request) {
        return partiesService.createParty(request.getName(), request.getOwnerId());
    }

    @GetMapping(path = "/{id}")
    private ResponseEntity<PartiesDTO.Response.UsersList> getUsersOfParty(@PathVariable(value = "id") Long id){
        return partiesService.getUsersOfParty(id);
    }

    @GetMapping(path = "/of")
    private ResponseEntity<PartiesDTO.Response.PartiesList> getPartiesOfUser(@RequestParam String username){
        return partiesService.getPartiesOfUser(username);
    }

    @GetMapping(path = "/{id}/info")
    private ResponseEntity<PartiesDTO.Response.FullInfo> getFullInfoAboutParty(@PathVariable(value = "id") Long id){
            return partiesService.getFullInfoOfParty(id);
    }

    @PatchMapping(path = "/users")
    private ResponseEntity<PartiesDTO.Response.Success> addUserToParty(@RequestBody PartiesDTO.Request.AddUserToParty request) {
        return partiesService.addUserToParty(request.getPartyId(), request.getUsername());
    }

    @DeleteMapping(path = "/users")
    private ResponseEntity<PartiesDTO.Response.Success> removeUserFromParty(@RequestBody PartiesDTO.Request.RemoveUserFromParty request){
        return partiesService.removeUserFromParty(request.getPartyId(), request.getUsername());
    }

    @DeleteMapping(path = "/{id}")
    private ResponseEntity<PartiesDTO.Response.Success> deleteParty(@PathVariable(value = "id") Long partyId){
        return partiesService.deleteParty(partyId);
    }

    @PatchMapping
    private ResponseEntity<PartiesDTO.Response.Success> changePartyName(@RequestBody PartiesDTO.Request.ChangeName request){
        return partiesService.changeName(request.getPartyId(), request.getName());
    }

}
