package com.isolutions.taxi5;

import android.app.Fragment;
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
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 27.07.16.
 */

public class FragmentLoginPhone extends Fragment
        implements Callback<Void> {
    @BindView(R.id.fragment_login_phone_text_edit) EditText phoneEditText;
    @BindView(R.id.fragment_login_phone_country_code_picker) CountryCodePicker countryCodePicker;
    @BindView(R.id.fragment_login_phone_button_getSMS) Button getSMSButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View phoneFragment = inflater.inflate(R.layout.fragment_login_phone, container, false);
        ButterKnife.bind(this, phoneFragment);

        this.DisableGetSMSButton();
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
        LoginActivity loginActivity = (LoginActivity) getActivity();
        loginActivity.OpenSMSFragment();

//        String numberStr = countryCodePicker.getSelectedCountryCode() + this.phoneEditText.getText();
//        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
//        Call<Void> call = taxi5SDK.GetSMSCode("friday_sms", numberStr, "taxi5_ios_app");
//        call.enqueue(this);

//            Call<TokenData> call = taxi5SDK.Authorization("friday_sms", "375447221174", "taxi5_ios_app", "cri2thrauoau6whucizem8aukeo9traa", "4512");
////            Call<Void> call = taxi5SDK.GetSMSCode("friday_sms", "375447221174", "taxi5_ios_app");
//            call.enqueue(this);
//        numberStr =  + numberStr;
//        Log.d("taxi5", "hallo phone");
    }

    @Override
    public void onResponse(Call<Void> call, Response<Void> response) {
        Log.d("taxi5", TokenData.getInstance().GetDescription());
        if(response.code() == 200) {
            Log.d("taxi5", "response: " + response.body());
        }
        else {
            try {
                Log.d("taxi5", "responseCode: " + response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onFailure(Call<Void> call, Throwable t) {
        Log.d("taxi5", "responseCode: error");
    }


    private void EnableGetSMSButton() {
        getSMSButton.setClickable(true);
        getSMSButton.setTextColor(Color.parseColor("#FFFFFF"));
        getSMSButton.setBackground(getMyDrawable(R.drawable.round_shape_blue_btn));
    }

    private void DisableGetSMSButton() {
        getSMSButton.setClickable(false);
        getSMSButton.setTextColor(Color.parseColor("#353535"));
        getSMSButton.setBackground(getMyDrawable(R.drawable.round_shape_white_bordered_btn));
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
