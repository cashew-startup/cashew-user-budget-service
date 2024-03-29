package com.cashew.budgetservice.DAO.Repos;

import com.cashew.budgetservice.DAO.Entities.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Optional<Product> findByName(String name);
}
