package com.isolutions.taxi5;

import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.pinball83.maskededittext.MaskedEditText;
import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 02.08.16.
 */

public class FragmentLoginSMS extends Fragment {
    @BindView(R.id.fragment_login_sms_text_edit)
    EditText smsText;

    @BindView(R.id.fragment_login_sms_send_code_button)
    Button sendCodeButton;

    @BindView(R.id.fragment_login_sms_loading_button_sms_view)
    AVLoadingIndicatorView avLoadingIndicatorView;

    private CountDownTimer timer;
    private boolean isTimerFinish = false;

    private boolean noNeedUpdateTime = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View phoneFragment = inflater.inflate(R.layout.fragment_login_sms, container, false);
        ButterKnife.bind(this, phoneFragment);

        avLoadingIndicatorView.hide();
//        getSMSButton.setEnabled(false);
//        getSMSButton.setClickable(false);

        startTimer();
        return phoneFragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        timer.cancel();
    }

    @OnTextChanged(R.id.fragment_login_sms_text_edit)
    public void OnPhoneChanged(Editable editable) {
        if(smsText.getText().toString().length() > 0) {
            noNeedUpdateTime = true;
            EnableSendCodeButton();
            sendCodeButton.setText(getString(R.string.registration_sms_buttonText_code_entered));
        }
        else {
            noNeedUpdateTime = false;

            if(isTimerFinish) {
                timer.onFinish();
//                EnableSendCodeButton();
//                sendCodeButton.setText(getString(R.string.registration_sms_buttonText));
            }
        }
    }


    @OnClick(R.id.fragment_login_sms_send_code_button)
    public void OnSendSMSCodeClick() {
        StartGetSMSButtonLoadingAnimation();

        String code = smsText.getText().toString();

        ApplicationLauncher applicationLauncher = (ApplicationLauncher) getActivity().getApplicationContext();
        String numberStr = applicationLauncher.temp_login_phone;
        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }
        Call<TokenData> call = taxi5SDK.Authorization("friday_sms", numberStr, "taxi5_ios_app", "cri2thrauoau6whucizem8aukeo9traa", code);

        call.enqueue(new Callback<TokenData>() {
            @Override
            public void onResponse(Call<TokenData> call, Response<TokenData> response) {
                onResponseAuthorization(call, response);
            }

            @Override
            public void onFailure(Call<TokenData> call, Throwable t) {
                onFailureAuthorization(call, t);
            }
        });
    }


    public void startTimer() {
        this.timer = new CountDownTimer(30000, 500) {
            public void onTick(long millisUntilFinished) {
                if(!noNeedUpdateTime) {
                    sendCodeButton.setText(getString(R.string.registration_sms_buttonText) + " (" + (millisUntilFinished / 1000 + 1) + ")");
                    DisableSendCodeButton();
                }
            }

            public void onFinish() {
                if(!noNeedUpdateTime) {
                    sendCodeButton.setText(getString(R.string.registration_sms_buttonText));
                    EnableSendCodeButton();
                }
                isTimerFinish = true;
            }
        }.start();
        isTimerFinish = false;
    }

    private void EnableSendCodeButton() {
        sendCodeButton.setClickable(true);
        sendCodeButton.setTextColor(Color.parseColor("#FFFFFF"));
        sendCodeButton.setBackground(getMyDrawable(R.drawable.round_shape_blue_btn));
    }

    private void DisableSendCodeButton() {
        sendCodeButton.setClickable(false);
        sendCodeButton.setTextColor(Color.parseColor("#353535"));
        sendCodeButton.setBackground(getMyDrawable(R.drawable.round_shape_white_bordered_btn));
    }

    private Drawable getMyDrawable(int id) {
        int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return getActivity().getDrawable(id);
        } else {
            return getActivity().getResources().getDrawable(id);
        }
    }


    private void StartGetSMSButtonLoadingAnimation() {
        sendCodeButton.setClickable(false);
        smsText.setClickable(false);
        avLoadingIndicatorView.show();
    }

    private void StopGetSMSButtonLoadinganimation() {
        if(sendCodeButton.getCurrentTextColor() == Color.parseColor("#FFFFFF")) {
            sendCodeButton.setClickable(false);
            smsText.setClickable(false);
        }
        else {
            sendCodeButton.setClickable(true);
            smsText.setClickable(true);
        }
        avLoadingIndicatorView.hide();
    }


    public void onResponseAuthorization(Call<TokenData> call, Response<TokenData> response) {
        if(response.code() == 200) {
//            Log.d("taxi5", "response: " + response.body());
            response.body().setAuthorized(true);
            response.body().saveTokenData();
//            TokenData.getInstance().setAuthorized(true);


            LoginActivity loginActivity = (LoginActivity) getActivity();
            loginActivity.OpenMainActivity();

//            Log.d("taxi5", TokenData.getInstance().getDescription());

            Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
            if(taxi5SDK == null) {
                return;
            }
            Call<ProfileResponseData> profileDataCall = taxi5SDK.GetProfile(TokenData.getInstance().getType() + " " + TokenData.getInstance().getAccessToken());

            profileDataCall.enqueue(new Callback<ProfileResponseData>() {
                @Override
                public void onResponse(Call<ProfileResponseData> call, Response<ProfileResponseData> response) {
                    if (response.body().getStatusCode() == 200) {
                        LoginActivity loginActivity = (LoginActivity) getActivity();
                        response.body().getProfileData().saveProfileData();


                        loginActivity.OpenMainActivity();
                    }
                    else {
                        LoginActivity loginActivity = (LoginActivity) getActivity();
                        loginActivity.OpenNameFragment();
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponseData> call, Throwable t) {
                    Log.d("taxi5", "error to become profile");
                }
            });

//            LoginActivity loginActivity = (LoginActivity) getActivity();
//            loginActivity.OpenSMSFragment();
//
//            ApplicationLauncher applicationLauncher = (ApplicationLauncher) getActivity().getApplicationContext();
//            if(applicationLauncher != null) {
//                applicationLauncher.temp_login_phone = countryCodePicker.getSelectedCountryCode() + this.phoneEditText.getText();
//                Log.d("taxi5", applicationLauncher.temp_login_phone);
//            }
        }
        else {
            try {
                Log.d("taxi5", "responseCode: " + response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onFailureAuthorization(Call<TokenData> call, Throwable t) {
        Log.d("taxi5", "responseCode: error");
    }
}
