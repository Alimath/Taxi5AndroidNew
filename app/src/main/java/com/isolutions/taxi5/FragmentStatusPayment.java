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

public class FragmentStatusPayment extends StatusesBase {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);

        View paymentWaiting = inflater.inflate(R.layout.fragment_status_payment, container, false);
        ButterKnife.bind(this, paymentWaiting);

        return paymentWaiting;
    }

}