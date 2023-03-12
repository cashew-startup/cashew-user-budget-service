package com.cashew.budgetservice.DAO;

import com.cashew.budgetservice.DAO.Entities.Party;
import com.cashew.budgetservice.DAO.Entities.User;
import com.cashew.budgetservice.DAO.Interfaces.PartiesDAO;
import com.cashew.budgetservice.DAO.Repos.PartyRepository;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class PartiesDAOImpl implements PartiesDAO {
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Long createParty(String name, Long ownerId) {
        name = name.toLowerCase();
        User u = userRepository.findById(ownerId).orElseThrow();
        Party p = new Party();
        p.setName(name);
        p.setDate(LocalDateTime.now());
        p.setOwnerId(ownerId);
        p.setListOfUserDetails(new ArrayList<>());
        p.getListOfUserDetails().add(u.getUserDetails());
        partyRepository.save(p);
        u.getUserDetails()
                .getParties()
                .add(p);
        userRepository.save(u);
        return p.getId();
    }

    @Override
    public Optional<Party> getParty(Long id) {
        return partyRepository.findById(id);
    }

    @Override
    public List<Party> getPartiesOfUser(String username){
        return userRepository
                .findUserByUsername(username)
                .orElseThrow()
                .getUserDetails()
                .getParties();
    }

    @Override
    public void addUserToParty(Long partyId, String username) {
        User u = userRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("No user with such username"));
        Party p = partyRepository
                .findById(partyId)
                .orElseThrow(() -> new NoSuchElementException("No party with such id"));
        if (u.getUserDetails().getParties().contains(p)
            ||
            p.getListOfUserDetails().contains(u.getUserDetails())) {
            throw new IllegalArgumentException("User is already in the party");
        } else {
            u.getUserDetails().getParties().add(p);
            p.getListOfUserDetails().add(u.getUserDetails());
        }
        userRepository.save(u);
        partyRepository.save(p);
    }

    @Override
    public void removeUserFromParty(Long partyId, String username) {
        Party p = partyRepository
                .findById(partyId)
                .orElseThrow(()->new IllegalArgumentException("No party with such id"));
        User u = userRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("No user with such id"));
        if (username.equals(userRepository.findById(p.getOwnerId()).get().getUsername())){
            throw new IllegalArgumentException("Can't remove owner from party");
        }
        p.getListOfUserDetails().remove(u.getUserDetails());
        partyRepository.save(p);
    }


    @Override
    public void deleteParty(Long partyId) {
        Party p = partyRepository
                .findById(partyId)
                .orElseThrow(() -> new IllegalArgumentException("No party with such id"));
        p.getListOfUserDetails().forEach((ud) -> ud.getParties().remove(p));
        partyRepository.deleteById(partyId);
    }
}
