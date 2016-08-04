package com.isolutions.taxi5;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fedar.trukhan on 02.08.16.
 */

public class FragmentLoginSMS extends Fragment {
    @BindView(R.id.fragment_login_sms_text_edit) EditText editText;
    @BindView(R.id.fragment_login_phone_button_getSMS) Button sendCodeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View phoneFragment = inflater.inflate(R.layout.fragment_login_sms, container, false);
        ButterKnife.bind(this, phoneFragment);

//        getSMSButton.setEnabled(false);
//        getSMSButton.setClickable(false);

        startTimer();
        return phoneFragment;
    }

    public void startTimer() {
        new CountDownTimer(30000, 500) {
            public void onTick(long millisUntilFinished) {
                sendCodeButton.setText(getString(R.string.registration_sms_buttonText) + " (" + (millisUntilFinished / 1000 + 1) + ")");
                DisableSendCodeButton();
            }

            public void onFinish() {
                sendCodeButton.setText(getString(R.string.registration_sms_buttonText));
                EnableSendCodeButton();
            }
        }.start();
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
}
