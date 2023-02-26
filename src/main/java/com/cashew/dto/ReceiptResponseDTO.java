package com.cashew.dto;

import com.cashew.service.receipt.Receipt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptResponseDTO extends DTO {

    private String username;
    private String date;
    private Receipt receipt;

}
