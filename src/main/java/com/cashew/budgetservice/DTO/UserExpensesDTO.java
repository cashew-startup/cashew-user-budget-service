package com.cashew.budgetservice.DTO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserExpensesDTO {
    private String username;
    private List<ProductShareDTO> expenses;
}
