package com.cashew.budgetservice.DAO.CustomSerializers;

import com.cashew.budgetservice.DAO.Entities.UserCheck;
import com.cashew.budgetservice.DTO.ExpensesDTO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ExpensesDTOSerializer extends StdSerializer<ExpensesDTO> {
    public ExpensesDTOSerializer() {
        this(null);
    }

    public ExpensesDTOSerializer(Class<ExpensesDTO> t) {
        super(t);
    }

    @Override
    public void serialize(ExpensesDTO dto, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeStartObject();
        generator.writeArrayFieldStart("expenses");
        for (UserCheck check : dto.getExpenses()){
            generator.writeStartObject();
            generator.writeNumberField("receiptId", check.getReceipt().getId());
            generator.writeStringField("check info", "to be implemented");
            generator.writeEndObject();
        }
        generator.writeEndArray();
        generator.writeEndObject();
    }
}
