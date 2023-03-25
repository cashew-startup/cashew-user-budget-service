package com.cashew.budgetservice.DAO.Repos;

import com.cashew.budgetservice.DAO.Entities.Party;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PartyRepository extends CrudRepository<Party, Long> {
    void removePartyById(Long id);
    Optional<Party> findPartyById(Long id);
}