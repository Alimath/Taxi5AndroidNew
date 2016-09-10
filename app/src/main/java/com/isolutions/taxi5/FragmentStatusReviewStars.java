package com.isolutions.taxi5;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by fedar.trukhan on 30.08.16.
 */

public class FragmentStatusReviewStars extends Fragment {

    @BindView(R.id.fragment_status_review_stars_star_1_button)
    Button star1;
    @BindView(R.id.fragment_status_review_stars_star_2_button)
    Button star2;
    @BindView(R.id.fragment_status_review_stars_star_3_button)
    Button star3;
    @BindView(R.id.fragment_status_review_stars_star_4_button)
    Button star4;
    @BindView(R.id.fragment_status_review_stars_star_5_button)
    Button star5;

    private int value = 5;
    public int getValue() {
        return this.value;
    }
    public void setValue(int value) {
        this.value = value;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_order_review_stars, container, false);
        ButterKnife.bind(this, view);

        FillWithValue(value);
        return view;
    }

    @Optional
    @OnClick({R.id.fragment_status_review_stars_star_1_button, R.id.fragment_status_review_stars_star_2_button,
            R.id.fragment_status_review_stars_star_3_button, R.id.fragment_status_review_stars_star_4_button,
            R.id.fragment_status_review_stars_star_5_button,
            R.id.fragment_status_review_stars_star_1_button_help, R.id.fragment_status_review_stars_star_2_button_help,
            R.id.fragment_status_review_stars_star_3_button_help, R.id.fragment_status_review_stars_star_4_button_help,
            R.id.fragment_status_review_stars_star_5_button_help})
    public void OnStarClick(View view) {
        Log.d("taxi5", "tag: "+view.getTag().toString());

        final String btn1Tag = "1";
        final String btn2Tag = "2";
        final String btn3Tag = "3";
        final String btn4Tag = "4";
        final String btn5Tag = "5";

        if(view.getTag() != null) {
            switch (view.getTag().toString()) {
                case btn1Tag:
                    value = 1;
                    FillWithValue(1);
                    break;
                case btn2Tag:
                    value = 2;
                    FillWithValue(2);
                    break;
                case btn3Tag:
                    value = 3;
                    FillWithValue(3);
                    break;
                case btn4Tag:
                    value = 4;
                    FillWithValue(4);
                    break;
                case btn5Tag:
                    value = 5;
                    FillWithValue(5);
                    break;
            }
        }

    }

    void FillWithValue(int value) {
        ResetEvals();
        if(value > 4) {
            star5.setBackgroundResource(R.drawable.review_full_star);
        }
        if(value > 3) {
            star4.setBackgroundResource(R.drawable.review_full_star);
        }
        if(value > 2) {
            star3.setBackgroundResource(R.drawable.review_full_star);
        }
        if(value > 1) {
            star2.setBackgroundResource(R.drawable.review_full_star);
        }
    }

    void ResetEvals() {
        star1.setBackgroundResource(R.drawable.review_full_star);
        star2.setBackgroundResource(R.drawable.review_clear_star);
        star3.setBackgroundResource(R.drawable.review_clear_star);
        star4.setBackgroundResource(R.drawable.review_clear_star);
        star5.setBackgroundResource(R.drawable.review_clear_star);
    }
}
