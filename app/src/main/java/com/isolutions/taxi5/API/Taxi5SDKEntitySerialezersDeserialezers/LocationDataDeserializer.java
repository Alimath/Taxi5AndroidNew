package com.isolutions.taxi5.API.Taxi5SDKEntitySerialezersDeserialezers;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationDescription;

import java.lang.reflect.Type;

/**
 * Created by fedar.trukhan on 23.08.16.
 */


public class LocationDataDeserializer implements JsonDeserializer<LocationData> {
    @Override
    public LocationData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        LocationData locationData = new Gson().fromJson(json, LocationData.class);

        JsonObject jsonObject = json.getAsJsonObject();

        if(jsonObject.has("description")) {
            JsonElement jsonElement = jsonObject.get("description");
            if(jsonElement.isJsonPrimitive()) {
                locationData.locationDescription = null;
                locationData.locationStringDescription = jsonElement.getAsString();
            }
            else if (jsonElement.isJsonObject()) {
                locationData.locationStringDescription = null;
                LocationDescription locationDescription = new Gson().fromJson(jsonElement, new TypeToken<LocationDescription>(){}.getType());
                locationData.locationDescription = locationDescription;
            }
        }


        return locationData;
    }


}
