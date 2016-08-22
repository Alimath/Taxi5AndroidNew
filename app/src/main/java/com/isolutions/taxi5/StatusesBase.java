package com.isolutions.taxi5;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;

import butterknife.ButterKnife;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class StatusesBase extends Fragment implements StatusesInterface {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void fillWithOrder(OrderData order) {

    }
}
