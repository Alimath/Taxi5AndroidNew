package com.isolutions.taxi5;

/**
 * Created by fedar.trukhan on 22.08.16.
 */


import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.google.gson.Gson;
import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.CustomerData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderOptions;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentStatusCreateOrder extends StatusesBaseFragment {
    @BindView(R.id.fragment_status_create_order_comment_edit_text)
    EditText commentEditText;

    @BindView(R.id.fragment_status_create_order_approximate_price_layout)
    LinearLayout estimatedPriceLayout;

    @BindView(R.id.fragment_status_create_order_from_text)
    TextView fromText;

    @BindView(R.id.fragment_status_create_order_to_text)
    TextView toText;

    @BindView(R.id.fragment_status_create_order_from_to_view_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.fragment_status_create_order_button_progress_bar)
    ProgressBar buttonProgressBar;

    @BindView(R.id.fragment_status_create_order_button)
    Button createOrderButton;

    private LocationData fromLocation;
    private LocationData toLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
        View createOrder= inflater.inflate(R.layout.fragment_status_create_order, container, false);
        ButterKnife.bind(this, createOrder);


        HideEstimatedPrice();
        HideProgressBar();
        EndCreateButtonProgress();

        return createOrder;
    }



    public void setFromLocation(LocationData fromLoc) {
        if(fromLoc != null) {
            fromLocation = fromLoc;
            setFromText(fromLoc.getStringDescription());
        }
        else {
            fromLocation = null;
            setFromText("");
        }
    }

    public void setToLocation(LocationData toLoc) {
        if(toLoc != null) {
            toLocation = toLoc;
            setToText(toLoc.getStringDescription());
        }
        else {
            toLocation = null;
            setToText("");
        }
    }

    public void setFromText(String fromDescription) {
        this.fromText.setText(fromDescription);
    }

    public void setToText(String toDescription) {
        this.toText.setText(toDescription);
    }

    @OnClick(R.id.fragment_status_create_order_button)
    public void onCreateOrderButtonClick() {

//        String jjs = new Gson().toJson(CreateOrder());
//        Log.d("taxi5", "JSON: " + jjs);

        StartCreateButtonProgress();
//        createOrderBtn.setMode(ActionProcessButton.Mode.ENDLESS);
//        createOrderBtn.setProgress(1);

        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        Call<OrderResponseData> call = taxi5SDK.SendOrderRequest(TokenData.getInstance().getToken(), CreateOrder());

        call.enqueue(new Callback<OrderResponseData>() {
            @Override
            public void onResponse(Call<OrderResponseData> call, Response<OrderResponseData> response) {
                EndCreateButtonProgress();
                if (response.body().getStatusCode() == 201) {
                    OrderData order = response.body().getOrderData();
                    appData.setCurrentOrder(order);
                    FragmentMap.getMapFragment().RefreshView();
                } else {
                    Log.d("taxi5", "status code: " + response.body().getStatusCode());
                }
            }

            @Override
            public void onFailure(Call<OrderResponseData> call, Throwable t) {
                EndCreateButtonProgress();
//                createOrderBtn.setProgress(0);
                for (StackTraceElement element:t.getStackTrace()) {
                    Log.d("taxi5", "error:1 - " + element.toString());
                }
                Log.d("taxi5", "error create: " + t.getStackTrace());
            }
        });
    }


    public OrderData CreateOrder() {
        OrderData order = new OrderData();
        order.customerData = new CustomerData();
        order.customerData.name = ProfileData.getInstance().getName();
        order.customerData.msid = ProfileData.getInstance().getMsid();

        order.options= new OrderOptions();
        order.options.developer = true;

        if(fromLocation != null) {
            Log.d("taxi5", fromLocation.getStringDescription());
            order.from = fromLocation;
        }
        if(toLocation != null) {
            order.to = toLocation;
        }

        return order;
    }



    void SetCreateOrderButtonAvailableState(boolean state) {
        if(state) {
            createOrderButton.setClickable(true);
        }
        else {
            createOrderButton.setClickable(false);
        }
    }

    public void ShowEstimatedPrice(int price) {
        estimatedPriceLayout.setVisibility(View.VISIBLE);
    }


    public void HideEstimatedPrice() {
        estimatedPriceLayout.setVisibility(View.INVISIBLE);
    }

    void StartCreateButtonProgress() {
        SetCreateOrderButtonAvailableState(false);
        buttonProgressBar.setVisibility(View.VISIBLE);
    }

    void EndCreateButtonProgress() {
        SetCreateOrderButtonAvailableState(true);
        buttonProgressBar.setVisibility(View.INVISIBLE);
    }

    public void ShowProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        SetCreateOrderButtonAvailableState(false);

    }

    public void HideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
        SetCreateOrderButtonAvailableState(true);
    }
}
