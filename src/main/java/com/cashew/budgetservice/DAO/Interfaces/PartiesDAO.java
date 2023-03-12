package com.cashew.budgetservice.DAO.Interfaces;

import com.cashew.budgetservice.DAO.Entities.Party;

import java.util.List;
import java.util.Optional;

public interface PartiesDAO {
    Long createParty(String name, Long ownerId);
    Optional<Party> getParty(Long id);
    List<Party> getPartiesOfUser(String username);
    void addUserToParty(Long partyId, String username);
    void removeUserFromParty(Long partyId, String username);
    void deleteParty(Long partyId);
}
