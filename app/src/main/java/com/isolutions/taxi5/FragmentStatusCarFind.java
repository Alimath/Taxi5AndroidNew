package com.isolutions.taxi5;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseActionData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import org.w3c.dom.Text;

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

public class FragmentStatusCarFind extends StatusesBaseFragment {
    @BindView(R.id.fragment_status_car_find_time_text_view)
    TextView timeTextView;

    @BindView(R.id.fragment_status_car_find_plate_text_view)
    TextView plateTextView;

    @BindView(R.id.fragment_status_car_find_car_text_view)
    TextView carTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);

        View findCar = inflater.inflate(R.layout.fragment_status_car_find, container, false);
        ButterKnife.bind(this, findCar);

        fillWithOrder();
        return findCar;
    }


    @OnClick(R.id.fragment_status_car_find_cancel_button)
    void OnCancelOrderBtnClick() {
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

    @OnClick(R.id.fragment_status_car_find_approve_button)
    void OnApproveOrderBtnClick() {
        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        Call<OrderResponseActionData> call = taxi5SDK.ConfirmOrderWithID(TokenData.getInstance().getToken(), appData.getCurrentOrder().id);

        call.enqueue(new Callback<OrderResponseActionData>() {
            @Override
            public void onResponse(Call<OrderResponseActionData> call, Response<OrderResponseActionData> response) {
                if(response.isSuccessful()) {
                    Log.d("taxi5", "Approve ok");
                }
                else {
                    Log.d("taxi5", "Approve error");
                }
            }

            @Override
            public void onFailure(Call<OrderResponseActionData> call, Throwable t) {
                Log.d("taxi5", "Approve error 2");
            }
        });
    }

    @Override
    public void fillWithOrder() {
        OrderData order = appData.getCurrentOrder();

        if(order == null) {
            return;
        }
        if(order.eta != null) {
            Long time = new Date().getTime()/1000L;
            Long err = order.eta - time;

            Long minutes = err / 60L;
            Long secundes = err - minutes*60L;
            if(secundes > 0) {
                minutes += 1;
            }

            timeTextView.setText(minutes + " " + appData.getAppContext().getString(R.string.minutes));
        }

        if(order.vehicle == null) {
            return;
        }
        if(!TextUtils.isEmpty(order.vehicle.license_tax)) {
            plateTextView.setText(order.vehicle.license_tax);
        }
        if(!TextUtils.isEmpty(order.vehicle.titleName)) {
            carTextView.setText(order.vehicle.titleName);
        }

    }
}
