package com.isolutions.taxi5;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.maps.model.LatLng;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileResponseData;
import com.isolutions.taxi5.APIAssist.AssistCardsHolder;
import com.isolutions.taxi5.FragmentPlans;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseActionData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public FragmentMap mapFragment = new FragmentMap();
    public final FragmentScreenProfile fragmentScreenProfile = new FragmentScreenProfile();
    public FragmentAboutUs fragmentAboutUs = new FragmentAboutUs();
    public FragmentPlans fragmentPlans = new FragmentPlans();
    public FragmentMyPlaces fragmentMyPlaces = new FragmentMyPlaces();
    public FragmentPayments fragmentPayments = new FragmentPayments();
    public FragmentPaymentHasStoredCards fragmentPaymentHasStoredCards = new FragmentPaymentHasStoredCards();
    public FragmentPaymentsCustomerInfo fragmentPaymentsCustomerInfo = new FragmentPaymentsCustomerInfo();
//    public FragmentCustomToolbar customToolbar = new FragmentCustomToolbar();

    @BindView(R.id.left_drawer_avatar_image) ImageView avatarImageView;


    @BindView(R.id.main_activity_splash)
    RelativeLayout splashView;

    LocationManager locationManager;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    private boolean isLocationEnabled() {
        if(locationManager != null) {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        else {
            return false;
        }
    }

    PermissionListener locatioPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
//            Log.d("taxi5", "location check granted");
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if(FragmentMap.getMapFragment() != null && FragmentMap.getMapFragment().mMap != null) {
                FragmentMap.getMapFragment().mMap.setMyLocationEnabled(true);
                FragmentMap.getMapFragment().mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
            else {
            }
//            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//            Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };


    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
//            Log.d("taxi5", "location cahnged: " + location.getLatitude() + " : " + location.getLongitude());
            //TODO Добавить переход на локацию, после определения геопозиции (первого)
//            if(AppData.getInstance().mainActivity != null && AppData.getInstance().mainActivity.mapFragment.isVisible()) {
//                AppData.getInstance().mainActivity.OpenClearMap();
//            }

            // Creating a criteria object to retrieve provider
//            Criteria criteria = new Criteria();
//
//            // Getting the name of the best provider
//            String provider = locationManager.getBestProvider(criteria, true);
//
//            // Getting Current Location
//            Location locationBest = locationManager.getLastKnownLocation(provider);

//            try
            AppData.getInstance().nullPoint = new LatLng(location.getLatitude(), location.getLongitude());
        }

        @Override
        public void onProviderDisabled(String provider) {
//            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
//            checkEnabled();
//            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
//            if (provider.equals(LocationManager.GPS_PROVIDER)) {
//                tvStatusGPS.setText("Status: " + String.valueOf(status));
//            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
//                tvStatusNet.setText("Status: " + String.valueOf(status));
//            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
//                    PERMISSION_ACCESS_COARSE_LOCATION);
//        }

        if(ProfileData.getInstance() != null && ProfileData.getInstance().getId() != null) {
            FlurryAgent.setUserId(ProfileData.getInstance().getId().toString());
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        new TedPermission(this).setPermissionListener(this.locatioPermissionListener).setRationaleMessage(R.string.permission_location_rationale_message)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION).setDeniedMessage(R.string.permission_location_denied_message).check();
        AppData.getInstance().mainActivity = this;
        AppData.getInstance().currentActivity = this;
        Log.d("taxi5", "open clear map");
        OpenClearMap();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setScrimColor(getResources().getColor(android.R.color.transparent));

        // TODO: НАЙТИ ПРИЧИНЫ И ИСПРАВИТЬ НЕОТОБРАЖЕНИЕ КАРТЫ СРАЗУ ПОСЛЕ ПЕРЕЗАХОДА (СМЕНЕ ПОЛЬЗОВАТЕЛЯ)
        CountDownTimer timer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                if(!mapFragment.isAdded() ) {
                    OpenClearMap();
                }
            }
        }.start();
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
        if(mapFragment!= null && mapFragment.isVisible() && mapFragment.statusCreateOrderFindAddressFragment != null && mapFragment.statusCreateOrderFindAddressFragment.isVisible()) {
            mapFragment.HideSearhAddressView();
        }
        else if(mapFragment != null && mapFragment.isVisible() && mapFragment.statusPaymentFragment != null &&
                mapFragment.statusPaymentFragment.isVisible() && mapFragment.statusPaymentFragment.isCardsShowing) {
            mapFragment.statusPaymentFragment.HideCardsList();
        }
        else if(fragmentPaymentsCustomerInfo != null && fragmentPaymentsCustomerInfo.isVisible()) {
            OpenPayments();
        }
        else if(fragmentScreenProfile != null && fragmentScreenProfile.croper != null
                && fragmentScreenProfile.croper.getVisibility() == View.VISIBLE) {
            fragmentScreenProfile.CancelImageCroping();
        }
        else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

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
    protected void onPause() {
        super.onPause();
        if(locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
        isStarted = false;
        AppData.getInstance().setAppForeground(false);
        if(AppData.getInstance().mainActivity != null && AppData.getInstance().mainActivity.mapFragment != null
                && AppData.getInstance().mainActivity.mapFragment.isAdded() && AppData.getInstance().getCurrentOrder() != null) {
            Paper.book().write("CURRENTSTATESAVEDORDER", AppData.getInstance().getCurrentOrder());
        }
        else {
            Paper.book().delete("CURRENTSTATESAVEDORDER");
        }
    }

    public void ShowSplash() {
        splashView.setVisibility(View.VISIBLE);
    }
    public void HideSplash() {
        splashView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isStarted = true;
        AppData.getInstance().setAppForeground(true);
        if(locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 10, locationListener);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 10,
                    locationListener);
        }

        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }
        Call<TokenData> call = taxi5SDK.RefreshToken("refresh_token", AppData.client_id, AppData.client_secret, TokenData.getInstance().getRefreshToken());
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

