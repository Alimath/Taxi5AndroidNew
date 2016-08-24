package com.isolutions.taxi5;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fedar.trukhan on 23.08.16.
 */

public class MenuRight extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);

        View rightMenu= inflater.inflate(R.layout.left_drawer, container, false);
        ButterKnife.bind(this, rightMenu);



        return rightMenu;
    }

}
