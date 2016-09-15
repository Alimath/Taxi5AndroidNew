package com.isolutions.taxi5.API.Taxi5SDKEntity;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by fedar.trukhan on 15.09.16.
 */

public class LocationDescriptionDeserializer implements JsonDeserializer<LocationDescription> {
    @Override
    public LocationDescription deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        LocationDescription locationDescription = gson.fromJson(json, LocationDescription.class);

        JsonObject jsonObject = json.getAsJsonObject();
        if(jsonObject.has("address")) {
            JsonElement jsonElement = jsonObject.get("address");
            if(jsonElement.isJsonArray() || jsonElement.isJsonNull()) {
                locationDescription.address = null;
            }
            else {
                LocationAddress locationAddress = gson.fromJson(jsonElement, new TypeToken<LocationAddress>(){}.getType());
                locationDescription.address = locationAddress;
            }
        }
        else {
            locationDescription.address = null;
        }

        return locationDescription;
    }
}
