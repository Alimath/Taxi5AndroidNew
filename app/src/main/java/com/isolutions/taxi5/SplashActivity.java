package com.isolutions.taxi5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fedar.trukhan on 25.07.16.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.d("taxi5", "hallo wolds");
        ButterKnife.bind(this);
    }

    @OnClick(R.id.splashtestbutton)
    public void OnChangeViewClick() {
        Log.d("taxi5", "button2 click");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        Log.d("taxi5", "button1 click");
    }

    @OnClick(R.id.splashtestbutton2)
    public void OnChangeView2Click() {
        Log.d("taxi5", "button2 click");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        Log.d("taxi5", "button2 click");
    }
}
