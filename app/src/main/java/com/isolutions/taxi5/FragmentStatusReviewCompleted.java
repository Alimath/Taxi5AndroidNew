package com.isolutions.taxi5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fedar.trukhan on 30.08.16.
 */

public class FragmentStatusReviewCompleted extends StatusesBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_order_review_completed, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.fragment_status_order_review_completed_button)
    public void OnBackToMapClick() {
        AppData.getInstance().setCurrentOrder(null, false);
        FragmentMap.getMapFragment().RefreshView();
    }
}
