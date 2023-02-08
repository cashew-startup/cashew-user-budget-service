package com.cashew.budgetservice.accessingDataJPA;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface PartyRepository extends CrudRepository<Party, Long> {
    Optional<Party> findPartyByNameAndCreatorId (String name, Long creatorId);
}