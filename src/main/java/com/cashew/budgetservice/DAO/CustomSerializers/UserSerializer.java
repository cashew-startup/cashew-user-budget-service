package com.cashew.budgetservice.DAO.CustomSerializers;


import com.cashew.budgetservice.DAO.Entities.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class UserSerializer extends StdSerializer<User> {

    public UserSerializer() {
        this(null);
    }

    public UserSerializer(Class<User> t) {
        super(t);
    }


    @Override
    public void serialize(
            User user,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException, JsonProcessingException {
        generator.writeStartObject();
        generator.writeNumberField("id", user.getId());
        generator.writeStringField("username", user.getUsername());
        generator.writeStringField("email", user.getEmail());
        generator.writeStringField("date", user.getDate().toString());
        generator.writeEndObject();
    }

}