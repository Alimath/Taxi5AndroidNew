package com.isolutions.taxi5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    @BindView(R.id.left_drawer_order_taxi_icon)
    ImageView mapIcon;

    @BindView(R.id.left_drawer_my_places_icon)
    ImageView myPlacesIcon;

    @BindView(R.id.left_drawer_payments_icon)
    ImageView paymentsIcon;

    @BindView(R.id.left_drawer_plans_icon)
    ImageView plansIcon;

    @BindView(R.id.left_drawer_about_icon)
    ImageView aboutIcon;


    int colorBlack = AppData.getInstance().getColor(R.color.defaultBlack);
    int colorBlue = AppData.getInstance().getColor(R.color.defaultBlue);
    int clearColor = AppData.getInstance().getColor(R.color.clearColor);

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
        if(ProfileData.getInstance().getAvatarImage() != null) {
            this.avatarImage.setImageBitmap(ProfileData.getInstance().getAvatarImage());
        }
        else if(ProfileData.getInstance().getAvatarURL() != null) {
            if(!ProfileData.getInstance().getAvatarURL().isEmpty()) {
                Picasso.with(getActivity().getApplicationContext()).load(ProfileData.getInstance().getAvatarURL()).into(avatarImage);
            }
        }
        else {
            this.avatarImage.setImageBitmap(((BitmapDrawable)AppData.getInstance().getMyDrawable(R.drawable.default_user_icon)).getBitmap());
        }
        if(ProfileData.getInstance().getName() != null) {
            if(!ProfileData.getInstance().getName().isEmpty()) {
                profileNameTextView.setText(ProfileData.getInstance().getName());
            }
            else {
                profileNameTextView.setText("");
            }
        }
    }

    @OnClick(R.id.left_drawer_call_to_us_btn)
    public void onClickCallUsListener() {
        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity());
        }
        else {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
        }

//        TextView msgTextView = new TextView(AppData.getInstance().getAppContext());
//        msgTextView.setTextSize(14);
//        msgTextView.setPadding(0,0,0,0);
//        msgTextView.setTextColor(AppData.getInstance().getColor(R.color.defaultBlack));
//        msgTextView.setText("+375 (29) 133-75-00");

//        msgTextView.setGravity(Gravity.CENTER);

//        builder.setView(msgTextView);
        builder.setTitle("Позвоните в Такси \"Пятница\"");
        builder.setMessage("+375 (29) 133-75-00");
//        builder.
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
            HighlightMenuItem(OpenFragmentTypes.Profile);
        }
    }

    @OnClick(R.id.left_drawer_map_button)
    public void onMapButtonClick() {
        if(AppData.getInstance().mainActivity != null) {
            AppData.getInstance().setCurrentOrder(null, false);
            AppData.getInstance().mainActivity.OpenClearMap();
            HighlightMenuItem(OpenFragmentTypes.Map);
        }
    }

    @OnClick(R.id.left_drawer_my_places_button)
    public void onMyPlacesClick() {
        if(AppData.getInstance().mainActivity != null) {
            AppData.getInstance().mainActivity.OpenMyPlaces();
            HighlightMenuItem(OpenFragmentTypes.MyPlaces);
        }
    }

    @OnClick(R.id.left_drawer_payments_button)
    public void onPaymentsClick() {
        if(AppData.getInstance().mainActivity != null) {
            AppData.getInstance().mainActivity.OpenPayments();
            HighlightMenuItem(OpenFragmentTypes.Payments);
        }
    }

    public void ClearSelecting() {
        mapTextView.setTextColor(colorBlack);
        myPlacesTextView.setTextColor(colorBlack);
        paymentsTextView.setTextColor(colorBlack);
        plansTextView.setTextColor(colorBlack);
        aboutUsTextView.setTextColor(colorBlack);
        profileNameTextView.setTextColor(colorBlack);

        avatarImage.setColorFilter(clearColor);
        mapIcon.setColorFilter(clearColor);
        myPlacesIcon.setColorFilter(clearColor);
        paymentsIcon.setColorFilter(clearColor);
        plansIcon.setColorFilter(clearColor);
        aboutIcon.setColorFilter(clearColor);
    }

    public void HighlightMenuItem(OpenFragmentTypes type) {
        ClearSelecting();

        switch (type) {
            case Map:
                mapTextView.setTextColor(colorBlue);
                mapIcon.setColorFilter(colorBlue);
                break;
            case MyPlaces:
                myPlacesTextView.setTextColor(colorBlue);
                myPlacesIcon.setColorFilter(colorBlue);
                break;
            case Payments:
                paymentsTextView.setTextColor(colorBlue);
                paymentsIcon.setColorFilter(colorBlue);
                break;
            case Plans:
                plansTextView.setTextColor(colorBlue);
                plansIcon.setColorFilter(colorBlue);
                break;
            case AboutUs:
                aboutUsTextView.setTextColor(colorBlue);
                aboutIcon.setColorFilter(colorBlue);
                break;
            case Profile:
                profileNameTextView.setTextColor(colorBlue);
                break;
            case Nothing:
                break;
        }
    }

    public enum OpenFragmentTypes {
        Map, MyPlaces, Payments, Plans, AboutUs, Profile, Nothing
    }
}
