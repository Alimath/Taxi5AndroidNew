package com.isolutions.taxi5;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import io.paperdb.Paper;

public class ApplicationLauncher extends Application {
    public String temp_login_phone;
    public String temp_login_code;

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(getApplicationContext());
    }
}
