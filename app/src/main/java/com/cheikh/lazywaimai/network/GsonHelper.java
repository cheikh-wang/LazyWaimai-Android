package com.cheikh.lazywaimai.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.cheikh.lazywaimai.model.bean.Gender;
import com.cheikh.lazywaimai.model.bean.OrderStatus;
import com.cheikh.lazywaimai.model.bean.PayMethod;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonHelper {

    private static final SimpleDateFormat JSON_STRING_DATE = new SimpleDateFormat("yyy-MM-dd", Locale.CHINA);
    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT-8:00");
    private static final long SECOND_IN_MILLISECONDS = 1000L;

    public static Gson builderGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Integer.class, new JsonDeserializer() {
            @Override
            public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                try {
                    return json.getAsInt();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        builder.registerTypeAdapter(Date.class, new JsonDeserializer() {
            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                try {
                    long outer = json.getAsLong();
                    Calendar calendar = Calendar.getInstance(TIME_ZONE);
                    calendar.setTimeInMillis(outer * SECOND_IN_MILLISECONDS);
                    return calendar.getTime();
                } catch (NumberFormatException e) {
                    try {
                        return JSON_STRING_DATE.parse(json.getAsString());
                    } catch (ParseException e1) {
                        throw new JsonParseException(e1);
                    }
                }
            }
        });
        builder.registerTypeAdapter(OrderStatus.class, new JsonDeserializer<OrderStatus>() {
            @Override
            public OrderStatus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return OrderStatus.valueOf(json.getAsInt());
            }
        });
        builder.registerTypeAdapter(PayMethod.class, new JsonDeserializer<PayMethod>() {
            @Override
            public PayMethod deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return PayMethod.valueOf(json.getAsInt());
            }
        });
        builder.registerTypeAdapter(Gender.class, new JsonDeserializer<Gender>() {
            @Override
            public Gender deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return Gender.valueOf(json.getAsInt());
            }
        });
        builder.registerTypeAdapter(Gender.class, new JsonSerializer<Gender>() {
            @Override
            public JsonElement serialize(Gender gender, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(gender.getValue());
            }
        });
        return builder.create();
    }
}
