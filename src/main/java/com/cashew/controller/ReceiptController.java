package com.cashew.controller;

import com.cashew.dto.ReceiptRequestDTO;
import com.cashew.dto.ReceiptResponseDTO;
import com.cashew.service.ReceiptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ReceiptController {

    private ReceiptService checkService;

    @Autowired
    public ReceiptController(ReceiptService checkService) {
        this.checkService = checkService;
    }

    @PostMapping("/receipt")
    public ResponseEntity<String> getReceipt(@RequestBody ReceiptRequestDTO checkRequest) {
        log.info("receipt {} from {}", checkRequest.getToken(), checkRequest.getUsername());
        return checkService.getReceipt(checkRequest);
    }

}
