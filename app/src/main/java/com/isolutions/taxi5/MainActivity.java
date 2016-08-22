package com.isolutions.taxi5;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public FragmentMap mapFragment = new FragmentMap();
    public FragmentCustomToolbar customToolbar = new FragmentCustomToolbar();

    @BindView(R.id.left_drawer_avatar_image) ImageView avatarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.main_activity_fragment_map_layout, mapFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        customToolbar.mainActivity = this;

        ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.main_activity_fragment_custom_toolbar, customToolbar);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        Call<TokenData> call = taxi5SDK.RefreshToken("refresh_token", "taxi5_ios_app", "cri2thrauoau6whucizem8aukeo9traa", TokenData.getInstance().getRefreshToken());

        call.enqueue(new Callback<TokenData>() {
            @Override
            public void onResponse(Call<TokenData> call, Response<TokenData> response) {
                onResponseRefreshToken(call, response);
            }

            @Override
            public void onFailure(Call<TokenData> call, Throwable t) {
                onFailureRefreshToken(call, t);
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        setContentView(R.layout.activity_login);
//
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            Log.d("taxi5","Start callz");
//            Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
//            Call<TokenData> call = taxi5SDK.Authorization("friday_sms", "375447221174", "taxi5_ios_app", "cri2thrauoau6whucizem8aukeo9traa", "4512");
////            Call<Void> call = taxi5SDK.GetSMSCode("friday_sms", "375447221174", "taxi5_ios_app");
//            call.enqueue(this);
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void onResponse(retrofit2.Call<Void> call, Response<Void> response) {
//        Log.d("taxi5", "responseCode: "+response.code());
//    }
//
//    @Override
//    public void onFailure(retrofit2.Call<Void> call, Throwable t) {
//        Log.d("taxi5", "responseCode: error");
//    }

//    @Override
//    public void onResponse(Call<TokenData> call, Response<TokenData> response) {
//        Log.d("taxi5", TokenData.getInstance().GetDescription());
//        if(response.code() == 200) {
//            Log.d("taxi5", "response: " + response.body().GetDescription());
//        }
//        else {
//            try {
//                Log.d("taxi5", "responseCode: " + response.errorBody().string());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//    @Override
//    public void onFailure(Call<TokenData> call, Throwable t) {
//        Log.d("taxi5", "responseCode: error");
//    }

    public void OpenLeftMenu() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    public void onResponseRefreshToken(Call<TokenData> call, Response<TokenData> response) {
        if(response.code() == 200) {
            response.body().setAuthorized(true);
            response.body().saveTokenData();
            Log.d("taxi5", TokenData.getInstance().getDescription());
        }
        else {

        }
    }

    public void onFailureRefreshToken(Call<TokenData> call, Throwable t) {
        Log.d("taxi5", "responseCode: error");
    }
}
