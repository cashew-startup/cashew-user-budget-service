package com.cashew.budgetservice.DAO;

import com.cashew.budgetservice.DAO.Entities.User;
import com.cashew.budgetservice.DAO.Entities.UserDetails;
import com.cashew.budgetservice.DAO.Repos.PartyRepository;
import com.cashew.budgetservice.DAO.Repos.UserDetailsRepository;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BudgetDAOImpl implements BudgetDAO{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private com.cashew.budgetservice.DAO.Repos.ReceiptRepository ReceiptRepository;

    @Override
    public void createUser(String username, String email) {
        User u = new User();
        u.setEmail(email);
        u.setUsername(username);
        u.setDate(LocalDate.now());
        UserDetails ud = new UserDetails();
        userDetailsRepository.save(ud);
        u.setUserDetails(ud);
        userRepository.save(u);
        ud.setUser(u);
        userDetailsRepository.save(ud);
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
