package com.isolutions.taxi5;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseActionData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public FragmentMap mapFragment = new FragmentMap();
    public FragmentScreenProfile fragmentScreenProfile = new FragmentScreenProfile();
//    public FragmentCustomToolbar customToolbar = new FragmentCustomToolbar();

    @BindView(R.id.left_drawer_avatar_image) ImageView avatarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppData.getInstance().mainActivity = this;

//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//
////        ft.replace(R.id.main_activity_fragment_map_layout, mapFragment);
//        ft.add(customToolbar, "customtoolbar");
////        ft.replace(R.id.main_activity_fragment_custom_toolbar, customToolbar);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        ft.commit();

        OpenMap();

//        FragmentTransaction ft1 = getFragmentManager().beginTransaction();
//        ft1.replace(R.id.main_activity_fragment_custom_toolbar, customToolbar);
//        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        ft1.commit();


//        ProfileData profileData = ProfileData.getInstance();
//        profileData.setName("Hallo games");
//        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
//        Call<OrderResponseActionData> call = taxi5SDK.SendProfile(TokenData.getInstance().getToken(),
//                profileData);
//
//        call.enqueue(new Callback<OrderResponseActionData>() {
//            @Override
//            public void onResponse(Call<OrderResponseActionData> call, Response<OrderResponseActionData> response) {
//                if(response.isSuccessful()) {
//                    Log.d("taxi5", "profileUpdated");
//                }
//                else {
//                    Log.d("taxi5", "profileUpdated error");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<OrderResponseActionData> call, Throwable t) {
//                Log.d("taxi5", "profileUpdated failure");
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if(drawer.isDrawerOpen(findViewById(R.id.activity_main_nav_view_right))) {
                drawer.closeDrawer(findViewById(R.id.activity_main_nav_view_right));
            }
            else {
//                super.onBackPressed();
            }
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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void OpenRightMenu() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.END);
    }

    public void OpenLeftMenu() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    public void CloseRightMenu() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
    }

    public void CloseLeftMenu() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

//    public void onResponseRefreshToken(Call<TokenData> call, Response<TokenData> response) {
//        if(response.code() == 200) {
//            response.body().setAuthorized(true);
//            response.body().saveTokenData();
//            Log.d("taxi5", TokenData.getInstance().getDescription());
//        }
//        else {
//            //TODO: make a clear
//        }
//    }
//
//    public void onFailureRefreshToken(Call<TokenData> call, Throwable t) {
//        Log.d("taxi5", "responseCode: error");
//    }



    @Override
    protected void onResume() {
        super.onResume();
        AppData.getInstance().setAppForeground(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppData.getInstance().setAppForeground(false);
    }

    public void OpenProfileMenu() {
//        Log.d("taxi5", "onProfileButtonClick");

        ChangeFragment(fragmentScreenProfile);

        if (fragmentScreenProfile.isVisible()) {
            fragmentScreenProfile.RefreshView();
        }

        CloseLeftMenu();
    }

    public void OpenMap() {
        ChangeFragment(mapFragment);
//        mapFragment.RefreshView();
//        if(mapFragment.isVisible()) {
//        }
    }

    public void OpenClearMap() {
        AppData.getInstance().setCurrentOrder(null);
        ChangeFragment(mapFragment);
        mapFragment.RefreshView();
    }

    public void CloseMenus() {
        CloseLeftMenu();
        CloseRightMenu();
    }

    public void ChangeFragment(Fragment fragment) {
        if(AppData.getInstance().getAppForeground()) {
            CloseLeftMenu();
            CloseRightMenu();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_activity_fragment_map_layout, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        }
    }
}
