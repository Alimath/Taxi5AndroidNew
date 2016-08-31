package com.isolutions.taxi5;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseActionData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ReviewData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 30.08.16.
 */

public class FragmentStatusReview extends StatusesBaseFragment {
    FragmentStatusReviewStars cleannessFragment;
    FragmentStatusReviewStars civilityFragment;
    FragmentStatusReviewStars velocityFragment;

    @BindView(R.id.fragment_status_review_comment_edit_text)
    EditText commentEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status_order_review, container, false);

        ButterKnife.bind(this, view);

        cleannessFragment = (FragmentStatusReviewStars) getChildFragmentManager().findFragmentById(R.id.fragment_status_review_cleanness_stars_fragment);
        civilityFragment = (FragmentStatusReviewStars) getChildFragmentManager().findFragmentById(R.id.fragment_status_review_civility_stars_fragment);
        velocityFragment = (FragmentStatusReviewStars) getChildFragmentManager().findFragmentById(R.id.fragment_status_review_velocity_stars_fragment);

        fillWithOrder();

        return view;
    }

    @OnClick(R.id.fragment_status_review_send_review_button)
    public void OnSendReviewClick() {
        Log.d("taxi5", "begin send review");

        ReviewData reviewData = new ReviewData();

        reviewData.cleanliness = cleannessFragment.getValue();
        reviewData.civility = civilityFragment.getValue();
        reviewData.velocity = velocityFragment.getValue();
        reviewData.comment = commentEditText.getText().toString();

        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }
        Call<OrderResponseActionData> call = taxi5SDK.SendReview(TokenData.getInstance().getToken(),
                AppData.getInstance().getCurrentOrder().id, reviewData);

        call.enqueue(new Callback<OrderResponseActionData>() {
            @Override
            public void onResponse(Call<OrderResponseActionData> call, Response<OrderResponseActionData> response) {
                if(response.isSuccessful()) {
                    Log.d("taxi5", "response ok");
                    FragmentMap.getMapFragment().ShowReviewCompletedStatus();
                }
                else {
                    Log.d("taxi5", "req: " + call.request().body().toString());
                    Log.d("taxi5", "response error");
                }
            }

            @Override
            public void onFailure(Call<OrderResponseActionData> call, Throwable t) {
                Log.d("taxi5", "response failure");
            }
        });


    }

    @Override
    public void fillWithOrder() {
        CleanAll();
        if(AppData.getInstance().getCurrentOrder() != null && AppData.getInstance().getCurrentOrder().review != null) {
            ReviewData reviewData = AppData.getInstance().getCurrentOrder().review;

            if(reviewData.cleanliness != null) {
                cleannessFragment.setValue(reviewData.cleanliness);
                cleannessFragment.FillWithValue(reviewData.cleanliness);
            }

            if(reviewData.civility != null) {
                civilityFragment.setValue(reviewData.civility);
                civilityFragment.FillWithValue(reviewData.civility);
            }

            if(reviewData.velocity != null) {
                velocityFragment.setValue(reviewData.velocity);
                velocityFragment.FillWithValue(reviewData.velocity);
            }

            if(!TextUtils.isEmpty(reviewData.comment)) {
                commentEditText.setText(reviewData.comment);
            }
        }
    }

    void CleanAll() {
        cleannessFragment.setValue(5);
        civilityFragment.setValue(5);
        velocityFragment.setValue(5);
        commentEditText.setText("");
    }
}
