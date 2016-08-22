package com.isolutions.taxi5;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class FragmentStatusSearchCar extends StatusesBase {

    @BindView(R.id.fragment_status_search_car_approximate_price_layout)
    LinearLayout estimatedPriceLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);

        View searchCar = inflater.inflate(R.layout.fragment_status_search_car, container, false);
        ButterKnife.bind(this, searchCar);


        HideEstimatedPrice();

        return searchCar;
    }


    public void ShowEstimatedPrice(int price) {
        estimatedPriceLayout.setVisibility(View.VISIBLE);
    }


    public void HideEstimatedPrice() {
        estimatedPriceLayout.setVisibility(View.INVISIBLE);
    }
}
