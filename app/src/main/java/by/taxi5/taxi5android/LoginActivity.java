package by.taxi5.taxi5android;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import butterknife.BindView;


/**
 * Created by fedar.trukhan on 26.07.16.
 */

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_activity_fragment_layout) FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Fragment phoneFragment = new FragmentLoginPhone();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.login_activity_fragment_layout, phoneFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public void OpenSMSFragment() {
        Fragment smsFragment = new FragmentLoginSMS();
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.login_activity_fragment_layout, smsFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

}
