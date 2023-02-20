package com.cashew.budgetservice.DAO.Repos;

import com.cashew.budgetservice.DAO.Entities.Party;
import org.springframework.data.repository.CrudRepository;

public interface PartyRepository extends CrudRepository<Party, Long> {
}