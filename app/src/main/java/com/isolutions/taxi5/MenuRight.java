package com.isolutions.taxi5;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ActiveHistoryOrdersResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 23.08.16.
 */

public class MenuRight extends Fragment {
    private LayoutInflater inflater;
    @BindView(R.id.right_drawer_list_view_main) ListView listView;
    private ArrayList<OrderData> activeOrders = new ArrayList<>();
    private ArrayList<OrderData> historyOrders = new ArrayList<>();


    private CountDownTimer checkActiveOrderTimer;
    private CountDownTimer checkHistoryOrderTimer;

    private AdapterRightMenuOrder adapterActiveOrders;
    private AdapterRightMenuOrder adapterHistoryOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
        this.inflater = inflater;
        View rightMenu= inflater.inflate(R.layout.right_drawer, container, false);
        ButterKnife.bind(this, rightMenu);

        CheckActiveOrders();
        CheckHistoryOrders();

        adapterActiveOrders = new AdapterRightMenuOrder(AppData.getInstance().getAppContext(), activeOrders);
        adapterHistoryOrders = new AdapterRightMenuOrder(AppData.getInstance().getAppContext(), historyOrders);
        adapterHistoryOrders.isActiveOrders = false;



//        for (String item: testData) {
//
//        }


//        ArrayAdapter adapter = new ArrayAdapter(AppData.getInstance().getAppContext(), android.R.layout.simple_list_item_1, testData);
//        listViewActiveOrders.setAdapter(adapter);

//        ArrayAdapter adapter2 = new ArrayAdapter(AppData.getInstance().getAppContext(), android.R.layout.simple_list_item_1, testData2);
//        listViewHistory.setAdapter(adapter2);

        MergeAdapter mergeAdapter = new MergeAdapter();
        mergeAdapter.addView(createHeader(AppData.getInstance().getAppContext().getString(R.string.right_drawer_active_orders_header)));
        mergeAdapter.addAdapter(adapterActiveOrders);
        mergeAdapter.addView(createHeader(AppData.getInstance().getAppContext().getString(R.string.right_drawer_history_orders_header)));
        mergeAdapter.addAdapter(adapterHistoryOrders);


