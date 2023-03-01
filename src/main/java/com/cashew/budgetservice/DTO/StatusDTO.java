package com.cashew.budgetservice.DTO;

import lombok.Data;
import lombok.NonNull;

@Data
public class StatusDTO extends DTO{
    @NonNull int code;
    @NonNull String description;
}
