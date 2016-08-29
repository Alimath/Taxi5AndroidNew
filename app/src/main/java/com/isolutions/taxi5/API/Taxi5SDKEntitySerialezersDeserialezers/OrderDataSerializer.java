package com.isolutions.taxi5.API.Taxi5SDKEntitySerialezersDeserialezers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;

import java.lang.reflect.Type;

/**
 * Created by fedar.trukhan on 24.08.16.
 */

public class OrderDataSerializer implements JsonSerializer<OrderData> {
    @Override
    public JsonElement serialize(OrderData src, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().
                registerTypeAdapter(LocationData.class, new LocationDataSerializer()).create();

        JsonElement result = gson.toJsonTree(src);

        Log.d("taxi5", "orderData serialized: " + new Gson().toJson(result));

        return result;
    }
}
