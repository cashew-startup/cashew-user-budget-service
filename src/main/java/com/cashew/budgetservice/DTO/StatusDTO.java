package com.cashew.budgetservice.DTO;

import lombok.Data;
import lombok.NonNull;

@Data
public class StatusDTO{
    @NonNull int code;
    @NonNull String description;
}
