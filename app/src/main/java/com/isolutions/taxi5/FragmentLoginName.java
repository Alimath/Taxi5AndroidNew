package com.isolutions.taxi5;

import android.support.v4.app.Fragment;
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
 * Created by fedar.trukhan on 09.08.16.
 */

public class FragmentLoginName extends Fragment {
    @BindView(R.id.fragment_login_name_text_edit)
    EditText editText;
    @BindView(R.id.fragment_login_name_send_code_button)
    Button sendCodeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View nameFragment = inflater.inflate(R.layout.fragment_login_name, container, false);
        ButterKnife.bind(this, nameFragment);

//        getSMSButton.setEnabled(false);
//        getSMSButton.setClickable(false);

        return nameFragment;
    }
}