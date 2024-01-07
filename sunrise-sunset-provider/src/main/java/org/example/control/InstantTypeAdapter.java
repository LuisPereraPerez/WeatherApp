package org.example.control;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class InstantTypeAdapter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

    @Override
    public JsonElement serialize(Instant instant, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(formatter.format(instant));
    }

    @Override
    public Instant deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String dateStr = jsonElement.getAsString();
        return Instant.from(formatter.parse(dateStr));
    }
}
