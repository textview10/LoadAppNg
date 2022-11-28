package com.loadapp.load.base;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class ToStringSerializer implements JsonSerializer<Double> {

    @Override
    public JsonElement serialize(Double number, Type type, JsonSerializationContext jsonSerializationContext) {
        long longNum = number.longValue();
        if (number.doubleValue() > Long.MAX_VALUE) {
            return new JsonPrimitive(number);
        }
        if (number == longNum) {
            return new JsonPrimitive(longNum);
        } else {
            return new JsonPrimitive(number);
        }
    }
}
