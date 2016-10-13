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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseActionData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;
import com.wang.avi.AVLoadingIndicatorView;

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

//    @BindView(R.id.fragment_status_search_car_rotation_image)
//    ImageView rotatedImage;


    @BindView(R.id.fragment_status_search_car_cancel_button)
    Button cancelBtn;

    @BindView(R.id.fragment_status_search_car_cancel_button_progress_bar)
    AVLoadingIndicatorView buttonCancelProgressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View searchCar = inflater.inflate(R.layout.fragment_status_search_car, container, false);
        ButterKnife.bind(this, searchCar);

        startRotateSearchCircleAnimation();

//        rotatedImage.setVisibility(View.INVISIBLE);

        HideEstimatedPrice();
        HideCancelProgressBar();

        fillWithOrder();

        return searchCar;
    }

    @Override
    public void onDetach() {
//        stopRotateSearchCircleAnimation();
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
            else {
                fromLoc.setText("");
            }
        }
        if(order != null && order.to != null) {
            toLoc.setText(order.to.getStringDescription());
        }
        else {
            toLoc.setText(getString(R.string.no_address_string));
            toLoc.setTextColor(AppData.getInstance().getColor(R.color.hintsColor));
        }
    }



    private void startRotateSearchCircleAnimation() {
        RotateAnimation anim = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(2000);

// Start animating the image
        //final ImageView splash = (ImageView) findViewById(R.id.fragment_status_search_car_rotation_image);
//        if(rotatedImage != null) {
//            rotatedImage.startAnimation(anim);
//        }
    }

//    private void stopRotateSearchCircleAnimation() {
//        rotatedImage.setAnimation(null);
//    }

    @OnClick(R.id.fragment_status_search_car_cancel_button)
    void OnCancelOrderBtnClick() {
        ShowCancelProgressBar();

        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }
        Call<OrderResponseActionData> call = taxi5SDK.CancelOrderWithID(TokenData.getInstance().getToken(), appData.getCurrentOrder().id);

        call.enqueue(new Callback<OrderResponseActionData>() {
            @Override
            public void onResponse(Call<OrderResponseActionData> call, Response<OrderResponseActionData> response) {
                HideCancelProgressBar();
                if (response.isSuccessful()) {
                    appData.setCurrentOrder(FragmentMap.getMapFragment().statusCreateOrderFragment.CreateOrder(), true);
                    FragmentMap.getMapFragment().RefreshView();
                }
            }

            @Override
            public void onFailure(Call<OrderResponseActionData> call, Throwable t) {
                HideCancelProgressBar();
                Log.d("taxi5", "error to cancel order: " + t.getLocalizedMessage());
            }
        });
    }

    void SetCancelBtnAvailableState(boolean state) {
        cancelBtn.setClickable(state);
    }

    public void ShowCancelProgressBar() {
        buttonCancelProgressBar.setVisibility(View.VISIBLE);
        cancelBtn.setText("");
        SetCancelBtnAvailableState(false);

    }

    public void HideCancelProgressBar() {
        cancelBtn.setText(getString(R.string.status_search_car_button_text));
        buttonCancelProgressBar.setVisibility(View.INVISIBLE);
        SetCancelBtnAvailableState(true);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        if(AppData.getInstance().mainActivity != null) {
//            AppData.getInstance().mainActivity.HideToolbar();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if(AppData.getInstance().mainActivity != null) {
//            AppData.getInstance().mainActivity.ShowToolbar();
//        }
//    }
}
