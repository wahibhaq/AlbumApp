package com.deliveryhero.deliveryherotest.network.deserializers;

import com.deliveryhero.deliveryherotest.beans.models.Album;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by MACB105 on 11/06/16.
 */
public class AlbumDeserializer implements JsonDeserializer<List<Album>> {


    public static final Type TYPE = new TypeToken<List<Album>>() {
    }.getType();

    // JsonDeserializer

    @Override
    public List<Album> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        try {

            return new Gson().fromJson(json.getAsJsonArray(), TYPE);
        } catch (Exception exception) {

            // On any error - return null
            return null;
        }
    }
}
