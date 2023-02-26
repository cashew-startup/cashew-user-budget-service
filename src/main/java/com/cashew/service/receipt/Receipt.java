package com.cashew.service.receipt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receipt {

    private String company;
    private String address;
    private String INN;
    private String date;
    private String receiptNumber;
    private String shift;
    private String cashier;
    private List<Product> products = new ArrayList<>();
    private BigDecimal total;
    private BigDecimal cash;
    private BigDecimal card;
    private BigDecimal VAT20;
    private BigDecimal VAT10;
    private String taxation;


    @Override
    public String toString() {
        return "Receipt{" + "\n" +
                "company='" + company + '\'' + "\n" +
                ", address='" + address + '\'' + "\n" +
                ", INN='" + INN + '\'' + "\n" +
                ", date='" + date + '\'' + "\n" +
                ", receiptNumber='" + receiptNumber + '\'' + "\n" +
                ", shift='" + shift + '\'' + "\n" +
                ", cashier='" + cashier + '\'' + "\n" +
                ", products=" + products + "\n" +
                ", total=" + total + "\n" +
                ", cash=" + cash + "\n" +
                ", card=" + card + "\n" +
                ", VAT20=" + VAT20 + "\n" +
                ", VAT10=" + VAT10 + "\n" +
                ", taxation='" + taxation + '\'' + "\n" +
                '}';
    }
}
