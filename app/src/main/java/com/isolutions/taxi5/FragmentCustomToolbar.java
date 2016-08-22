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
 * Created by fedar.trukhan on 22.08.16.
 */

//public class FragmentCustomToolbar {
//}

public class FragmentCustomToolbar extends Fragment {

    public MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d("taxi5", "create order");
        View customToolbar = inflater.inflate(R.layout.fragment_custom_toolbar, container, false);
        ButterKnife.bind(this, customToolbar);

        return customToolbar;
    }

    @OnClick(R.id.toolbar_menu_button)
    public void onLeftMenuOpenClick() {
        Log.d("taxi5", "open drawer");
        mainActivity.OpenLeftMenu();
    }


}
