package com.isolutions.taxi5.API.Taxi5SDKEntitySerialezersDeserialezers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationDescription;

/**
 * Created by fedar.trukhan on 20.09.16.
 */

public class LocationDescriptionSerializer implements JsonSerializer<LocationDescription> {
@Override
public JsonElement serialize(LocationDescription src, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = new GsonBuilder().create();
        JsonObject result = (JsonObject)gson.toJsonTree(src);
        return result;
    }
}