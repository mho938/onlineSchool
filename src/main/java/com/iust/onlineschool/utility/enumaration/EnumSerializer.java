package com.iust.onlineschool.utility.enumaration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author mojtaba khallash
 */
public class EnumSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator generator,
                          SerializerProvider provider)
            throws  IOException, JsonProcessingException {

        generator.writeNumber(((Enum)value).ordinal());
    }
}