//        if(FragmentMap.getMapFragment().isVisible()) {
//            OrderData order = AppData.getInstance().getCurrentOrder();
//            if(order != null && order.status != null && order.status.status != null) {
//                ShowSplash();
//                AppData.getInstance().mainActivity.mapFragment.ReadOrderState();
//            }
//            else {
//                AppData.getInstance().mainActivity.OpenClearMap();
//                AppData.getInstance().leftDrawer.HighlightMenuItem(MenuLeft.OpenFragmentTypes.Map);
//            }
//
////            FragmentMap.getMapFragment().RefreshView();
////            OpenClearMap();
//
//        }
    }
    public void onResponseRefreshToken(Call<TokenData> call, Response<TokenData> response) {
        if(response.isSuccessful()) {
            response.body().setAuthorized(true);
            response.body().saveTokenData();
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    public void onFailureRefreshToken(Call<TokenData> call, Throwable t) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void OpenPlansMenu() {
        ChangeFragment(fragmentPlans);
        CloseMenus();
    }

    public void OpenProfileMenu() {
//        Log.d("taxi5", "onProfileButtonClick");
//
        ChangeFragment(fragmentScreenProfile);

        if (fragmentScreenProfile.isVisible()) {
            fragmentScreenProfile.RefreshView();
        }

        CloseLeftMenu();
    }

    public void OpenAboutUs() {
        ChangeFragment(fragmentAboutUs);
        CloseMenus();
    }

    public void OpenClearMap() {
        AppData.getInstance().setCurrentOrder(null, false);
        AppData.getInstance().leftDrawer.HighlightMenuItem(MenuLeft.OpenFragmentTypes.Map);
        ChangeFragment(mapFragment);
        mapFragment.RefreshView();
        mapFragment.ScrollMaptoPos(AppData.getInstance().nullPoint, false);
    }

    public void CloseMenus() {
        CloseLeftMenu();
        CloseRightMenu();
    }

    public void OpenMyPlaces() {
        ChangeFragment(fragmentMyPlaces);
        CloseMenus();
    }

    public void OpenPayments() {
        if((AssistCardsHolder.GetCards() != null && !AssistCardsHolder.GetCards().isEmpty()) || AssistCardsHolder.GetOneClickState()) {
            ChangeFragment(fragmentPaymentHasStoredCards);
        }
        else {
            ChangeFragment(fragmentPayments);
        }
        CloseMenus();
    }

    public void OpenPaymentsCustomerInfo() {
        ChangeFragment(fragmentPaymentsCustomerInfo);
    }

    public void ChangeFragment(Fragment fragment) {
        if(AppData.getInstance().getAppForeground()) {
            CloseLeftMenu();
            CloseRightMenu();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_activity_fragment_map_layout, fragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            try {
                ft.commit();
                getSupportFragmentManager().executePendingTransactions();
            }
            catch (Exception error) {
                Log.d("taxi5", "change fragment error");
            }
        }
    }

    @BindView(R.id.main_activity_fragment_custom_toolbar)
    FrameLayout toolbarLayout;

    @BindView(R.id.main_activity_fragment_map_layout)
    FrameLayout mapLayout;

    @Override
    public void onStart() {
        super.onStart();
        OpenClearMap();
        isStarted = true;
    }

    @Override
    public void onRestart() {
        super.onRestart();
        OpenClearMap();
        isStarted = true;
    }



    private boolean isStarted = false;


//    public void HideToolbar() {
//        FrameLayout.LayoutParams toolbarParams = (FrameLayout.LayoutParams)toolbarLayout.getLayoutParams();
//        FrameLayout.LayoutParams mapParams = (FrameLayout.LayoutParams)mapLayout.getLayoutParams();
//
//        toolbarParams.height = 0;
//        mapParams.topMargin = 0;
//
//        toolbarLayout.setLayoutParams(toolbarParams);
//        mapLayout.setLayoutParams(mapParams);
//    }
//
//    public void ShowToolbar() {
//        FrameLayout.LayoutParams toolbarParams = (FrameLayout.LayoutParams)toolbarLayout.getLayoutParams();
//        FrameLayout.LayoutParams mapParams = (FrameLayout.LayoutParams)mapLayout.getLayoutParams();
//
//        toolbarParams.height = 56;
//        mapParams.topMargin = 56;
//
//        toolbarLayout.setLayoutParams(toolbarParams);
//        mapLayout.setLayoutParams(mapParams);
//    }
}
