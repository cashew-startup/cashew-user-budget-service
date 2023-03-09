package com.cashew.budgetservice.DTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DTO {
    public static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
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
            log.info("JsonProcessingException:"+e.getMessage());
            return "";
        }
    }

    private static String empty;

    public static String empty(){
        return empty;
    }


}
