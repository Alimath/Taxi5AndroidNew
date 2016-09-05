package com.isolutions.taxi5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationsListResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseActionData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderStatusType;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 26.08.16.
 */

public class FragmentStatusInformation extends StatusesBaseFragment  {
    @BindView(R.id.fragment_status_information_title_text_view)
    TextView titleTextView;

    @BindView(R.id.fragment_status_information_header_text_view)
    TextView headerTextView;

    @BindView(R.id.fragment_status_information_message_text_view)
    TextView messageTextView;

    @BindView(R.id.fragment_status_information_button)
    Button button;

    @BindView(R.id.fragment_status_information_button_progress_bar)
    AVLoadingIndicatorView buttonProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View information = inflater.inflate(R.layout.fragment_status_information, container, false);
        ButterKnife.bind(this, information);

        HideProgressBar();

        return information;
    }

    @Override
    public void fillWithOrder() {
        OrderData order = appData.getCurrentOrder();
        if(order != null && order.status != null && order.status.status != null) {
            if(order.status.status == OrderStatusType.CarNotFound) {
                button.setText(getString(R.string.status_information_repeat_order_button_text));
                titleTextView.setText(getString(R.string.status_information_title));
                headerTextView.setText(getString(R.string.status_information_no_cars_header));
                messageTextView.setText(getString(R.string.status_information_try_to_repeat_order));
            }
            if(order.status.status == OrderStatusType.ClientApproveTimeout) {
                button.setText(getString(R.string.status_information_repeat_order_button_text));
                titleTextView.setText(getString(R.string.status_information_title));
                headerTextView.setText(getString(R.string.status_information_timeout_header));
                messageTextView.setText(getString(R.string.status_information_try_to_repeat_order));
            }
        }
    }

    void SetCreateOrderButtonAvailableState(boolean state) {
        if(state) {
            button.setClickable(true);
        }
        else {
            button.setClickable(false);
        }
    }

    void StartCreateButtonProgress() {
        SetCreateOrderButtonAvailableState(false);
        ShowProgressBar();
    }

    void EndCreateButtonProgress() {
        SetCreateOrderButtonAvailableState(true);
        HideProgressBar();
    }

    public void ShowProgressBar() {
        buttonProgressBar.setVisibility(View.VISIBLE);
        SetCreateOrderButtonAvailableState(false);

    }

    public void HideProgressBar() {
        buttonProgressBar.setVisibility(View.INVISIBLE);
        SetCreateOrderButtonAvailableState(true);
    }

    @OnClick(R.id.fragment_status_information_button)
    void OnButtonClick() {
        ShowProgressBar();
        OrderData order = appData.getCurrentOrder();
        if(order != null && order.status != null && order.status.status != null) {
            if (order.status.status == OrderStatusType.CarNotFound) {
                Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
                if(taxi5SDK == null) {
                    return;
                }
                Call<OrderResponseActionData> call = taxi5SDK.RepeatOrder(TokenData.getInstance().getToken(), order.id);

                call.enqueue(new Callback<OrderResponseActionData>() {
                    @Override
                    public void onResponse(Call<OrderResponseActionData> call, Response<OrderResponseActionData> response) {
                        HideProgressBar();
                        if(FragmentMap.getMapFragment() != null) {
                            FragmentMap.getMapFragment().ReadOrderState();
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderResponseActionData> call, Throwable t) {
                        HideProgressBar();
                    }
                });
            }
        }
    }
}
