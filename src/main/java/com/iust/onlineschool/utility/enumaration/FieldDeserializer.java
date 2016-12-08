package com.iust.onlineschool.utility.enumaration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.iust.onlineschool.enumaration.Field;

import java.io.IOException;


public class FieldDeserializer extends JsonDeserializer<Field> {

    @Override
    public Field deserialize(final JsonParser parser, final DeserializationContext context)
            throws IOException, JsonProcessingException
    {
        return Field.values()[parser.getIntValue()];
    }
}
