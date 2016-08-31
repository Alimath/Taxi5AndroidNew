package com.isolutions.taxi5;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;

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

    private volatile Long serverClientOffset = 0L;

    public volatile Boolean selectedPlansCurrencyNew = true;

    public MenuLeft leftDrawer = null;
    public MenuRight rightDrawer = null;

    public MainActivity mainActivity = null;
    public FragmentCustomToolbar toolbar = null;

    public Boolean getAppForeground() {
        return isAppForeground;
    }

    public void setAppForeground(Boolean appForeground) {
        isAppForeground = appForeground;
    }

    private volatile Boolean isAppForeground = true;

    public Long getServerTimeZoneOffset() {
        return serverTimeZoneOffset;
    }

    public void setServerTimeZoneOffset(Long serverTimeZoneOffset) {
        this.serverTimeZoneOffset = serverTimeZoneOffset;
    }

    public Long getServerClientOffset() {
        return serverClientOffset;
    }

    public void setServerClientOffset(Long serverClientOffset) {
        this.serverClientOffset = serverClientOffset;
    }

    private volatile Long serverTimeZoneOffset = 10800L;

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

    public int getColor(int id) {
        return ContextCompat.getColor(appContext, id);
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(getAppContext().getPackageManager()) != null) {
            getAppContext().startActivity(intent);
        }
    }

    public Drawable getMyDrawable(int id) {
        int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return getAppContext().getDrawable(id);
        } else {
            return getAppContext().getResources().getDrawable(id);
        }
    }
}
