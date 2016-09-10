package com.isolutions.taxi5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class FragmentStatusOrderComplete extends StatusesBaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        super.onCreateView(inflater, container, savedInstanceState);

        View orderComplete = inflater.inflate(R.layout.fragment_status_order_complete, container, false);
        ButterKnife.bind(this, orderComplete);

        return orderComplete;
    }

    @OnClick(R.id.fragment_status_order_complete_review_button)
    public void ReviewButton() {
        if(FragmentMap.getMapFragment() != null) {
            FragmentMap.getMapFragment().ShowReviewStatus();
        }
    }

    @OnClick(R.id.fragment_status_order_complete_main_fade)
    public void OnFadeClick() {
        if(AppData.getInstance().mainActivity != null) {
            AppData.getInstance().mainActivity.OpenClearMap();
        }
    }

}
