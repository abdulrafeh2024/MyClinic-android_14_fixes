package com.telemedicine.myclinic.webservices;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ckm4 on 11/15/16.
 */

public class GsonUTCDateAdapter implements JsonDeserializer<Date> {
    private static final String[] DATE_FORMATS = new String[]{
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'"
    };

    @Override
    public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        String date = element.getAsString();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        for (String format : DATE_FORMATS) {
            try {
                formatter = new SimpleDateFormat(format);
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                return formatter.parse(date);
            } catch (ParseException e) {
//                throw new JsonParseException(e);
            }
        }
        return null;
    }
}
