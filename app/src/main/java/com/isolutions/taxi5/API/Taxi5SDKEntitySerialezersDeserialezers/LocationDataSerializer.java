package com.isolutions.taxi5.API.Taxi5SDKEntitySerialezersDeserialezers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;

import java.lang.reflect.Type;

/**
 * Created by fedar.trukhan on 24.08.16.
 */

public class LocationDataSerializer implements JsonSerializer<LocationData> {
    @Override
    public JsonElement serialize(LocationData src, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        Gson gson2 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().
        registerTypeAdapter(LocationData.class, new LocationDataSerializer()).create();

        JsonObject result = (JsonObject)gson.toJsonTree(src);
        if(src.locationDescription != null) {
            Log.d("taxi5", "add location description");
            result.add("description", gson2.toJsonTree(src.locationDescription));
        }
        else if(src.details != null) {
            Log.d("taxi5", "add details");
            result.add("description", gson2.toJsonTree(src.details));
        }
        else if(src.locationStringDescription != null) {
            Log.d("taxi5", "add string location description");
            result.addProperty("description", src.locationStringDescription);
        }
        return result;
    }
}
