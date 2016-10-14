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
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.merge.MergeAdapter;
import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.MyPlacesData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.MyPlacesResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;
import com.isolutions.taxi5.APIAssist.AssistCardsHolder;

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
    AdapterMyPlaces adapterFavorite;
    AdapterMyPlaces adapterMyPlaces;

    @BindView(R.id.fragment_my_places_list_view)
    ListView listView;

    Call<MyPlacesResponseData> myPlacesCall;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(AppData.getInstance().toolbar != null) {
            AppData.getInstance().toolbar.ConvertToDefaultWithTitle(getString(R.string.left_drawer_menu_item_plans));
        }

    }

    @Override
    public void onStart() {
        super.onStart();
//        if(adapterMyPlaces != null) {
//            adapterMyPlaces.updateResource(null);
//        }
//        if(adapterFavorite != null) {
//            adapterFavorite.updateResource(null);
//        }
        LoadMyPlaces();
    }

    MergeAdapter mergeAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_places, container, false);
        ButterKnife.bind(this, view);

        this.inflater = inflater;
//        adapter = new AdapterSearchAddress(AppData.getInstance().getAppContext(), mData);

        if(AppData.getInstance().toolbar != null) {
            AppData.getInstance().toolbar.ConvertToDefaultWithTitle(getString(R.string.left_drawer_menu_item_my_places));
        }

        adapterFavorite = new AdapterMyPlaces(AppData.getInstance().getAppContext(), mData);
        adapterFavorite.isFavoriteAdapter = true;

        adapterMyPlaces = new AdapterMyPlaces(AppData.getInstance().getAppContext(), mData);
        adapterMyPlaces.isFavoriteAdapter = false;

        mergeAdapter = new MergeAdapter();
        mergeAdapter.addView(createHeader(AppData.getInstance().getAppContext().getString(R.string.adapter_my_places_header_favorite)));
        mergeAdapter.addAdapter(adapterFavorite);
        mergeAdapter.addView(createHeader(AppData.getInstance().getAppContext().getString(R.string.adapter_my_places_header_my_places)));
        mergeAdapter.addAdapter(adapterMyPlaces);

        listView.setAdapter(mergeAdapter);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                onItemSelected(adapterView, view, i, l);
//            }
//        });



        return view;
    }

//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////        if(AppData.getInstance().leftDrawer != null) {
////            AppData.getInstance().leftDrawer.HighlightMenuItem(MenuLeft.OpenFragmentTypes.Map);
////        }
////        AppData.getInstance().mainActivity.OpenClearMap();
////        FragmentMap.getMapFragment().statusCreateOrderFragment.ClearFields();
////
////        OrderData order = new OrderData();
////        order.from = adapterView.getItem(i);
//
//
////        AppData.getInstance().setCurrentOrder(order, false);
////        FragmentMap.getMapFragment().RefreshView();
//
////        Log.d("taxi5", "Adapter: " + adapterView.getClass());
//    }

    void LoadMyPlaces() {
        if(adapterFavorite != null) {
            adapterFavorite.updateResource(null);
        }
        if(adapterMyPlaces != null) {
            adapterMyPlaces.updateResource(null);
        }

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
                        adapterMyPlaces.updateResource(mData);
                        adapterFavorite.updateResource(mData);
                    }
                    else {
                        mData = null;
                        adapterFavorite.updateResource(null);
                        adapterMyPlaces.updateResource(null);
                    }
                }
                else {
                    Toast.makeText(AppData.getInstance().mainActivity, "Ошибка при загрузке списка мест", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyPlacesResponseData> call, Throwable t) {
                Toast.makeText(AppData.getInstance().mainActivity, "Ошибка при загрузке списка мест", Toast.LENGTH_SHORT).show();
                Log.d("taxi5", "load myPlaces failure: " + t.getLocalizedMessage());
            }
        });
    }

    View createHeader(String headerText) {
        View v = inflater.inflate(R.layout.right_drawer_adapters_header, null);
        TextView titleView = (TextView) v.findViewById(R.id.right_drawer_adapter_headers_main_header_text_view);
        titleView.setText(headerText);

        return v;
    }
}
