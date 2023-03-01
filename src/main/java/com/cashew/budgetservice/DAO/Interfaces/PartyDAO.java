package com.cashew.budgetservice.DAO.Interfaces;

import com.cashew.budgetservice.DAO.Entities.Party;

import java.util.Optional;

public interface PartyDAO {
    Long createParty(String name, Long ownerId);
    Optional<Party> getParty(Long id);
    Iterable<Party> getPartiesOfUser(Long userId);
    Iterable<Party> getAllParties();
    void addUserToParty(Long partyId, Long userId);
    void removeUserFromParty(Long partyId, Long userId);
    void deleteParty(Long partyId);
}