        listView.setAdapter(mergeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(AppData.getInstance().mainActivity != null) {
                    if(AppData.getInstance().leftDrawer != null) {
                        AppData.getInstance().leftDrawer.HighlightMenuItem(MenuLeft.OpenFragmentTypes.Map);
                    }
                    AppData.getInstance().mainActivity.OpenClearMap();

                    boolean isHistorySelected = false;

                    int realPos = position - 1;
                    if (realPos > activeOrders.size()) {
                        realPos -= activeOrders.size();
                        realPos -= 1;
                        isHistorySelected = true;
                    }

//                    TODO: Сделать запись orderdata в appdata, после чего просто открывать карту. а уже в самой карте при инициализации сделать проверку и обновление статусов.

                    if (!isHistorySelected) {
                        AppData.getInstance().setCurrentOrder(activeOrders.get(realPos), false);
                        if (AppData.getInstance().mainActivity != null) {
                            AppData.getInstance().mainActivity.CloseRightMenu();
                        }

                        FragmentMap.getMapFragment().RefreshView();
                    }
                    else {
                        AppData.getInstance().setCurrentOrder(historyOrders.get(realPos), true);
                        if (AppData.getInstance().mainActivity != null) {
                            AppData.getInstance().mainActivity.CloseRightMenu();
                        }

                        FragmentMap.getMapFragment().RefreshView();
                    }
//                        AppData.getInstance().setCurrentOrder(historyOrders.get(realPos), true);
//                        AppData.getInstance().mainActivity.OpenMap();
//
//                        if (AppData.getInstance().mainActivity != null) {
//                            AppData.getInstance().mainActivity.CloseRightMenu();
//                        }
//
//                        FragmentMap.getMapFragment().RefreshView();

//                        if (order != null) {
//                            if (order.from != null) {
//                                mapFragment.statusCreateOrderFragment.setFromLocation(order.from);
//                                mapFragment.ScrollMaptoPos(new LatLng(order.from.latitude, order.from.longitude), true);
//                            } else {
//                                mapFragment.ScrollMaptoPos(mapFragment.nullPoint, false);
//                            }
//                            if (order.to != null) {
//                                mapFragment.statusCreateOrderFragment.setToLocation(order.to);
//                            } else {
//                                mapFragment.statusCreateOrderFragment.setToLocation(null);
//                            }
//
//                        }
//                    }
                }
            }

        });


        AppData.getInstance().rightDrawer = this;

        return rightMenu;
    }



    View createHeader(String headerText) {
        View v = inflater.inflate(R.layout.right_drawer_adapters_header, null);
        TextView titleView = (TextView) v.findViewById(R.id.right_drawer_adapter_headers_main_header_text_view);
        titleView.setText(headerText);

        return v;
    }



    public void startUpdateActiveOrdersTimer() {
        if(AppData.getInstance().toolbar != null) {
            if(activeOrders != null) {
                AppData.getInstance().toolbar.SetOrderCount(activeOrders.size());
            }
            else {
                AppData.getInstance().toolbar.SetOrderCount(0);
            }
        }

        if(this.checkActiveOrderTimer != null) {
            this.checkActiveOrderTimer.cancel();
        }
        this.checkActiveOrderTimer = new CountDownTimer(3000, 1000) {
            public void onTick(long l) {

            }

            public void onFinish() {
                CheckActiveOrders();
            }
        }.start();
    }

    public void startUpdateHistoryOrdersTimer() {
        if(this.checkHistoryOrderTimer != null) {
            this.checkHistoryOrderTimer.cancel();
        }
        this.checkHistoryOrderTimer = new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                CheckHistoryOrders();
            }
        }.start();
    }

    private void CheckActiveOrders() {
        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }
        Call<ActiveHistoryOrdersResponseData> call = taxi5SDK.ActiveOrders(TokenData.getInstance().getToken());

        call.enqueue(new Callback<ActiveHistoryOrdersResponseData>() {
            @Override
            public void onResponse(Call<ActiveHistoryOrdersResponseData> call, Response<ActiveHistoryOrdersResponseData> response) {
                onCheckActiveOrdersResponse(call, response);
            }

            @Override
            public void onFailure(Call<ActiveHistoryOrdersResponseData> call, Throwable t) {
                onCheckActiveOrdersFailure(call, t);
            }
        });
    }

    private void CheckHistoryOrders() {
        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }
        Call<ActiveHistoryOrdersResponseData> call = taxi5SDK.HistoryOrders(TokenData.getInstance().getToken());

        call.enqueue(new Callback<ActiveHistoryOrdersResponseData>() {
            @Override
            public void onResponse(Call<ActiveHistoryOrdersResponseData> call, Response<ActiveHistoryOrdersResponseData> response) {
                onCheckHistoryOrdersResponse(call, response);
            }

            @Override
            public void onFailure(Call<ActiveHistoryOrdersResponseData> call, Throwable t) {
                onCheckHistoryOrdersFailure(call, t);
            }
        });
    }

    public void onCheckActiveOrdersResponse(Call<ActiveHistoryOrdersResponseData> call, Response<ActiveHistoryOrdersResponseData> response) {
        if(response.isSuccessful()) {
            if(response.body() != null && response.body().getResponseData() != null) {
                activeOrders = response.body().getResponseData();
                adapterActiveOrders.updateResource(activeOrders);
            }
            else {
            }
        }
        else {
            Log.d("taxi5", "some errors in active orders parse");
        }
        startUpdateActiveOrdersTimer();
    }

    public void onCheckActiveOrdersFailure(Call<ActiveHistoryOrdersResponseData> call, Throwable t) {
        startUpdateActiveOrdersTimer();
        Log.d("taxi5", t.getLocalizedMessage());
    }


    public void onCheckHistoryOrdersResponse(Call<ActiveHistoryOrdersResponseData> call, Response<ActiveHistoryOrdersResponseData> response) {
        if(response.isSuccessful()) {
            if(response.body() != null && response.body().getResponseData() != null) {
                historyOrders = response.body().getResponseData();
                adapterHistoryOrders.updateResource(historyOrders);
            }
        }
        else {
            Log.d("taxi5", "some errors in history orders parse");
        }
        startUpdateHistoryOrdersTimer();
    }

    public void onCheckHistoryOrdersFailure(Call<ActiveHistoryOrdersResponseData> call, Throwable t) {
        startUpdateHistoryOrdersTimer();
        Log.d("taxi5", "failure to load history");
        Log.d("taxi5", t.getLocalizedMessage());
    }

}
