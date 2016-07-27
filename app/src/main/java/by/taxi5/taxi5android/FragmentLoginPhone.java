package by.taxi5.taxi5android;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fedar.trukhan on 27.07.16.
 */

public class FragmentLoginPhone extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View phoneFragment = inflater.inflate(R.layout.fragment_login_phone, container, false);
        return phoneFragment;
    }
}
