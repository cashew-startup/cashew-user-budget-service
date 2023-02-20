package com.cashew.budgetservice.DAO.CustomSerializers;


import com.cashew.budgetservice.DAO.Entities.Party;
import com.cashew.budgetservice.DAO.Entities.UserDetails;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class UserDetailsSerializer extends StdSerializer<UserDetails> {

    public UserDetailsSerializer() {
        this(null);
    }

    public UserDetailsSerializer(Class<UserDetails> t) {
        super(t);
    }


    @Override
    public void serialize(
            UserDetails userDetails,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException, JsonProcessingException {
        generator.writeStartArray();
        for (Party party : userDetails.getParties()) {
            generator.writeStartObject();
            generator.writeNumberField("id", party.getId());
            generator.writeStringField("", party.getName());
            generator.writeEndObject();
        }
        generator.writeEndArray();
    }

}