package com.isolutions.taxi5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fedar.trukhan on 25.07.16.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_splash);


        TokenData tokenData = TokenData.getInstance();

        if(tokenData.getAuthorized()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
