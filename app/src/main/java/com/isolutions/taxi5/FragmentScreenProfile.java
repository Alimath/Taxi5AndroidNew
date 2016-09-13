package com.isolutions.taxi5;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.system.ErrnoException;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by fedar.trukhan on 29.08.16.
 */

public class FragmentScreenProfile extends Fragment {

    private static volatile boolean isUploading = false;

    @BindView(R.id.profile_screen_name_edit_text)
    EditText nameEditText;

    @BindView(R.id.profile_screen_email_edit_text)
    EditText emailEditText;

    @BindView(R.id.profile_screen_phone_text_view)
    TextView phoneTextView;

    @BindView(R.id.fragment_screen_profile_avatar_image)
    CircleImageView avatarImage;

    @BindView(R.id.fragment_screen_profile_upload_btn_progress_bar)
    AVLoadingIndicatorView uploadProgressBar;
    @BindView(R.id.fragment_screen_profile_upload_btn)
    Button uploadButton;

    @BindView(R.id.fragment_screen_profile_image_croper)
    CropImageView croper;

    @BindView(R.id.fragment_screen_profile_image_croper_button)
    Button croperButton;

    ProfileData profileData;

    Bitmap newAvatarImage;
    private Uri mCropImageUri;

    Call<OrderResponseActionData> call;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d("taxi5", "image loaded: " + from.name());
            croper.setVisibility(View.VISIBLE);
            croper.setFixedAspectRatio(true);

//            croper.getParams().setShape(CookieCutterShape.SQUARE);
            croperButton.setVisibility(View.VISIBLE);
            croper.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            // loading of the bitmap failed
            Log.d("taxi5", "error fail load image");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(AppData.getInstance().toolbar != null) {
            AppData.getInstance().toolbar.ConvertToDefaultWithTitle(getString(R.string.profile_screen_title));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View screenView = inflater.inflate(R.layout.fragment_screen_profile, container, false);
        ButterKnife.bind(this, screenView);

        RefreshView();

        nameEditText.clearFocus();
        phoneTextView.clearFocus();

        if(isUploading) {
            ShowProgressBar();
        }
        else {
            HideProgressBar();
        }

        emailEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_ENTER) {
                    try {
                        InputMethodManager inputManager = (InputMethodManager) AppData.getInstance().mainActivity.getSystemService(INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(AppData.getInstance().mainActivity.getCurrentFocus().getWindowToken(), 0);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    emailEditText.setFocusable(false);

                    emailEditText.setFocusable(true);
                    emailEditText.setFocusableInTouchMode(true);
                }
                return false;
            }
        });

        return screenView;
    }

    public void RefreshView() {
        if(!isUploading) {
            if (ProfileData.getInstance().getMsid() != null) {
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

            if (ProfileData.getInstance().getName() != null) {
                nameEditText.setText(ProfileData.getInstance().getName());
            }
            if (ProfileData.getInstance().getEmail() != null) {
                emailEditText.setText(ProfileData.getInstance().getEmail());
            }

            if (ProfileData.getInstance().getAvatarImage() != null) {
                this.avatarImage.setImageBitmap(ProfileData.getInstance().getAvatarImage());
            } else if (ProfileData.getInstance().getAvatarURL() != null) {
                if (!ProfileData.getInstance().getAvatarURL().isEmpty()) {
                    Picasso.with(getActivity().getApplicationContext()).load(ProfileData.getInstance().getAvatarURL()).into(avatarImage);
                }
            }
            HideProgressBar();
        }
        else {
            ShowProgressBar();
        }
    }

    @OnClick(R.id.fragment_screen_profile_upload_btn)
    public void OnUploadProfileClick() {
        if(TextUtils.isEmpty(this.nameEditText.getText())) {
            Toast.makeText(AppData.getInstance().currentActivity, getString(R.string.login_name_please_set_your_name_text), Toast.LENGTH_SHORT).show();
            return;
        }
        if(!TextUtils.isEmpty(this.emailEditText.getText()) && !isValidEmail(this.emailEditText.getText())) {
            Toast.makeText(AppData.getInstance().currentActivity, getString(R.string.profile_screen_please_set_valid_email), Toast.LENGTH_SHORT).show();
        }
        if(!isUploading) {
            isUploading = true;
            ShowProgressBar();

            ProfileData profileData = ProfileData.getInstance();

            profileData.setName(this.nameEditText.getText().toString());
            profileData.setEmail(this.emailEditText.getText().toString());
            if (newAvatarImage != null) {
                profileData.setAvatarImage(newAvatarImage);
            }

            Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
            if (taxi5SDK == null) {
                return;
            }
            call = taxi5SDK.SendProfile(TokenData.getInstance().getToken(), profileData);

            this.profileData = profileData;

            call.enqueue(new Callback<OrderResponseActionData>() {
                @Override
                public void onResponse(Call<OrderResponseActionData> call, Response<OrderResponseActionData> response) {
                    if (response.isSuccessful()) {
                        Log.d("taxi5", "ok to load profile");
                    } else {
                        Log.d("taxi5", "error to load profile");

                        try {
                            Log.d("taxi5", response.raw().code() + ": " + response.raw().body().string());
                        }
                        catch (IOException err) {
                            Log.d("taxi5", response.raw().code() + ": ");
                        }
                    }

                    isUploading = false;
                    if(AppData.getInstance().mainActivity != null && AppData.getInstance().mainActivity.fragmentScreenProfile != null) {
                        AppData.getInstance().mainActivity.fragmentScreenProfile.RefreshView();
                    }
//                    if(isVisible() && isAdded()) {
//                        AppData.getInstance().mainActivity.fragmentScreenProfile.HideProgressBar();
//                        AppData.getInstance().mainActivity.fragmentScreenProfile.refreshProfile();
//                    }
//                    else {
//                        if(AppData.getInstance().leftDrawer != null) {
//                            AppData.getInstance().leftDrawer.RefreshProfileData();
//                        }
//                    }
                    if(AppData.getInstance().leftDrawer != null) {
                        AppData.getInstance().leftDrawer.RefreshProfileData();
                    }
                    newAvatarImage = null;
                }

                @Override
                public void onFailure(Call<OrderResponseActionData> call, Throwable t) {
                    isUploading = false;
                    newAvatarImage = null;
                    if(AppData.getInstance().mainActivity != null && AppData.getInstance().mainActivity.fragmentScreenProfile != null) {
                        AppData.getInstance().mainActivity.fragmentScreenProfile.RefreshView();
                    }
                    Log.d("taxi5", "failure to load profile: " + t.getLocalizedMessage());
                }
            });
        }
    }

    private void refreshProfile() {
        profileData.setAvatarImage(newAvatarImage);
        profileData.saveProfileData();
        if(profileData != null && isVisible()) {
            RefreshView();
        }
        AppData.getInstance().leftDrawer.RefreshProfileData();
    }

    private void ShowProgressBar() {
        uploadButton.setClickable(false);
        uploadProgressBar.setVisibility(View.VISIBLE);
    }

    private void HideProgressBar() {
        if(isVisible()) {
            uploadButton.setClickable(true);
            uploadProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.fragment_screen_profile_choose_avatar_btn)
    public void onChooseAvatarClick() {
        this.startActivityForResult(getPickImageChooserIntent(), 200);

//        RxImagePicker.with(AppData.getInstance().getAppContext()).requestImage(Sources.GALLERY).subscribe(new Action1<Uri>() {
//            @Override
//            public void call(Uri uri) {
//                Picasso.with(getActivity().getApplicationContext()).load(uri).into(target);
//            }
//        });
    }

    @OnClick(R.id.fragment_screen_profile_image_croper_button)
    public void onPickImageClick() {
        newAvatarImage = Bitmap.createScaledBitmap(croper.getCroppedImage(), 300, 300, false);
        avatarImage.setImageBitmap(newAvatarImage);
        croper.setVisibility(View.INVISIBLE);
        croperButton.setVisibility(View.INVISIBLE);

        croper.clearImage();
    }

    /**
     * Create a chooser intent to select the source to get image from.<br/>
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the intent chooser.
     */
    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = AppData.getInstance().currentActivity.getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = AppData.getInstance().currentActivity.getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
     */
    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = AppData.getInstance().currentActivity.getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e) {
            if (e.getCause() instanceof ErrnoException) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = getPickImageResultUri(data);

            croper.setVisibility(View.VISIBLE);
            croper.setFixedAspectRatio(true);

//            croper.getParams().setShape(CookieCutterShape.SQUARE);
            croperButton.setVisibility(View.VISIBLE);
            croper.setImageUriAsync(imageUri);

//            Picasso.with(getActivity().getApplicationContext()).invalidate(imageUri);
//            Picasso.with(getActivity().getApplicationContext()).load
//            Picasso.with(getActivity().getApplicationContext()).load(data).into(target);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
//            boolean requirePermissions = false;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//                    AppData.getInstance().currentActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
//                    isUriRequiresPermissions(imageUri)) {
//
//                // request permissions and handle the result in onRequestPermissionsResult()
//                requirePermissions = true;
//                mCropImageUri = imageUri;
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
//            }
//
//            if (!requirePermissions) {
//                croper.setImageUriAsync(imageUri);
//            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            croper.setImageUriAsync(mCropImageUri);
        } else {
//            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick({R.id.fragment_screen_profile_main, R.id.fragment_screen_profile_pick_ava_btn})
    public void OnBackgroundClick() {
        try {
            InputMethodManager inputManager = (InputMethodManager) AppData.getInstance().mainActivity.getSystemService(INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(AppData.getInstance().mainActivity.getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
