package com.isolutions.taxi5;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

//public class FragmentCustomToolbar {
//}

public class FragmentCustomToolbar extends Fragment {
//    public MainActivity mainActivity;
    @BindView(R.id.toolbar_orders_count_background)
    ImageView ordersCountBack;

    @BindView(R.id.toolbar_orders_count_text_view)
    TextView orderCountTextView;

    @BindView(R.id.toolbar_main_constraint)
    ConstraintLayout defaultToolbar;

    @BindView(R.id.toolbar_main_search_bar)
    ConstraintLayout searchToolbar;

    FragmentStatusCreateOrderFindAddress createOrderFindAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View customToolbar = inflater.inflate(R.layout.fragment_custom_toolbar, container, false);
        ButterKnife.bind(this, customToolbar);
        this.SetOrderCount(0);

        AppData.getInstance().toolbar = this;

        return customToolbar;
    }

    @OnClick(R.id.toolbar_right_menu_button)
    public void onRightMenuOpenClick() {
        if(AppData.getInstance().mainActivity != null) {
            AppData.getInstance().mainActivity.OpenRightMenu();
        }
    }

    @OnClick(R.id.toolbar_left_menu_button)
    public void onLeftMenuOpenClick() {
        if(AppData.getInstance().mainActivity != null) {
            AppData.getInstance().mainActivity.OpenLeftMenu();
        }
    }

    public void SetOrderCount(int ordersCount) {
        if(ordersCount > 0) {
            ordersCountBack.setVisibility(View.VISIBLE);
            orderCountTextView.setText(""+ordersCount);
        }
        else {
            ordersCountBack.setVisibility(View.INVISIBLE);
            orderCountTextView.setText("");
        }
    }

    public void ConvertToSearchBar() {
        defaultToolbar.setVisibility(View.INVISIBLE);
        searchToolbar.setVisibility(View.VISIBLE);
    }

    public void ConvertToDefaultToolbar() {
        defaultToolbar.setVisibility(View.VISIBLE);
        searchToolbar.setVisibility(View.INVISIBLE);
    }

    @OnTextChanged(R.id.toolbar_main_search_edit_text)
    public void onTextChanged(CharSequence text) {
        if(createOrderFindAddress != null) {
            if(text.length() > 2) {
                createOrderFindAddress.SearchAddressesWithString(text.toString());
            }
        }
    }


}
