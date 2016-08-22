package com.isolutions.taxi5;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class FragmentStatusCarDelivered extends StatusesBase {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);

        View carDelivered = inflater.inflate(R.layout.fragment_status_car_delivered, container, false);
        ButterKnife.bind(this, carDelivered);

        return carDelivered;
    }

}
