package com.cashew.service.receipt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private String id;

    private String name;
    private BigDecimal price;
    private String count;
    private BigDecimal sumPrice;

    @Override
    public String toString() {
        return "    \nProduct{" + "\n" +
                "       id='" + id + '\'' + "\n" +
                "       , name='" + name + '\'' + "\n" +
                "       , price=" + price + "\n" +
                "       , count='" + count + '\'' + "\n" +
                "       , sumPrice=" + sumPrice + "\n" +
                '}';
    }
}
