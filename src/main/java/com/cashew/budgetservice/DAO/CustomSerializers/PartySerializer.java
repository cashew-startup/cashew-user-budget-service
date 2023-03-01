package com.cashew.budgetservice.DAO.CustomSerializers;

import com.cashew.budgetservice.DAO.Entities.Party;
import com.cashew.budgetservice.DAO.Entities.UserDetails;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class PartySerializer extends StdSerializer<Party> {
    public PartySerializer(){
        this(null);
    }

    public PartySerializer(Class<Party> t) {
        super(t);
    }

    @Override
    public void serialize(
            Party party,
            JsonGenerator generator,
            SerializerProvider serializerProvider)
            throws IOException {
        generator.writeStartObject();
        generator.writeNumberField("id", party.getId());
        generator.writeStringField("name", party.getName());
        generator.writeStringField("owner id",party.getOwnerId().toString());
        generator.writeStringField("date", party.getDate().toString());
        generator.writeArrayFieldStart("users");
        for (UserDetails ud : party.getSetOfUserDetails()){
            generator.writeString(ud.getUser().getUsername()+"@"+ud.getUser().getId().toString());
        }
        generator.writeEndArray();
        generator.writeEndObject();

    }
}