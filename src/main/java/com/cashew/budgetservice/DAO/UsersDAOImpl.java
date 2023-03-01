package com.cashew.budgetservice.DAO;

import com.cashew.budgetservice.DAO.Entities.User;
import com.cashew.budgetservice.DAO.Entities.UserDetails;
import com.cashew.budgetservice.DAO.Interfaces.UsersDAO;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class UsersDAOImpl implements UsersDAO {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Long createUser(String username, String email) {
        username = username.toLowerCase().trim();
        email = email.toLowerCase().trim();
        if (userRepository.existsByUsername(username)){
            throw new IllegalArgumentException("Not unique username");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Not unique email");
        }
        User u = new User();
        u.setEmail(email);
        u.setUsername(username);
        u.setDate(LocalDateTime.now());
        UserDetails ud = new UserDetails();
        ud.setUser(u);
        u.setUserDetails(ud);
        userRepository.save(u);
        return u.getId();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        username = username.toLowerCase().trim();
        return userRepository.findUserByUsername(username);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        email = email.toLowerCase().trim();
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Optional<User> updateUser(Long id, String username, String email) {
        username = username.toLowerCase().trim();
        email = email.toLowerCase().trim();
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            username = username.toLowerCase();
            email = email.toLowerCase();
            User u = user.get();
            u.setUsername(username);
            u.setEmail(email);
            userRepository.save(u);
        }
        return user;
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else throw new IllegalArgumentException("User with such id not found");
    }
}
