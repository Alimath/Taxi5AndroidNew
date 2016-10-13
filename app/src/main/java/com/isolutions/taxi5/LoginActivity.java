package com.isolutions.taxi5;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import butterknife.BindView;


/**
 * Created by fedar.trukhan on 26.07.16.
 */

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_activity_fragment_layout) FrameLayout frameLayout;

    public volatile String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Fragment phoneFragment = new FragmentLoginPhone();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.login_activity_fragment_layout, phoneFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        AppData.getInstance().currentActivity = this;
        try {
            ft.commit();
        }
        catch (Exception error) {
            Log.d("taxi5", "change map status fragment error");
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.whiteColor));
//        }
    }

    public void OpenSMSFragment() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Fragment smsFragment = new FragmentLoginSMS();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.login_activity_fragment_layout, smsFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        try {
            ft.commit();
        }
        catch (Exception error) {
            Log.d("taxi5", "change map status fragment error");
        }
    }

    public void OpenNameFragment() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Fragment nameFragment = new FragmentLoginName();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.login_activity_fragment_layout, nameFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        try {
            ft.commit();
        }
        catch (Exception error) {
            Log.d("taxi5", "change map status fragment error");
        }
    }


    public void OpenMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void OpenPhoneFragment() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Fragment phoneFragment = new FragmentLoginPhone();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.login_activity_fragment_layout, phoneFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        try {
            ft.commit();
        }
        catch (Exception error) {
            Log.d("taxi5", "change map status fragment error");
        }
    }
}
