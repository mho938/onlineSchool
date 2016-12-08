package com.iust.onlineschool.utility.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by mohsen.oloumi on 10/03/2016.
 */
public class PasswordSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator generator,
                          SerializerProvider provider)
            throws IOException, JsonProcessingException {

        generator.writeObject(" ");
    }
}
