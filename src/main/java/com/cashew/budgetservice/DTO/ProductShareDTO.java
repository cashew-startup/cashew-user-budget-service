package com.cashew.budgetservice.DTO;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProductShareDTO {
    private String name;
    private double price;
}
