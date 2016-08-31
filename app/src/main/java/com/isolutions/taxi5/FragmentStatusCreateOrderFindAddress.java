package com.isolutions.taxi5;

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
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationsListResponseData;
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

    AdapterSearchAddress adapter;

    private ArrayList<LocationData> mData = new ArrayList<>();

    @BindView(R.id.fragment_status_create_order_find_address_list_view)
    ListView listView;

    Call<LocationsListResponseData> call;

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

    }

    public void SearchAddressesWithString(String searchString) {
        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }
        if(call != null) {
            call.cancel();
        }
        call = taxi5SDK.SearchAddresses(TokenData.getInstance().getToken(), searchString);

        call.enqueue(new Callback<LocationsListResponseData>() {
            @Override
            public void onResponse(Call<LocationsListResponseData> call, Response<LocationsListResponseData> response) {
                if(response.isSuccessful()) {
                    Log.d("taxi5", "load locations ok");
                    if(response.body().getResponseData() != null) {
                        adapter.updateResource(response.body().getResponseData());
                    }
                }
                else {
                    Log.d("taxi5", "load locations error: " + response.body().getErrors().get(0));
                }
            }

            @Override
            public void onFailure(Call<LocationsListResponseData> call, Throwable t) {
                Log.d("taxi5", "load locations failure");
            }
        });

    }
}
