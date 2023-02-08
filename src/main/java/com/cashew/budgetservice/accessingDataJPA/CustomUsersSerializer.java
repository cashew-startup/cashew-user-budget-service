package com.cashew.budgetservice.accessingDataJPA;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.List;

public class CustomUsersSerializer extends StdSerializer<List<User>> {

    public CustomUsersSerializer() {
        this(null);
    }

    public CustomUsersSerializer(Class<List<User>> t) {
        super(t);
    }


    @Override
    public void serialize(
            List<User> users,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException, JsonProcessingException {

        generator.writeStartArray();
        for (User user : users) {
            generator.writeStartObject();
            generator.writeNumberField("id", user.getId());
            generator.writeStringField("email", user.getEmail());
            generator.writeEndObject();
        }
        generator.writeEndArray();
    }

}