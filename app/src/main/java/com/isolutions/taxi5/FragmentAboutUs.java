package com.isolutions.taxi5;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fedar.trukhan on 30.08.16.
 */

public class FragmentAboutUs extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(AppData.getInstance().toolbar != null) {
            AppData.getInstance().toolbar.ConvertToDefaultWithTitle(getString(R.string.left_drawer_menu_item_about));
        }
    }

    @OnClick(R.id.fragment_about_us_fb_button)
    public void OnFBButtonClick() {
        AppData.getInstance().openWebPage("https://www.facebook.com/taxi5by");
    }

    @OnClick(R.id.fragment_about_us_tw_button)
    public void OnTWButtonClick() {
        AppData.getInstance().openWebPage("https://twitter.com/taxi5by");
    }

    @OnClick(R.id.fragment_about_us_instagram_button)
    public void OnInstagramButtonClick() {
        AppData.getInstance().openWebPage("https://www.instagram.com/taxi5by");
    }

    @OnClick(R.id.fragment_about_vk_button)
    public void OnVKButtonClick() {
        AppData.getInstance().openWebPage("https://vk.com/taxi5by");
    }

    @OnClick(R.id.fragment_about_us_youtube_button)
    public void OnYoutubeButtonClick() {
        AppData.getInstance().openWebPage("https://www.youtube.com/channel/UCFBs-iCA3o48MZlATEMVFLQ");
    }

    @OnClick(R.id.fragment_about_us_review_button)
    public void OnRateAppButtonClick() {
        AppData.getInstance().openWebPage("https://play.google.com/store/apps/details?id=com.isolutions.taxi5");
    }
}
