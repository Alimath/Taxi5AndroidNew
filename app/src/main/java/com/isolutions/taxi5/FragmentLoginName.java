package com.isolutions.taxi5;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.wang.avi.AVLoadingIndicatorView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 09.08.16.
 */

public class FragmentLoginName extends Fragment {
    @BindView(R.id.fragment_login_name_text_edit)
    EditText editText;
    @BindView(R.id.fragment_login_name_send_code_button)
    Button sendCodeButton;

    @BindView(R.id.fragment_login_name_loading_button_name_view)
    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View nameFragment = inflater.inflate(R.layout.fragment_login_name, container, false);
        ButterKnife.bind(this, nameFragment);

        avLoadingIndicatorView.hide();
        sendCodeButton.setText(getString(R.string.registration_name_buttonText));


        return nameFragment;
    }

    @OnClick(R.id.fragment_login_name_send_code_button)
    public void OnYesItMyNameClick() {
        if(TextUtils.isEmpty(editText.getText())) {
            Toast.makeText(getContext(), R.string.login_name_please_set_your_name_text, Toast.LENGTH_SHORT).show();
            return;
        }
        avLoadingIndicatorView.show();
        sendCodeButton.setText("");
        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }
        LoginActivity loginActivity = (LoginActivity) getActivity();
        Call<Void> call = taxi5SDK.GetSMSWithName("friday_sms", loginActivity.phoneNumber, AppData.client_id, this.editText.getText().toString());
        sendCodeButton.setClickable(false);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                avLoadingIndicatorView.hide();
                sendCodeButton.setText(getString(R.string.registration_name_buttonText));
                sendCodeButton.setClickable(true);
                LoginActivity loginActivity = (LoginActivity) getActivity();
                if(response.isSuccessful()) {
                    loginActivity.OpenSMSFragment();
                }
                else {
                    Toast.makeText(AppData.getInstance().getAppContext(), "Ошибка, введите ваше имя", Toast.LENGTH_SHORT);
                    loginActivity.OpenPhoneFragment();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                sendCodeButton.setClickable(true);
                avLoadingIndicatorView.hide();
                Toast.makeText(AppData.getInstance().getAppContext(), "Ошибка, введите ваше имя", Toast.LENGTH_SHORT);
                sendCodeButton.setText(getString(R.string.registration_name_buttonText));
                LoginActivity loginActivity = (LoginActivity) getActivity();
                loginActivity.OpenPhoneFragment();
            }
        });
    }
}