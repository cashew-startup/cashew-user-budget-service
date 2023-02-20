package com.cashew.budgetservice.DAO.Repos;

import com.cashew.budgetservice.DAO.Entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
