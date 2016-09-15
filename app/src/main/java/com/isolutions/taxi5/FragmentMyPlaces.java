package com.isolutions.taxi5;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Alimath on 15.09.16.
 */

public class FragmentMyPlaces extends Fragment {
    LayoutInflater inflater;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(AppData.getInstance().toolbar != null) {
            AppData.getInstance().toolbar.ConvertToDefaultWithTitle(getString(R.string.left_drawer_menu_item_plans));
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plans, container, false);
        ButterKnife.bind(this, view);
        this.inflater = inflater;
        if(AppData.getInstance().toolbar != null) {
            AppData.getInstance().toolbar.ConvertToDefaultWithTitle(getString(R.string.left_drawer_menu_item_plans));
        }

//        LoadAndFillPlans();
//
//        adapterPlans = new AdapterPlans(AppData.getInstance().getAppContext(), plansData);
//        listView.setAdapter(adapterPlans);
//
//        OnBYNButtonClick();

        return view;
    }
}
