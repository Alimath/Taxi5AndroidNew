package com.isolutions.taxi5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class FragmentStatusCarDelivered extends StatusesBaseFragment {

    @BindView(R.id.fragment_status_car_delivered_plate_text_view)
    TextView plateTextView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        super.onCreateView(inflater, container, savedInstanceState);

        View carDelivered = inflater.inflate(R.layout.fragment_status_car_delivered, container, false);
        ButterKnife.bind(this, carDelivered);

        return carDelivered;
    }

    @Override
    public void fillWithOrder() {
        super.fillWithOrder();
        if (appData.getCurrentOrder() != null) {
            OrderData order = appData.getCurrentOrder();
            if(order.vehicle != null && order.vehicle.license_tax != null) {
                this.plateTextView.setText(order.vehicle.license_tax);
            }
        }
    }

    @OnClick(R.id.fragment_status_car_delivered_call_to_driver)
    public void CallToDriverBtnClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.status_car_on_way_call_driver_dialog_message).setTitle(R.string.status_car_on_way_call_driver_dialog_title);
        builder.setPositiveButton(R.string.status_car_on_way_call_driver_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(appData.getCurrentOrder().driver == null || appData.getCurrentOrder().driver.driverPhone == null) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:+" + appData.getCurrentOrder().driver.driverPhone));
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.status_car_on_way_call_driver_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
