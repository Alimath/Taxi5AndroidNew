package com.isolutions.taxi5;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.isolutions.taxi5.API.ApiFactory;

/**
 * Created by fedar.trukhan on 31.08.16.
 */

public class NoInternetActivity extends AppCompatActivity {

    private CountDownTimer checkInetTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
        AppData.getInstance().currentActivity = this;

        restartTimer();
    }

    private void restartTimer() {
        this.checkInetTimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if(!ApiFactory.isNetworkConnected()) {
                    restartTimer();
                }
                else {
                    reloadApp();
                }
            }
        }.start();
    }

    private void reloadApp() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        this.finish();
    }


}
