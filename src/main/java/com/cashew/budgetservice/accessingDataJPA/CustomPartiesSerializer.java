package com.cashew.budgetservice.accessingDataJPA;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.List;

public class CustomPartiesSerializer extends StdSerializer<List<Party>> {

    public CustomPartiesSerializer() {
        this(null);
    }

    public CustomPartiesSerializer(Class<List<Party>> t) {
        super(t);
    }


    @Override
    public void serialize(
            List<Party> parties,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException, JsonProcessingException {

        generator.writeStartArray();
        for (Party party : parties) {
            generator.writeStartObject();
            generator.writeNumberField("id", party.getId());
            generator.writeStringField("nickname", party.getName());
            generator.writeEndObject();
        }
        generator.writeEndArray();
    }

}