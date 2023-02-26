package com.cashew.service.receipt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParseReceiptService {

    private ReceiptApi api;

    @Autowired
    public ParseReceiptService(ReceiptApi api) {
        this.api = api;
    }

    public Receipt parseToken(String token) {
        Receipt receipt = null;
        int count = 0;
        while (true) {
            if (count == 30) { return null; }
            receipt = api.receiptByToken(token);
            if (receipt != null) { return receipt; }
            count++;
        }
    }

}
