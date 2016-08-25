package com.isolutions.taxi5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseActionData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderStatusType;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class FragmentStatusCarOnWay extends StatusesBaseFragment {

    @BindView(R.id.fragment_status_car_on_way_timer_text_view)
    TextView timerTextView;

    @BindView(R.id.fragment_status_car_on_way_plate_text_view)
    TextView plateTextView;

    @BindView(R.id.fragment_status_car_on_way_car_text_view)
    TextView carTextView;

    private boolean isInitiated = false;

    private Long etaTimeStamp;

    private CountDownTimer refillOrderDataTimer;

    private boolean isTimerActive = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);

        View carOnWay = inflater.inflate(R.layout.fragment_status_car_on_way, container, false);
        ButterKnife.bind(this, carOnWay);

        isInitiated = true;

        Date date = new Date();
        Log.d("taxi5", "" + date.getTime());

        fillWithOrder();

        return carOnWay;
    }

    @Override
    public void onDetach() {
        refillOrderDataTimer.cancel();
        refillOrderDataTimer = null;
        super.onDetach();
    }

    public void startTimer() {
        if(this.refillOrderDataTimer == null) {
            this.refillOrderDataTimer = new CountDownTimer(30000, 500) {
                public void onTick(long millisUntilFinished) {
                    updateTimer();
                }

                public void onFinish() {
                    isTimerActive = false;
                    updateTimer();

                    if (AppData.getInstance().getCurrentOrder() != null && isVisible()) {
                        if(AppData.getInstance().getCurrentOrder().status != null) {
                            if(AppData.getInstance().getCurrentOrder().status.status == OrderStatusType.CarDelivering)
                                startTimer();
                        }
                    }
                }
            };
        }
        else {
            if(!isTimerActive) {
                isTimerActive = true;
                refillOrderDataTimer.start();
            }
        }
    }

    @Override
    public void fillWithOrder() {
        OrderData order = appData.getCurrentOrder();
        if(order != null) {
            if (isInitiated) {
                if (order.vehicle != null) {
                    if (order.vehicle.license_tax != null) {
                        plateTextView.setText(order.vehicle.license_tax);
                    }
                    if (order.vehicle.titleName != null) {
                        carTextView.setText(order.vehicle.titleName);
                    }
                }
                if (order.eta != null) {
                    etaTimeStamp = order.eta;
                    updateTimer();
                }
            }
        }
        startTimer();
    }

    public void updateTimer() {
        Log.d("taxi5", "update timer");
        if(etaTimeStamp != null) {
            Long time = new Date().getTime()/1000L;
            Long err = etaTimeStamp - time;

            if(err < 0) {
                timerTextView.setTextColor(appData.getColor(R.color.defaultRed));
            }
            else {
                timerTextView.setTextColor(appData.getColor(R.color.defaultBlack));
            }

            Long minutes = err / 60L;
            Long secundes = err - minutes*60L;

            minutes = Math.abs(minutes);
            secundes = Math.abs(secundes);

            String minutesString = ""+minutes;
            String secundesString = ""+secundes;

            if(minutes < 10) {
                minutesString = "0"+minutesString;
            }
            if(secundes < 10) {
                secundesString = "0"+secundesString;
            }

            if(isInitiated) {
                timerTextView.setText(minutesString+":"+secundesString);
            }
        }
    }

    @OnClick(R.id.fragment_status_car_on_way_call_to_driver)
    public void CallToDriverBtnClick() {
        if(appData.getCurrentOrder().driver == null || appData.getCurrentOrder().driver.driverPhone == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + appData.getCurrentOrder().driver.driverPhone));
        startActivity(intent);
    }

    @OnClick(R.id.fragment_status_car_on_way_cancel_button)
    public void CancelOrderBtnClick() {
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
