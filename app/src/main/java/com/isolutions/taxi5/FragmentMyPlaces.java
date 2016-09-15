package com.isolutions.taxi5;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.MyPlacesData;
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
 * Created by Alimath on 15.09.16.
 */

public class FragmentMyPlaces extends Fragment {
    LayoutInflater inflater;
    private ArrayList<LocationData> mData = new ArrayList<>();
    AdapterSearchAddress adapter;

    @BindView(R.id.fragment_my_places_list_view)
    ListView listView;

    Call<MyPlacesResponseData> myPlacesCall;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(AppData.getInstance().toolbar != null) {
            AppData.getInstance().toolbar.ConvertToDefaultWithTitle(getString(R.string.left_drawer_menu_item_plans));
        }
        LoadMyPlaces();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_places, container, false);
        ButterKnife.bind(this, view);

        this.inflater = inflater;
//        adapter = new AdapterSearchAddress(AppData.getInstance().getAppContext(), mData);

        if(AppData.getInstance().toolbar != null) {
            AppData.getInstance().toolbar.ConvertToDefaultWithTitle(getString(R.string.left_drawer_menu_item_my_places));
        }

        adapter = new AdapterSearchAddress(AppData.getInstance().getAppContext(), mData);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onItemSelected(adapterView, view, i, l);
            }
        });

        return view;
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(AppData.getInstance().leftDrawer != null) {
            AppData.getInstance().leftDrawer.HighlightMenuItem(MenuLeft.OpenFragmentTypes.Map);
        }
        AppData.getInstance().mainActivity.OpenClearMap();
        FragmentMap.getMapFragment().statusCreateOrderFragment.ClearFields();

        OrderData order = new OrderData();
        order.from = adapter.getItem(i);


        AppData.getInstance().setCurrentOrder(order, false);
        FragmentMap.getMapFragment().RefreshView();
    }

    void LoadMyPlaces() {
        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }
        if(myPlacesCall != null) {
            myPlacesCall.cancel();
        }

        myPlacesCall = taxi5SDK.GetMyPlaces(TokenData.getInstance().getToken());

        myPlacesCall.enqueue(new Callback<MyPlacesResponseData>() {
            @Override
            public void onResponse(Call<MyPlacesResponseData> call, Response<MyPlacesResponseData> response) {
                if(response.isSuccessful()) {
                    if(response.body().getResponseData() != null && response.body().getResponseData().getPlacesData() != null) {
                        mData = response.body().getResponseData().getPlacesData();
                        adapter.updateResource(mData);
                    }
                    else {
                        mData = null;
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
