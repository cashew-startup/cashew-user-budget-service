package com.cashew.service;

import com.cashew.dto.ReceiptRequestDTO;
import com.cashew.dto.ReceiptResponseDTO;
import com.cashew.dto.StatusDTO;
import com.cashew.entity.ReceiptHistory;
import com.cashew.repository.ReceiptHistoryRepository;
import com.cashew.service.receipt.ParseReceiptService;
import com.cashew.service.receipt.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ReceiptService {

    private ParseReceiptService parseReceiptService;
    private ReceiptHistoryRepository historyRepository;

    @Autowired
    public ReceiptService(ParseReceiptService parseReceiptService, ReceiptHistoryRepository historyRepository) {
        this.parseReceiptService = parseReceiptService;
        this.historyRepository = historyRepository;
    }

    public ResponseEntity<String> getReceipt(ReceiptRequestDTO receiptRequest) {
        Receipt receipt = parseReceiptService.parseToken(receiptRequest.getToken());
        if (receipt == null) return new ResponseEntity<>(new StatusDTO(204, "receipt is empty, try again").toJson(), HttpStatus.NO_CONTENT);

        Date date = new Date();
        ReceiptResponseDTO response = new ReceiptResponseDTO();
        response.setUsername(receiptRequest.getUsername());
        response.setDate(getDate(date));
        response.setReceipt(receipt);

//        ReceiptHistory receiptHistory = new ReceiptHistory();
//        receiptHistory.setDate(date);
//        receiptHistory.setUsername(receiptHistory.getUsername());
//        receiptHistory.setReceipt();
//        receiptHistory.setReceipt(ReceiptMapper.INSTANCE.receiptToReceiptEntity(receipt));
//
//        historyRepository.save(receiptHistory);

        return ResponseEntity.ok(response.toJson());
    }


    private String getDate(Date date) {
        String pattern = "dd-M-yyyy hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

}
