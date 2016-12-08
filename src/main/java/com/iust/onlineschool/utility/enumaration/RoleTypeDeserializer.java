package com.iust.onlineschool.utility.enumaration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.iust.onlineschool.enumaration.RoleType;

import java.io.IOException;


public class RoleTypeDeserializer extends JsonDeserializer<RoleType> {

    @Override
    public RoleType deserialize(final JsonParser parser, final DeserializationContext context)
            throws IOException, JsonProcessingException
    {
        return RoleType.values()[parser.getIntValue()];
    }
}
