package com.cashew.budgetservice;

import com.cashew.budgetservice.DTO.StatusDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<StatusDTO> handle(NoSuchElementException ex){
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new StatusDTO(404, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<StatusDTO> handle(IllegalArgumentException ex){
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(new StatusDTO(409, ex.getMessage()), HttpStatus.CONFLICT);
    }
}
