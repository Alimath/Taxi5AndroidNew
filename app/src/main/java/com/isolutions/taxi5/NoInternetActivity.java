package com.isolutions.taxi5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by fedar.trukhan on 31.08.16.
 */

public class NoInternetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
        AppData.getInstance().currentActivity = this;
    }
}
