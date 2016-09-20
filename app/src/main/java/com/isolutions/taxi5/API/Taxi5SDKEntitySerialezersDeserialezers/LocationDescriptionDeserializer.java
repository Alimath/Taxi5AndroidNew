package com.isolutions.taxi5.API.Taxi5SDKEntitySerialezersDeserialezers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationAddress;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationDescription;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationObject;

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
        if(jsonObject.has("object")) {
            JsonElement jsonElement = jsonObject.get("object");
            if(jsonElement.isJsonArray() || jsonElement.isJsonNull()) {
                locationDescription.locationObject = null;
            }
            else {
                LocationObject locationObject = gson.fromJson(jsonElement, new TypeToken<LocationObject>(){}.getType());
                locationDescription.locationObject = locationObject;
            }
        }
        else {
            locationDescription.locationObject = null;
        }

        return locationDescription;
    }
}
