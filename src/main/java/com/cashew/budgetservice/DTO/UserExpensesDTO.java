package com.cashew.budgetservice.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserExpensesDTO {

    private String user;
    private List<ProductDTO> products;

}
