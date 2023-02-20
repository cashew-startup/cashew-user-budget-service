package com.cashew.budgetservice.DAO;

import com.cashew.budgetservice.DAO.Entities.User;

public interface BudgetDAO {
    void createUser(String username, String email);
    Iterable<User> getAllUsers();
}
