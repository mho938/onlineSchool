package com.iust.onlineschool.utility.enumaration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.iust.onlineschool.enumaration.Grade;

import java.io.IOException;


public class GradeDeserializer extends JsonDeserializer<Grade> {

    @Override
    public Grade deserialize(final JsonParser parser, final DeserializationContext context)
            throws IOException, JsonProcessingException
    {
        return Grade.values()[parser.getIntValue()];
    }
}
