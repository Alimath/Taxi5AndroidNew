package com.isolutions.taxi5;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import butterknife.BindView;


/**
 * Created by fedar.trukhan on 26.07.16.
 */

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_activity_fragment_layout) FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Fragment phoneFragment = new FragmentLoginPhone();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.login_activity_fragment_layout, phoneFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public void OpenSMSFragment() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Fragment smsFragment = new FragmentLoginSMS();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.login_activity_fragment_layout, smsFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void OpenNameFragment() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Fragment nameFragment = new FragmentLoginName();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.login_activity_fragment_layout, nameFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }


    public void OpenMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
