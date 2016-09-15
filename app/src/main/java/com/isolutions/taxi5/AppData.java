package com.isolutions.taxi5;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;

import com.google.android.gms.maps.model.LatLng;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;

import java.io.ByteArrayOutputStream;
import java.security.Timestamp;
import java.util.TimeZone;

/**
 * Created by fedar.trukhan on 24.08.16.
 */

public class AppData {
    public static final String client_id = "taxi5_android_app";
    public static final String client_secret = "jasjh8afskjb3nbmansufi82jk2bdask";

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

    public LatLng nullPoint = new LatLng(53.902464, 27.56149);

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

    public volatile AppCompatActivity currentActivity;

    public void setAppContext(Context context) {
        appContext = context;
    }

    public OrderData getCurrentOrder() {
        return currentOrder;
    }

    public volatile boolean isOrderHistory = false;
    public void setCurrentOrder(OrderData orderData, boolean isHistory) {
        this.currentOrder= orderData;
        isOrderHistory = isHistory;
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

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encoded;
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = appContext.getResources().getDisplayMetrics();
        float density = appContext.getResources().getDisplayMetrics().density;
        int dp = Math.round(px / density);//(displayMetrics.ydpi / DisplayMetrics.density));
        return dp;
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = appContext.getResources().getDisplayMetrics();
        float density = appContext.getResources().getDisplayMetrics().density;
        int px = Math.round(dp * density);//(displayMetrics.ydpi / DisplayMetrics.DENSITY_420));
        return px;
    }
}
