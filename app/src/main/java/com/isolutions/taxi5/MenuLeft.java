package com.isolutions.taxi5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
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

    @BindView(R.id.left_drawer_call_taxi_text_view)
    TextView mapTextView;
    @BindView(R.id.left_drawer_my_places_text_view)
    TextView myPlacesTextView;
    @BindView(R.id.left_drawer_payments_text_view)
    TextView paymentsTextView;
    @BindView(R.id.left_drawer_plans_text_view)
    TextView plansTextView;
    @BindView(R.id.left_drawer_about_text_view)
    TextView aboutUsTextView;


    int colorBlack = AppData.getInstance().getColor(R.color.defaultBlack);
    int colorBlue = AppData.getInstance().getColor(R.color.defaultBlue);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);

        View leftMenu= inflater.inflate(R.layout.left_drawer, container, false);
        ButterKnife.bind(this, leftMenu);


        RefreshProfileData();
        AppData.getInstance().leftDrawer = this;

        return leftMenu;
    }

    public void RefreshProfileData() {
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
    }

    @OnClick(R.id.left_drawer_call_to_us_btn)
    public void onClickCallUsListener() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("+375 (29) 133-75-00");
        builder.setPositiveButton(R.string.status_car_on_way_call_driver_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:+375291337500"));
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.status_car_on_way_call_driver_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.left_drawer_about_us_button)
    public void onAboutUsClick() {
        if(AppData.getInstance().mainActivity != null) {
            AppData.getInstance().mainActivity.OpenAboutUs();
            HighlightMenuItem(OpenFragmentTypes.AboutUs);
        }
    }

    @OnClick(R.id.left_drawer_plans_button)
    public void onPlansButtonClick() {
        if(AppData.getInstance().mainActivity != null) {
            AppData.getInstance().mainActivity.OpenPlansMenu();
            HighlightMenuItem(OpenFragmentTypes.Plans);
        }
    }

    @OnClick(R.id.left_drawer_profile_button)
    public void onProfileButtonClick() {
        if(AppData.getInstance().mainActivity != null) {
            AppData.getInstance().mainActivity.OpenProfileMenu();
            HighlightMenuItem(OpenFragmentTypes.Nothing);
        }
    }

    @OnClick(R.id.left_drawer_map_button)
    public void onMapButtonClick() {
        if(AppData.getInstance().mainActivity != null) {
            AppData.getInstance().mainActivity.OpenClearMap();
            HighlightMenuItem(OpenFragmentTypes.Map);
        }
    }

    public void ClearSelecting() {
        mapTextView.setTextColor(colorBlack);
        myPlacesTextView.setTextColor(colorBlack);
        paymentsTextView.setTextColor(colorBlack);
        plansTextView.setTextColor(colorBlack);
        aboutUsTextView.setTextColor(colorBlack);
    }

    public void HighlightMenuItem(OpenFragmentTypes type) {
        ClearSelecting();

        switch (type) {
            case Map:
                mapTextView.setTextColor(colorBlue);
                break;
            case MyPlaces:
                myPlacesTextView.setTextColor(colorBlue);
                break;
            case Payments:
                paymentsTextView.setTextColor(colorBlue);
                break;
            case Plans:
                plansTextView.setTextColor(colorBlue);
                break;
            case AboutUs:
                aboutUsTextView.setTextColor(colorBlue);
                break;
            case Nothing:
                break;
        }
    }

    public enum OpenFragmentTypes {
        Map, MyPlaces, Payments, Plans, AboutUs, Nothing
    }
}
