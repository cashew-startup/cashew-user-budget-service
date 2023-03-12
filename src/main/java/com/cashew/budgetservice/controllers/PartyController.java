package com.cashew.budgetservice.controllers;

import com.cashew.budgetservice.DTO.DTO;
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

    @PostMapping(path="/add")
    private ResponseEntity<DTO> createParty(@RequestBody PartiesDTO.Request.Create request) {
        return partiesService.createParty(request.getName(), request.getOwnerId());
    }

    @GetMapping(path = "/{id}")
    private ResponseEntity<DTO> getUsersOfParty(@PathVariable(value = "id") Long id){
        return partiesService.getUsersOfParty(id);
    }

    @GetMapping(path = "/of")
    private ResponseEntity<DTO> getPartiesOfUser(@RequestBody PartiesDTO.Request.GetPartiesOf request){
        return partiesService.getPartiesOfUser(request.getUsername());
    }

    @GetMapping(path = "/{id}/info")
    private ResponseEntity<DTO> getFullInfoOfParty(@PathVariable(value = "id") Long id){
            return partiesService.getFullInfoOfParty(id);
    }

    @PatchMapping(path = "/addUser")
    private ResponseEntity<DTO> addUserToParty(@RequestBody PartiesDTO.Request.AddUserToParty request) {
        return partiesService.addUserToParty(request.getPartyId(), request.getUsername());
    }

    @PatchMapping(path = "/removeUser")
    private ResponseEntity<DTO> removeUserFromParty(@RequestBody PartiesDTO.Request.RemoveUserFromParty request){
        return partiesService.removeUserFromParty(request.getPartyId(), request.getUsername());
    }

    @DeleteMapping(path = "/{id}")
    private ResponseEntity<DTO> deleteParty(@PathVariable(value = "id") Long partyId){
        return partiesService.deleteParty(partyId);
    }
}
