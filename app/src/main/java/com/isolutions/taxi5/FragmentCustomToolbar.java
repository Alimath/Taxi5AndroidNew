package com.isolutions.taxi5;

import android.content.Context;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

//public class FragmentCustomToolbar {
//}

public class FragmentCustomToolbar extends Fragment {
//    public MainActivity mainActivity;

    @BindView(R.id.toolbar_main)
    ConstraintLayout toolbarMain;

    @BindView(R.id.toolbar_orders_count_background)
    ImageView ordersCountBack;

    @BindView(R.id.toolbar_orders_count_text_view)
    TextView orderCountTextView;

    @BindView(R.id.toolbar_main_constraint)
    ConstraintLayout defaultToolbar;

    @BindView(R.id.toolbar_main_search_bar)
    ConstraintLayout searchToolbar;

    @BindView(R.id.toolbar_main_search_edit_text)
    EditText searchEditText;

    @BindView(R.id.toolbar_logo_image)
    ImageView toolbarLogo;

    @BindView(R.id.toolbar_title)
    TextView titleTextView;

    @BindView(R.id.fragment_toolbar_fade)
    View loadingFade;

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
        searchEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) AppData.getInstance().currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void ConvertToDefaultToolbar() {
        defaultToolbar.setVisibility(View.VISIBLE);
        searchToolbar.setVisibility(View.INVISIBLE);
        searchEditText.setText("");

        this.titleTextView.setVisibility(View.INVISIBLE);
        this.toolbarLogo.setVisibility(View.VISIBLE);

        if(AppData.getInstance().mainActivity != null) {
            try {
                InputMethodManager inputManager = (InputMethodManager) AppData.getInstance().mainActivity.getSystemService(INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(AppData.getInstance().mainActivity.getCurrentFocus().getWindowToken(), 0);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void ConvertToDefaultWithTitle(String title) {
        this.ConvertToDefaultToolbar();
        this.toolbarLogo.setVisibility(View.INVISIBLE);
        this.titleTextView.setVisibility(View.VISIBLE);

        this.titleTextView.setText(title);
    }

    @OnTextChanged(R.id.toolbar_main_search_edit_text)
    public void onTextChanged(CharSequence text) {
        if(createOrderFindAddress != null) {
            if(text.length() > 0) {
                createOrderFindAddress.SearchAddressesWithString(text.toString());
            }
        }
    }

    public void HideAnimated() {
        if(this.isAdded() && !isHidden) {
            isHidden = true;
            this.toolbarMain.animate().translationY(AppData.getInstance().dpToPx(-56)).setDuration(300).start();
        }
    }

    private boolean isHidden = false;
    public void ShowAnimated() {
        if(this.isAdded() && isHidden) {
            isHidden = false;
            this.toolbarMain.animate().translationY(0).setDuration(300).start();
        }
    }

//    public void ConvertToHiddenToolbar() {
//        toolbarMain.setVisibility(View.INVISIBLE);
//        ViewGroup.LayoutParams params = toolbarMain.getLayoutParams();
//        params.height = 0;
//        toolbarMain.setLayoutParams(params);
//    }


    public void ShowLoadingFade() {
        loadingFade.setVisibility(View.VISIBLE);
    }
    public void HideLoadingFade() {
        loadingFade.setVisibility(View.INVISIBLE);
    }
}
