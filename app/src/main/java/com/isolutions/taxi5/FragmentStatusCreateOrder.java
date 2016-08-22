package com.isolutions.taxi5;

/**
 * Created by fedar.trukhan on 22.08.16.
 */


import android.app.Fragment;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentStatusCreateOrder extends StatusesBase  {

    @BindView(R.id.fragment_status_create_order_comment_edit_text)
    EditText commentEditText;

    @BindView(R.id.fragment_status_create_order_approximate_price_layout)
    LinearLayout estimatedPriceLayout;

    @BindView(R.id.fragment_status_create_order_from_text)
    TextView fromText;

    @BindView(R.id.fragment_status_create_order_to_text)
    TextView toText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);

        Log.d("taxi5", "create order");
        View createOrder= inflater.inflate(R.layout.fragment_status_create_order, container, false);
        ButterKnife.bind(this, createOrder);


        HideEstimatedPrice();

        return createOrder;
    }


    public void ShowEstimatedPrice(int price) {
        estimatedPriceLayout.setVisibility(View.VISIBLE);
    }


    public void HideEstimatedPrice() {
        estimatedPriceLayout.setVisibility(View.INVISIBLE);
    }


    public void setFromText(String fromDescription) {
        this.fromText.setText(fromDescription);
    }

    public void setToText(String toDescription) {
        this.toText.setText(toDescription);
    }

    @OnClick(R.id.fragment_status_create_order_button)
    public void onCreateOrderButtonClick() {
//        FragmentMap fragmentMap = ((MainActivity) getActivity()).mapFragment;
//
//        fragmentMap.changeStatus(fragmentMap.statusCreateOrderFragment);

//        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
//        Call<ProfileResponseData> profileDataCall = taxi5SDK.GetProfile(TokenData.getInstance().getType() + " " + TokenData.getInstance().getAccessToken());

//        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
//        Call<OrderResponseData> call = taxi5SDK.ReadOrderStatus(TokenData.getInstance().getType()+" "+TokenData.getInstance().getAccessToken(), 609020);
//
//        call.enqueue(new Callback<OrderResponseData>() {
//            @Override
//            public void onResponse(Call<OrderResponseData> call, Response<OrderResponseData> response) {
//                OrderData order = response.body().getOrderData();
//                Log.d("taxi5", order.id + " " +
//                        order.status.status + " " +
//                        order.to.locationDescription.address.street + " " +
//                        order.vehicle.titleName + " " +
//                        order.driver.driverPhone + " " +
//                        order.comment);
//            }
//
//            @Override
//            public void onFailure(Call<OrderResponseData> call, Throwable t) {
//
//            }
//        });

    }

}
