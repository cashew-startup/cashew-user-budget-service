package com.cashew.budgetservice.DAO;

import com.cashew.budgetservice.DAO.Interfaces.ExpensesDAO;
import com.cashew.budgetservice.DTO.ExpensesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExpensesDAOImpl implements ExpensesDAO {
    @Autowired
    private com.cashew.budgetservice.DAO.Repos.ReceiptRepository ReceiptRepository;


    @Override
    public ExpensesDTO getExpensesForLastDay() {
        return null;
    }

    @Override
    public ExpensesDTO getExpencesForLastWeek() {
        return null;
    }

    @Override
    public ExpensesDTO getExpensesForLastMonth() {
        return null;
    }

    @Override
    public ExpensesDTO getExpencesForLastYear() {
        return null;
    }

    @Override
    public ExpensesDTO getExpencesForCustomPeriod(LocalDateTime from, LocalDateTime to) {
        return null;
    }
}
