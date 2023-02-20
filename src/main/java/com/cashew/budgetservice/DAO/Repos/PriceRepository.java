package com.cashew.budgetservice.DAO.Repos;

import com.cashew.budgetservice.DAO.Entities.Price;
import org.springframework.data.repository.CrudRepository;

public interface PriceRepository extends CrudRepository<Price, Long> {
}
