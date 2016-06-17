package com.deliveryhero.deliveryherotest.network.deserializers;

/**
 * Created by Hamza_MACB105 on 13/06/16.
 */

import com.deliveryhero.deliveryherotest.beans.models.Photo;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class PhotoDeserializer implements JsonDeserializer<List<Photo>> {

    // Constants

    public static final Type TYPE = new TypeToken<List<Photo>>() {
    }.getType();

    // JsonDeserializer

    @Override
    public List<Photo> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        try {
            return new Gson().fromJson(json.getAsJsonArray(), TYPE);
        } catch (Exception exception) {

            // On any error - return null
            return null;
        }
    }
}

