package com.isolutions.taxi5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class FragmentStatusYouOnWay extends StatusesBaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        super.onCreateView(inflater, container, savedInstanceState);

        View youOnWay = inflater.inflate(R.layout.fragment_status_you_on_way, container, false);
        ButterKnife.bind(this, youOnWay);

        return youOnWay;
    }

}
