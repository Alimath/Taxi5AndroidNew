package com.isolutions.taxi5.APIAssist;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationDescription;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;
import com.isolutions.taxi5.API.Taxi5SDKEntitySerialezersDeserialezers.LocationDataDeserializer;
import com.isolutions.taxi5.API.Taxi5SDKEntitySerialezersDeserialezers.LocationDataSerializer;
import com.isolutions.taxi5.API.Taxi5SDKEntitySerialezersDeserialezers.LocationDescriptionDeserializer;
import com.isolutions.taxi5.API.Taxi5SDKEntitySerialezersDeserialezers.LocationDescriptionSerializer;
import com.isolutions.taxi5.API.Taxi5SDKEntitySerialezersDeserialezers.OrderDataSerializer;
import com.isolutions.taxi5.API.Taxi5SDKEntitySerialezersDeserialezers.ProfileDataSerializer;
import com.isolutions.taxi5.AppData;
import com.isolutions.taxi5.NoInternetActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fedar.trukhan on 29.09.16.
 */

public class ApiAssistFactory {
    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 60;
    private static final int TIMEOUT = 60;


    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public static AssistSDK getAssistSDK() {
        if(isNetworkConnected()) {
            return getRetrofit().create(AssistSDK.class);
        }
        else {
            Intent intent = new Intent(AppData.getInstance().currentActivity, NoInternetActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppData.getInstance().currentActivity.startActivity(intent);
            AppData.getInstance().currentActivity.finish();

            return null;
        }
    }

    @NonNull
    private static Retrofit getRetrofit() {
        Gson gson = new GsonBuilder().create();


        return new Retrofit.Builder()
                .baseUrl("https://pay140.paysec.by")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(CLIENT)
                .build();
    }

    public static boolean isNetworkConnected() {
        if(AppData.getInstance() != null && AppData.getInstance().getAppContext() != null) {
            ConnectivityManager cm = (ConnectivityManager) AppData.getInstance().getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null;
        }
        else {
            return false;
        }
    }
}
