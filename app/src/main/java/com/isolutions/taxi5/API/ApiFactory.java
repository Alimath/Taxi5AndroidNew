package com.isolutions.taxi5.API;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntityDeserialezers.LocationDataDeserializer;
import com.isolutions.taxi5.API.Taxi5SDKEntityDeserialezers.LocationDataSerializer;
import com.isolutions.taxi5.API.Taxi5SDKEntityDeserialezers.OrderDataSerializer;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 60;
    private static final int TIMEOUT = 60;



    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    @NonNull
    public static Taxi5SDK getTaxi5SDK() {
        return getRetrofit().create(Taxi5SDK.class);
    }

    @NonNull
    private static Retrofit getRetrofit() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().
            registerTypeAdapter(LocationData.class, new LocationDataDeserializer()).
            registerTypeAdapter(LocationData.class, new LocationDataSerializer()).
            registerTypeAdapter(OrderData.class, new OrderDataSerializer()).create();


        return new Retrofit.Builder()
                .baseUrl("http://taxi5.by")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(CLIENT)
                .build();
    }
}
