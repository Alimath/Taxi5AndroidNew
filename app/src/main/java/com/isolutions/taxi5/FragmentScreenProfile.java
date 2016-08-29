package com.isolutions.taxi5;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseActionData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnItemLongClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;

/**
 * Created by fedar.trukhan on 29.08.16.
 */

public class FragmentScreenProfile extends Fragment {

    private boolean isUploading = false;

    @BindView(R.id.profile_screen_name_edit_text)
    EditText nameEditText;

    @BindView(R.id.profile_screen_email_edit_text)
    EditText emailEditText;

    @BindView(R.id.profile_screen_phone_text_view)
    TextView phoneTextView;

    @BindView(R.id.fragment_screen_profile_avatar_image)
    CircleImageView avatarImage;

    @BindView(R.id.fragment_screen_profile_upload_btn_progress_bar)
    ProgressBar uploadProgressBar;

    ProfileData profileData;

    Bitmap newAvatarImage;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            // loading of the bitmap was a success
            newAvatarImage = bitmap;
            avatarImage.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            // loading of the bitmap failed
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View screenView = inflater.inflate(R.layout.fragment_screen_profile, container, false);
        ButterKnife.bind(this, screenView);

        RefreshView();

        nameEditText.clearFocus();
        phoneTextView.clearFocus();
        HideProgressBar();

        return screenView;
    }

    public void RefreshView() {
        if(ProfileData.getInstance().getMsid() != null) {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();


            String formatedPhone = PhoneNumberUtils.formatNumber("+" + ProfileData.getInstance().getMsid());
//            try {
//                Phonenumber.PhoneNumber numberProto = phoneUtil.parse(ProfileData.getInstance().getMsid(), "");
//                PhoneNumberUtils.formatNumber()
//                formatedPhone = phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                phoneTextView.setText(formatedPhone);
//            }
//            catch (NumberParseException e) {
//                phoneTextView.setText(formatedPhone);
//            }
        }

        if(ProfileData.getInstance().getName() != null) {
            nameEditText.setText(ProfileData.getInstance().getName());
        }
        if(ProfileData.getInstance().getEmail() != null) {
            emailEditText.setText(ProfileData.getInstance().getEmail());
        }

        if(ProfileData.getInstance().getAvatarURL() != null) {
            if(!ProfileData.getInstance().getAvatarURL().isEmpty()) {
                Picasso.with(getActivity().getApplicationContext()).load(ProfileData.getInstance().getAvatarURL()).into(avatarImage);
            }
        }

    }

    @OnClick(R.id.fragment_screen_profile_upload_btn)
    public void OnUploadProfileClick() {
        isUploading = true;
        ShowProgressBar();

        ProfileData profileData = ProfileData.getInstance();

        profileData.setName(this.nameEditText.getText().toString());
        profileData.setEmail(this.emailEditText.getText().toString());
        if(newAvatarImage != null) {
            profileData.setAvatarImage(newAvatarImage);
        }

        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        Call<OrderResponseActionData> call = taxi5SDK.SendProfile(TokenData.getInstance().getToken(), profileData);

        this.profileData = profileData;

        call.enqueue(new Callback<OrderResponseActionData>() {
            @Override
            public void onResponse(Call<OrderResponseActionData> call, Response<OrderResponseActionData> response) {
                isUploading = false;
                HideProgressBar();
                refreshProfile();
                if(response.isSuccessful()) {
                    Log.d("taxi5", "ok to load profile");
                }
                else {
                    Log.d("taxi5", "error to load profile");

                }
            }

            @Override
            public void onFailure(Call<OrderResponseActionData> call, Throwable t) {
                isUploading = false;
                HideProgressBar();
                Log.d("taxi5", "failure to load profile: " + t.getLocalizedMessage());
            }
        });
    }

    private void refreshProfile() {
        if(profileData != null) {
            profileData.saveProfileData();
            AppData.getInstance().leftDrawer.RefreshProfileData();
            RefreshView();
        }
    }

    public void ShowProgressBar() {
        uploadProgressBar.setVisibility(View.VISIBLE);
    }

    public void HideProgressBar() {
        uploadProgressBar.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.fragment_screen_profile_choose_avatar_btn)
    public void onChooseAvatarClick() {
//        RxImagePicker.with(AppData.getInstance().getAppContext()).requestImage(Sources.GALLERY).subscribe(new Action1<Uri>() {
//            @Override
//            public void call(Uri uri) {
//                Picasso.with(getActivity().getApplicationContext()).load(uri).resize(300, 300).onlyScaleDown().into(target);
//            }
//        });
    }

}
