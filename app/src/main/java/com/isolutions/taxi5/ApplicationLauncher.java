package com.isolutions.taxi5;

import android.app.Application;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;
import com.isolutions.taxi5.APIAssist.AssistCardsHolder;

import io.paperdb.Paper;

public class ApplicationLauncher extends Application {
    public String temp_login_phone;
    public String temp_login_code;

    @Override
    public void onCreate() {
        Paper.init(getApplicationContext());
        AppData.getInstance().setAppContext(this);


        if(ProfileData.getInstance() != null && ProfileData.getInstance().getId() != null) {
            FlurryAgent.setUserId(ProfileData.getInstance().getId().toString());
            Log.d("taxi5", "add userid to flurry: " + ProfileData.getInstance().getId().toString());
        }
        new FlurryAgent.Builder().withLogEnabled(false).build(this, "TVNS8D2TBWGXY6S676GQ");
    }
}
