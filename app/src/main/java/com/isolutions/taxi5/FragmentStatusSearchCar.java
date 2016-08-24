package com.isolutions.taxi5;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseActionData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class FragmentStatusSearchCar extends StatusesBaseFragment {

    @BindView(R.id.fragment_status_search_car_approximate_price_layout)
    LinearLayout estimatedPriceLayout;

    @BindView(R.id.fragment_status_search_car_from_text)
    EditText fromLoc;

    @BindView(R.id.fragment_status_search_car_to_text)
    EditText toLoc;

    @BindView(R.id.fragment_status_search_car_rotation_image)
    ImageView rotatedImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View searchCar = inflater.inflate(R.layout.fragment_status_search_car, container, false);
        ButterKnife.bind(this, searchCar);

        startRotateSearchCircleAnimation();


        HideEstimatedPrice();

        fillWithOrder();

        return searchCar;
    }

    @Override
    public void onDetach() {
        stopRotateSearchCircleAnimation();
        super.onDetach();
    }

    public void ShowEstimatedPrice(int price) {
        estimatedPriceLayout.setVisibility(View.VISIBLE);
    }


    public void HideEstimatedPrice() {
        estimatedPriceLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void fillWithOrder() {
        OrderData order = appData.getCurrentOrder();
        if(order != null) {
            if(order.from != null) {
                fromLoc.setText(order.from.getStringDescription());
            }
        }
        if(order != null && order.to != null) {
            toLoc.setText(order.to.getStringDescription());
        }
    }



    private void startRotateSearchCircleAnimation() {
        RotateAnimation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(2000);

// Start animating the image
        //final ImageView splash = (ImageView) findViewById(R.id.fragment_status_search_car_rotation_image);
        if(rotatedImage != null) {
            rotatedImage.startAnimation(anim);
        }
    }

    private void stopRotateSearchCircleAnimation() {
        rotatedImage.setAnimation(null);
    }

    @OnClick(R.id.fragment_status_search_car_cancel_button)
    void OnCancelOrderBtnClick() {
        Log.d("taxi5", "try to cancel order");
        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        Call<OrderResponseActionData> call = taxi5SDK.CancelOrderWithID(TokenData.getInstance().getToken(), appData.getCurrentOrder().id);

        call.enqueue(new Callback<OrderResponseActionData>() {
            @Override
            public void onResponse(Call<OrderResponseActionData> call, Response<OrderResponseActionData> response) {
                if (response.isSuccessful()) {
                    appData.setCurrentOrder(null);
                    FragmentMap.getMapFragment().RefreshView();
                }
            }

            @Override
            public void onFailure(Call<OrderResponseActionData> call, Throwable t) {
                Log.d("taxi5", "error to cancel order: " + t.getLocalizedMessage());
            }
        });
    }

}
