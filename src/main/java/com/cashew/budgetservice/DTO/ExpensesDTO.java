package com.cashew.budgetservice.DTO;

import com.cashew.budgetservice.DAO.CustomSerializers.ExpensesDTOSerializer;
import com.cashew.budgetservice.DAO.Entities.UserCheck;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
@JsonSerialize(using = ExpensesDTOSerializer.class)
public class ExpensesDTO extends DTO{
    Iterable<UserCheck> expenses;
}
