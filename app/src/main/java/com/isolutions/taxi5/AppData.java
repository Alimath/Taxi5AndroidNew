package com.isolutions.taxi5;

import android.content.Context;

import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;

import java.security.Timestamp;
import java.util.TimeZone;

/**
 * Created by fedar.trukhan on 24.08.16.
 */

public class AppData {
    private static volatile AppData instance;

    private volatile OrderData currentOrder;
    private volatile Context appContext;

    public Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context context) {
        appContext = context;
    }

    public OrderData getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(OrderData orderData) {
        this.currentOrder= orderData;
    }

    public static AppData getInstance() {
        AppData localInstance = instance;
        if(localInstance == null) {
            synchronized(AppData.class) {
                localInstance = instance;
                if(localInstance == null) {
                    instance = localInstance = new AppData();
                }
            }

        }
        return localInstance;
    }
}
