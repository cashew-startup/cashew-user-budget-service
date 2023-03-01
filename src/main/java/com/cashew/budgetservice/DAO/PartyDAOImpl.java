package com.cashew.budgetservice.DAO;

import com.cashew.budgetservice.DAO.Entities.Party;
import com.cashew.budgetservice.DAO.Entities.User;
import com.cashew.budgetservice.DAO.Interfaces.PartyDAO;
import com.cashew.budgetservice.DAO.Repos.PartyRepository;
import com.cashew.budgetservice.DAO.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class PartyDAOImpl implements PartyDAO {
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
        p.setSetOfUserDetails(new HashSet<>());
        p.getSetOfUserDetails().add(u.getUserDetails());
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
    public Iterable<Party> getPartiesOfUser(Long userId){
        return userRepository
                .findById(userId)
                .orElseThrow()
                .getUserDetails()
                .getParties();
    }

    @Override
    public Iterable<Party> getAllParties() {
        return partyRepository.findAll();
    }

    @Override
    public void addUserToParty(Long partyId, Long userId) {
        User u = userRepository
                .findById(userId)
                .orElseThrow(() -> new NoSuchElementException("No user with such id"));
        Party p = partyRepository
                .findById(partyId)
                .orElseThrow(() -> new NoSuchElementException("No party with such id"));
        if (u.getUserDetails().getParties().add(p) == false
            ||
            p.getSetOfUserDetails().add(u.getUserDetails()) == false) {
            throw new IllegalArgumentException("User is already in the party");
        }
        userRepository.save(u);
        partyRepository.save(p);
    }

    @Override
    public void removeUserFromParty(Long partyId, Long userId) {
        Party p = partyRepository
                .findById(partyId)
                .orElseThrow(()->new IllegalArgumentException("No party with such id"));
        User u = userRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user with such id"));
        if (userId.equals(p.getOwnerId())){
            throw new IllegalArgumentException("Can't remove owner from party");
        }
        p.getSetOfUserDetails().remove(u.getUserDetails());
        partyRepository.save(p);
    }


    @Override
    public void deleteParty(Long partyId) {
        Party p = partyRepository
                .findById(partyId)
                .orElseThrow(() -> new IllegalArgumentException("No party with such id"));
        p.getSetOfUserDetails().forEach((ud) -> ud.getParties().remove(p));
        partyRepository.deleteById(partyId);
    }
}
