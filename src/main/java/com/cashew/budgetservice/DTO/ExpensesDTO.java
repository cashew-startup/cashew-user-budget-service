package com.cashew.budgetservice.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ExpensesDTO extends DTO{
    List<String> expenses;
}
