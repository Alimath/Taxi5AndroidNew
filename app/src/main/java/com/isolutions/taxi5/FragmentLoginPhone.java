package com.isolutions.taxi5;

import android.support.v4.app.Fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;
import com.wang.avi.AVLoadingIndicatorView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 27.07.16.
 */

public class FragmentLoginPhone extends Fragment {
    @BindView(R.id.fragment_login_phone_text_edit) EditText phoneEditText;
    @BindView(R.id.fragment_login_phone_country_code_picker) CountryCodePicker countryCodePicker;
    @BindView(R.id.fragment_login_phone_button_getSMS) Button getSMSButton;
    @BindView(R.id.fragment_login_loading_button_sms_view) AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View phoneFragment = inflater.inflate(R.layout.fragment_login_phone, container, false);
        ButterKnife.bind(this, phoneFragment);

        avLoadingIndicatorView.hide();
        this.DisableGetSMSButton();

        TokenData tData = TokenData.getInstance();

        Log.d("taxi5", tData.getDescription());

        return phoneFragment;
    }

    @OnTextChanged(R.id.fragment_login_phone_text_edit)
    public void OnPhoneChanged(Editable editable) {
        String numberStr = editable.toString();
        numberStr = countryCodePicker.getSelectedCountryCodeWithPlus() + numberStr;
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(numberStr, "US");
//            Since you know the country you can format it as follows:
            String formatedPhone = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);

            if(phoneUtil.isValidNumber(numberProto)) {
                this.EnableGetSMSButton();
            }
            else {
                this.DisableGetSMSButton();
            }

//            this.phoneEditText.setText(formatedPhone.replace(countryCodePicker.getSelectedCountryCodeWithPlus(), ""));

        } catch (NumberParseException e) {
            this.DisableGetSMSButton();
//            System.err.println("NumberParseException was thrown: " + e.toString());
        }
    }

    @OnClick(R.id.fragment_login_phone_button_getSMS)
    public void OnGetSMSClick() {
        this.StartGetSMSButtonLoadingAnimation();

        String numberStr = countryCodePicker.getSelectedCountryCode() + this.phoneEditText.getText();
        LoginActivity loginActivity = (LoginActivity) getActivity();
        loginActivity.phoneNumber = numberStr;

        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }
        Call<Void> call = taxi5SDK.GetSMSCode("friday_sms", numberStr, AppData.client_id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                onResponseCallback(call, response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                onFailureCallback(call, t);
            }
        });
    }

    public void onResponseCallback(Call<Void> call, Response<Void> response) {
        StopGetSMSButtonLoadinganimation();
//        Log.d("taxi5", TokenData.getInstance().GetDescription());
        if(response.isSuccessful()) {
            Log.d("taxi5", "response: " + response.body());

            LoginActivity loginActivity = (LoginActivity) getActivity();
            loginActivity.OpenSMSFragment();

            ApplicationLauncher applicationLauncher = (ApplicationLauncher) getActivity().getApplicationContext();
            if(applicationLauncher != null) {
                applicationLauncher.temp_login_phone = countryCodePicker.getSelectedCountryCode() + this.phoneEditText.getText();
                Log.d("taxi5", applicationLauncher.temp_login_phone);
            }
        }
        else {
            try {
                LoginActivity loginActivity = (LoginActivity) getActivity();
                loginActivity.OpenNameFragment();
                Log.d("taxi5", "responseCode: " + response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onFailureCallback(Call<Void> call, Throwable t) {
        StopGetSMSButtonLoadinganimation();
        Log.d("taxi5", "responseCode: error");
    }


    private void EnableGetSMSButton() {
        getSMSButton.setClickable(true);
        getSMSButton.setTextColor(Color.parseColor("#FFFFFF"));
        getSMSButton.setBackground(getMyDrawable(R.drawable.round_shape_blue_btn));
    }

    private void DisableGetSMSButton() {
        getSMSButton.setClickable(false);
        phoneEditText.setClickable(false);
        getSMSButton.setTextColor(Color.parseColor("#353535"));
        getSMSButton.setBackground(getMyDrawable(R.drawable.round_shape_white_bordered_btn));
    }

    private void StartGetSMSButtonLoadingAnimation() {
        getSMSButton.setClickable(false);
        phoneEditText.setClickable(false);
        avLoadingIndicatorView.show();
    }

    private void StopGetSMSButtonLoadinganimation() {
        if(getSMSButton.getCurrentTextColor() == Color.parseColor("#FFFFFF")) {
            getSMSButton.setClickable(false);
            phoneEditText.setClickable(false);
        }
        else {
            getSMSButton.setClickable(true);
            phoneEditText.setClickable(true);
        }
        avLoadingIndicatorView.hide();
    }

    private Drawable getMyDrawable(int id) {
        int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return getActivity().getDrawable(id);
        } else {
            return getActivity().getResources().getDrawable(id);
        }
    }


}
