package com.isolutions.taxi5;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);

        View carOnWay = inflater.inflate(R.layout.fragment_status_car_on_way, container, false);
        ButterKnife.bind(this, carOnWay);

        isInitiated = true;

        Date date = new Date();
        Log.d("taxi5", "" + date.getTime());

        return carOnWay;
    }

    @Override
    public void fillWithOrder() {
        OrderData order = appData.getCurrentOrder();
        if(order != null) {
            Log.d("taxi5", "fillWithOrder" + " " + order.vehicle.license_tax);
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
    }

    public void updateTimer() {
        if(etaTimeStamp != null) {
            Long time = new Date().getTime()/1000L;
            Long err = etaTimeStamp - time;

            Long minutes = err / 60L;
            Long secundes = err - minutes*60L;

            if(secundes < 0) {
                secundes = secundes * (-1L);
            }

            if(isInitiated) {
                timerTextView.setText(minutes+":"+secundes);
            }
        }
    }
}
