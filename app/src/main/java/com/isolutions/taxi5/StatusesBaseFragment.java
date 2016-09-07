package com.isolutions.taxi5;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class StatusesBaseFragment extends Fragment implements StatusesInterface {
//    private ApplicationLauncher app = (ApplicationLauncher) getActivity().getApplication();
    AppData appData = AppData.getInstance();
//    FragmentMap parentFragment = FragmentMap.getMapFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void fillWithOrder() {
    }

    @Override
    public void onStart() {
        super.onStart();
        fillWithOrder();
    }
}
