package com.cashew.budgetservice.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DTO {
    private final ObjectMapper mapper = new ObjectMapper();

    static {
        try {
            empty = new ObjectMapper().writeValueAsString("");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public String toJson() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.info("JsonProcessingException");
            return "";
        }
    }

    private static String empty;

    public static String empty(){
        return empty;
    }


}
