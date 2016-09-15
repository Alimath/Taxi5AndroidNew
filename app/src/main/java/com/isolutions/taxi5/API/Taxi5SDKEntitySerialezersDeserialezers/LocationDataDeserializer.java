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
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationDescription;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationDescriptionDeserializer;

import java.lang.reflect.Type;

/**
 * Created by fedar.trukhan on 23.08.16.
 */


public class LocationDataDeserializer implements JsonDeserializer<LocationData> {
    @Override
    public LocationData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().
                registerTypeAdapter(LocationDescription.class, new LocationDescriptionDeserializer()).create();

        LocationData locationData = gson.fromJson(json, LocationData.class);

        JsonObject jsonObject = json.getAsJsonObject();

        if(jsonObject.has("description")) {
            JsonElement jsonElement = jsonObject.get("description");
            if(jsonElement.isJsonPrimitive()) {
                locationData.locationDescription = null;
                locationData.locationStringDescription = jsonElement.getAsString();
            }
            else if (jsonElement.isJsonObject()) {
                locationData.locationStringDescription = null;
                LocationDescription locationDescription = gson.fromJson(jsonElement, new TypeToken<LocationDescription>(){}.getType());
                locationData.locationDescription = locationDescription;
            }
        }
        if(jsonObject.has("details")) {
            JsonElement jsonElement = jsonObject.get("details");
            if(!jsonElement.isJsonPrimitive()) {
                LocationDescription locationDescription = gson.fromJson(jsonElement, new TypeToken<LocationDescription>(){}.getType());
                locationData.locationDescription = locationDescription;
            }
        }


        if(jsonObject.has("id")) {
            JsonElement jsonElementID = jsonObject.get("id");
            if(jsonElementID.isJsonPrimitive()) {
                locationData.favoriteID = jsonElementID.getAsInt();
            }
        }
        if(jsonObject.has("counter")) {
            JsonElement jsonElementCounter = jsonObject.get("counter");
            if(jsonElementCounter.isJsonPrimitive()) {
                locationData.favoriteCounter = jsonElementCounter.getAsInt();
            }
        }
        if(jsonObject.has("alias")) {
            JsonElement jsonElementAlias = jsonObject.get("alias");
            if(jsonElementAlias.isJsonPrimitive()) {
                locationData.favoriteAlias = jsonElementAlias.getAsString();
            }
        }
        if(jsonObject.has("is_favorite")) {
            JsonElement jsonElementIsFavorite = jsonObject.get("is_favorite");
            if(jsonElementIsFavorite.isJsonPrimitive()) {
                locationData.favoriteIsFavorite = jsonElementIsFavorite.getAsBoolean();
            }
        }

        return locationData;
    }


}
