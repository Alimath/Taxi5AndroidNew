package com.isolutions.taxi5;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;
import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationsListResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.MyPlacesResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 31.08.16.
 */

public class FragmentStatusCreateOrderFindAddress extends Fragment {

    public boolean isFromLocationSelect = true;

    AdapterSearchAddress adapter;

    private ArrayList<LocationData> mData = new ArrayList<>();

    @BindView(R.id.fragment_status_create_order_find_address_list_view)
    ListView listView;

    Call<LocationsListResponseData> call;
    Call<MyPlacesResponseData> myPlacesCall;

    private boolean isMyPlacesOnData = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_status_create_order_find_address, container, false);
        ButterKnife.bind(this, view);

        adapter = new AdapterSearchAddress(AppData.getInstance().getAppContext(), mData);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onItemSelected(adapterView, view, i, l);
            }
        });

        if(AppData.getInstance().toolbar != null) {
            AppData.getInstance().toolbar.createOrderFindAddress = this;
        }



        return view;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        LocationData selectedLocation = adapter.getItem(i);
        if(isMyPlacesOnData || (selectedLocation.details != null && selectedLocation.details.address.building != null)  ||
                (selectedLocation.details != null && selectedLocation.details.locationObject != null)) {
            if (AppData.getInstance().mainActivity != null) {
//                AppData.getInstance().mainActivity.onBackPressed();

                if (FragmentMap.getMapFragment() != null && FragmentMap.getMapFragment().statusCreateOrderFragment != null &&
                        FragmentMap.getMapFragment().statusCreateOrderFragment.isAdded()) {
                    if (isFromLocationSelect) {
                        FragmentMap.getMapFragment().statusCreateOrderFragment.setFromLocation(selectedLocation);
                        FragmentMap.getMapFragment().HideSearhAddressView();
                        FragmentMap.getMapFragment().ScrollMaptoPos(new LatLng(selectedLocation.latitude, selectedLocation.longitude), true);
                    } else {
                        FragmentMap.getMapFragment().HideSearhAddressView();
                        FragmentMap.getMapFragment().statusCreateOrderFragment.setToLocation(selectedLocation);

                        if(AppData.getInstance().getCurrentOrder() != null) {
                            OrderData orderData = AppData.getInstance().getCurrentOrder();
                            orderData.to = selectedLocation;
                            AppData.getInstance().setCurrentOrder(orderData, AppData.getInstance().isOrderHistory);
                        }
                        else {
                            AppData.getInstance().setCurrentOrder(FragmentMap.getMapFragment().statusCreateOrderFragment.CreateOrder(), true);
                        }
                    }
//                    OrderData orderData = FragmentMap.getMapFragment().statusCreateOrderFragment.CreateOrder();
//                    AppData.getInstance().setCurrentOrder(orderData, true);
//                    if(!isFromLocationSelect) {
//                        FragmentMap.getMapFragment().noNeedGeocoding = false;
//                    }
//                    FragmentMap.getMapFragment().RefreshView();

                }
            }
        }
        else {
            if(AppData.getInstance().toolbar != null && AppData.getInstance().toolbar.searchToolbar.getVisibility() != View.INVISIBLE) {
                AppData.getInstance().toolbar.searchEditText.setText(selectedLocation.getStringDescription());
                AppData.getInstance().toolbar.searchEditText.requestFocus();
                AppData.getInstance().toolbar.searchEditText.setSelection(AppData.getInstance().toolbar.searchEditText.getText().toString().length());
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SearchAddressesWithString("");
    }

    public void SearchAddressesWithString(String searchString) {
        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }
        if(call != null) {
            call.cancel();
        }
        if(myPlacesCall != null) {
            myPlacesCall.cancel();
        }

        if(!TextUtils.isEmpty(searchString)) {
            isMyPlacesOnData = false;
            call = taxi5SDK.SearchAddresses(TokenData.getInstance().getToken(), searchString);

            call.enqueue(new Callback<LocationsListResponseData>() {
                @Override
                public void onResponse(Call<LocationsListResponseData> call, Response<LocationsListResponseData> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getResponseData() != null) {
                            adapter.updateResource(response.body().getResponseData());
                        }
                    } else {
                        Log.d("taxi5", "load locations error: " + response.body().getErrors().get(0));
                    }
                }

                @Override
                public void onFailure(Call<LocationsListResponseData> call, Throwable t) {
                    Log.d("taxi5", "load locations failure");
                }
            });
        }
        else {
            isMyPlacesOnData = true;

            myPlacesCall = taxi5SDK.GetMyPlaces(TokenData.getInstance().getToken());

            myPlacesCall.enqueue(new Callback<MyPlacesResponseData>() {
                @Override
                public void onResponse(Call<MyPlacesResponseData> call, Response<MyPlacesResponseData> response) {
                    if(response.isSuccessful()) {
                        if(response.body().getResponseData() != null && response.body().getResponseData().getPlacesData() != null) {
                            adapter.updateResource(response.body().getResponseData().getPlacesData());
                        }
                        else {
                            adapter.updateResource(null);
                        }
                    }
                    else {
//                        if(response.body().getErrors() != null && response.body().getErrors().size() > 0) {
//                            Log.d("taxi5", "load myPlaces error: " + response.body().getErrors().get(0));
//                        }
//                        else {
//                            Log.d("taxi5", "load myPlaces error");
//                        }
                    }
                }

                @Override
                public void onFailure(Call<MyPlacesResponseData> call, Throwable t) {
                    Log.d("taxi5", "load myPlaces failure: " + t.getLocalizedMessage());
                }
            });

        }

    }
}
