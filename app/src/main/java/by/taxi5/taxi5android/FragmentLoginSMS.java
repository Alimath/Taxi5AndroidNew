package by.taxi5.taxi5android;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by fedar.trukhan on 02.08.16.
 */

public class FragmentLoginSMS extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View phoneFragment = inflater.inflate(R.layout.fragment_login_sms, container, false);
        ButterKnife.bind(this, phoneFragment);

//        getSMSButton.setEnabled(false);
//        getSMSButton.setClickable(false);


        return phoneFragment;
    }
}
