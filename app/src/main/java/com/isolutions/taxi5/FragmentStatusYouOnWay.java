package com.isolutions.taxi5;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderStatusType;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class FragmentStatusYouOnWay extends StatusesBaseFragment {

    @BindView(R.id.fragment_status_you_on_way_timer_text_view)
    TextView timerTextView;

    @BindView(R.id.fragment_status_you_on_way_plate_text_view)
    TextView plateTextView;

    @BindView(R.id.fragment_status_you_on_way_car_text_view)
    TextView carTextView;

    private boolean isInitiated = false;
    Long orderStartTimeStamp;
    private boolean isTimerActive = false;
    private CountDownTimer refillOrderDataTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        super.onCreateView(inflater, container, savedInstanceState);

        View youOnWay = inflater.inflate(R.layout.fragment_status_you_on_way, container, false);
        ButterKnife.bind(this, youOnWay);

        isInitiated = true;
        return youOnWay;
    }

    void clearFields() {
        timerTextView.setText("");
        plateTextView.setText("");
        carTextView.setText("");
    }

    @Override
    public void fillWithOrder() {
        clearFields();
        if(AppData.getInstance().getCurrentOrder() != null) {
            OrderData order = AppData.getInstance().getCurrentOrder();

            if(order.vehicle != null) {
                if(order.vehicle.license_tax != null) {
                    plateTextView.setText(order.vehicle.license_tax);
                }
                if(order.vehicle.titleName != null && order.vehicle.colorName != null) {
                    carTextView.setText(order.vehicle.titleName + ", " + order.vehicle.colorName);
                }
            }
            if(order.events != null) {
                if(order.events.get("order_in_progress_at") != null) {
                    this.orderStartTimeStamp = order.events.get("order_in_progress_at");
                    updateTimer();
                }
            }
        }
        startTimer();
    }

    @Override
    public void onDetach() {
        if(refillOrderDataTimer != null) {
            if(isTimerActive) {
                refillOrderDataTimer.cancel();
            }
            refillOrderDataTimer = null;
        }
        isTimerActive = false;
        super.onDetach();
    }

    public void startTimer() {
        if(this.refillOrderDataTimer == null) {
            this.refillOrderDataTimer = new CountDownTimer(1000, 500) {
                public void onTick(long millisUntilFinished) {
                    updateTimer();
                }

                public void onFinish() {
                    isTimerActive = false;
                    updateTimer();

                    if (AppData.getInstance().getCurrentOrder() != null && isVisible()) {
                        if(AppData.getInstance().getCurrentOrder().status != null) {
                            if(AppData.getInstance().getCurrentOrder().status.status == OrderStatusType.OrderInProgress)
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

    public void updateTimer() {
        if(orderStartTimeStamp != null) {
            Long time = new Date().getTime()/1000L;
            Long err = orderStartTimeStamp - time;

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
}
