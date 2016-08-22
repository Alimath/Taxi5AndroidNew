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

public class FragmentStatusOrderComplete extends StatusesBase {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);

        View orderComplete = inflater.inflate(R.layout.fragment_status_order_complete, container, false);
        ButterKnife.bind(this, orderComplete);

        return orderComplete;
    }

}
