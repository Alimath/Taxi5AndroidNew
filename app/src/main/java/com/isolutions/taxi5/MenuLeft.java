package com.isolutions.taxi5;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by fedar.trukhan on 23.08.16.
 */

public class MenuLeft extends Fragment {

    @BindView(R.id.left_drawer_avatar_image)
    CircleImageView avatarImage;

    @BindView(R.id.left_drawer_profile_name_text_view)
    TextView profileNameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);

        View leftMenu= inflater.inflate(R.layout.left_drawer, container, false);
        ButterKnife.bind(this, leftMenu);
        if(ProfileData.getInstance().getAvatarURL() != null) {
            if(!ProfileData.getInstance().getAvatarURL().isEmpty()) {
                Picasso.with(getActivity().getApplicationContext()).load(ProfileData.getInstance().getAvatarURL()).into(avatarImage);
            }
        }
        if(ProfileData.getInstance().getName() != null) {
            if(!ProfileData.getInstance().getName().isEmpty()) {
                profileNameTextView.setText(ProfileData.getInstance().getName());
            }
        }

        return leftMenu;
    }


    @OnClick(R.id.left_drawer_call_to_us_btn)
    public void OnClickCallUsListener() {
        Log.d("taxi5", "call us");
    }
}
