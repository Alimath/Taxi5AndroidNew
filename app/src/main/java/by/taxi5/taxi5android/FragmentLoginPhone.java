package by.taxi5.taxi5android;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Debug;
import android.os.SystemClock;
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
import by.taxi5.taxi5android.API.ApiFactory;
import by.taxi5.taxi5android.API.Taxi5SDK;
import by.taxi5.taxi5android.API.Taxi5SDKEntity.TokenData;
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

//        getSMSButton.setEnabled(false);
        getSMSButton.setClickable(false);
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
                getSMSButton.setClickable(true);
//                getSMSButton.setEnabled(true);
            }
            else {
//                getSMSButton.setEnabled(false);
                getSMSButton.setClickable(false);
            }

//            this.phoneEditText.setText(formatedPhone.replace(countryCodePicker.getSelectedCountryCodeWithPlus(), ""));

        } catch (NumberParseException e) {
//            getSMSButton.setEnabled(false);
            getSMSButton.setClickable(false);
//            System.err.println("NumberParseException was thrown: " + e.toString());
        }
    }

    @OnClick(R.id.fragment_login_phone_button_getSMS)
    public void OnGetSMSClick() {
        String numberStr = countryCodePicker.getSelectedCountryCode() + this.phoneEditText.getText();
        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        Call<Void> call = taxi5SDK.GetSMSCode("friday_sms", numberStr, "taxi5_ios_app");
        call.enqueue(this);
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
}
