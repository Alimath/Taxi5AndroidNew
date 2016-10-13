package com.isolutions.taxi5;

/**
 * Created by fedar.trukhan on 22.08.16.
 */


import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.gson.Gson;
import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.AmountData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.CustomerData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.EstimatedPriceAndRouteResponceData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderFeatures;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderOptions;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentStatusCreateOrder extends StatusesBaseFragment {
    @BindView(R.id.fragment_status_create_order_comment_edit_text)
    EditText commentEditText;

    @BindView(R.id.fragment_status_create_order_approximate_price_layout)
    LinearLayout estimatedPriceLayout;

    @BindView(R.id.fragment_status_create_order_from_text)
    TextView fromText;

    @BindView(R.id.fragment_status_create_order_to_text)
    TextView toText;

    @BindView(R.id.fragment_status_create_order_approximate_price_text)
    TextView estimatedPriceTextView;

    @BindView(R.id.fragment_status_create_order_from_to_view_search_to_icon)
    ImageView toSearchIcon;

//    @BindView(R.id.fragment_status_create_order_from_to_view_progress_bar)
//    ProgressBar progressBar;


    @BindView(R.id.fragment_status_create_order_from_to_view_progress_bar_2)
    AVLoadingIndicatorView progressBar2;

    @BindView(R.id.fragment_status_create_order_button_progress_bar)
    AVLoadingIndicatorView buttonProgressBar;

    @BindView(R.id.fragment_status_create_order_button)
    Button createOrderButton;

    private LocationData fromLocation;
    private LocationData toLocation;


    @BindView(R.id.fragment_status_create_order_from_to_view)
    ConstraintLayout fromToLayout;

    @BindView(R.id.fragment_status_create_order_options)
    ConstraintLayout featuresLayout;

    @BindView(R.id.fragment_status_create_order_features_view_back_image)
    ImageView featuresButtonBackImage;


    @BindView(R.id.fragment_status_create_order_options_baby_icon)
    ImageView isBabyIconImage;
    @BindView(R.id.fragment_status_create_order_options_baggage_icon)
    ImageView isEscortIconImage;
    @BindView(R.id.fragment_status_create_order_options_animal_icon)
    ImageView isAnimalIconImage;

    @BindView(R.id.fragment_status_create_order_options_baby_text)
    TextView isBabyTextView;
    @BindView(R.id.fragment_status_create_order_options_baggage_text)
    TextView isEscortTextView;
    @BindView(R.id.fragment_status_create_order_options_animal_text)
    TextView isAnimalTextView;


    @BindView(R.id.fragment_status_create_order_main)
    ConstraintLayout mainView;



    private boolean isBaby = false;
    private boolean isEscort = false;
    private boolean isAnimal = false;

    private boolean isBottomHidden = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
        View createOrder= inflater.inflate(R.layout.fragment_status_create_order, container, false);
        ButterKnife.bind(this, createOrder);

        HideEstimatedPrice();
        HideProgressBar();
        EndCreateButtonProgress();

        if (fromLocation != null) {
            setFromText(fromLocation.getStringDescription());
            HideProgressBar();
        }
        else {
            ShowProgressBar();
            setFromText("");
        }
        if(toLocation != null) {
            setToText(toLocation.getStringDescription());
        }
        else {
            setToText("");
        }

        return createOrder;
    }

    @Override
    public void onStart() {
        super.onStart();
        featuresButtonBackImage.setColorFilter(AppData.getInstance().getColor(R.color.hintsColor), PorterDuff.Mode.SRC_ATOP);
        fillWithOrder();
    }

    public void setFromLocation(LocationData fromLoc) {



        if(fromLoc != null) {
            if(!TextUtils.isEmpty(fromLoc.locationStringDescription)) {
                Log.d("taxi5", "location string description: " + fromLoc.locationStringDescription);
            }
//            if(fromLoc.locationDescription != null) {
//                Log.d("taxi5", "location description not null");
//                if(fromLoc.locationDescription.address != null) {
//                    Log.d("taxi5", "location description.address not null");
//                }
//                if(fromLoc.locationDescription.locationObject != null) {
//                    Log.d("taxi5", "location description.locationObject not null");
//                }
//            }

            fromLocation = fromLoc;
            setFromText(fromLoc.getStringDescription());
        }
        else {
            fromLocation = null;
            setFromText("");
        }
        CheckAndLoadPriceAndRoute();
    }

    public void ClearFields() {
        setFromLocation(null);
        setToLocation(null);
        commentEditText.setText("");
    }

    public void setToLocation(LocationData toLoc) {
        if(toLoc != null) {
            toSearchIcon.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.clear_edit_text_icon));
            toLocation = toLoc;
            setToText(toLoc.getStringDescription());
        }
        else {
            toSearchIcon.setImageDrawable(AppData.getInstance().getMyDrawable(R.drawable.status_icon_find));
            toLocation = null;
            setToText("");
        }
        CheckAndLoadPriceAndRoute();
    }

    public void setFromText(String fromDescription) {
        if(isVisible()) {
            if(!TextUtils.isEmpty(fromDescription)) {
                this.fromText.setText(fromDescription);
                HideProgressBar();
            }
            else {
                this.fromText.setText("");
                ShowProgressBar();
            }
        }
    }

    public void setToText(String toDescription) {
        if(isVisible()) {
            if(!TextUtils.isEmpty(toDescription)) {
                this.toText.setText(toDescription);
                this.toText.setTextColor(AppData.getInstance().getColor(R.color.defaultBlack));
            }
            else {
                this.toText.setText(getString(R.string.no_address_string));
                this.toText.setTextColor(AppData.getInstance().getColor(R.color.hintsColor));
            }
        }
    }

    @OnClick(R.id.fragment_status_create_order_button)
    public void onCreateOrderButtonClick() {

//        String jjs = new Gson().toJson(CreateOrder());
//        Log.d("taxi5", "JSON: " + jjs);

        StartCreateButtonProgress();
//        createOrderBtn.setMode(ActionProcessButton.Mode.ENDLESS);
//        createOrderBtn.setProgress(1);

        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }
        Call<OrderResponseData> call = taxi5SDK.SendOrderRequest(TokenData.getInstance().getToken(), CreateOrder());

        call.enqueue(new Callback<OrderResponseData>() {
            @Override
            public void onResponse(Call<OrderResponseData> call, Response<OrderResponseData> response) {
                EndCreateButtonProgress();
                if (response.isSuccessful()) {
                    OrderData order = response.body().getOrderData();
                    appData.setCurrentOrder(order, false);
                    FragmentMap.getMapFragment().RefreshView();
                } else {
                    Log.d("taxi5", "status code: " + response.raw().code());
                }
            }

            @Override
            public void onFailure(Call<OrderResponseData> call, Throwable t) {
                EndCreateButtonProgress();
//                createOrderBtn.setProgress(0);
                for (StackTraceElement element:t.getStackTrace()) {
                    Log.d("taxi5", "error:1 - " + element.toString());
                }
                Log.d("taxi5", "error create: " + t.getStackTrace());
            }
        });
    }


    public OrderData CreateOrder() {
        OrderData order = new OrderData();
        order.customerData = new CustomerData();
        order.customerData.name = ProfileData.getInstance().getName();
        order.customerData.msid = ProfileData.getInstance().getMsid();

        order.options= new OrderOptions();
        if(BuildConfig.DEBUG) {
            order.options.developer = true;
        }

        if(fromLocation != null) {
            order.from = fromLocation;
        }
        if(toLocation != null) {
            order.to = toLocation;
        }
        if(this.isBaby || this.isAnimal || this.isEscort) {
            order.features = new OrderFeatures();
            order.features.animal = this.isAnimal;
            order.features.baby = this.isBaby;
            order.features.escort = this.isEscort;
        }
        if(!TextUtils.isEmpty(this.commentEditText.getText())) {
            order.comment = this.commentEditText.getText().toString();
        }

        return order;
    }

    @Override
    public void fillWithOrder() {
        OrderData order = AppData.getInstance().getCurrentOrder();
        if(order != null && order.features != null) {
            if(order.features.baby != null) {
                this.isBaby = order.features.baby;
            }
            if(order.features.escort != null) {
                this.isEscort = order.features.escort;
            }
            if(order.features.animal != null) {
                this.isAnimal = order.features.animal;
            }
        }
        else {
            this.isBaby = false;
            this.isEscort = false;
            this.isAnimal = false;
            this.commentEditText.setText("");
        }

        if(isVisible()) {
            if (order != null && order.from != null) {
                setFromLocation(order.from);
                HideProgressBar();
            } else {
                ShowProgressBar();
                setFromLocation(null);
            }
            if (order != null && order.to != null) {
                setToLocation(order.to);
            } else {
                setToLocation(null);
//                setToText("");
            }

            RefillFeatures();
        }
    }

    void SetCreateOrderButtonAvailableState(boolean state) {
        if(state) {
            createOrderButton.setClickable(true);
        }
        else {
            createOrderButton.setClickable(false);
        }
    }

    public void ShowEstimatedPrice(Long price) {
        if(isVisible()) {
            if(price > 0) {
                estimatedPriceLayout.setVisibility(View.VISIBLE);
                Long rubles = price / 100L;
                Long copeek = price - (price/100L)*100L;


                estimatedPriceTextView.setText(" " + rubles + " " + getString(R.string.rubles_short) + " " +
                copeek + " " + getString(R.string.copeeks_short));
//                estimatedPriceTextView.setText("20 руб");
            }
            else {
                estimatedPriceLayout.setVisibility(View.INVISIBLE);
            }
        }
    }


    public void HideEstimatedPrice() {
        if(isVisible()) {
            estimatedPriceLayout.setVisibility(View.INVISIBLE);
        }
    }

    void StartCreateButtonProgress() {
        SetCreateOrderButtonAvailableState(false);
        createOrderButton.setText("");
        buttonProgressBar.setVisibility(View.VISIBLE);
    }

    void EndCreateButtonProgress() {
        createOrderButton.setText(getString(R.string.status_create_order_button_text));
        SetCreateOrderButtonAvailableState(true);
        buttonProgressBar.setVisibility(View.INVISIBLE);
    }

    private void ShowProgressBar() {
        if(isVisible()) {
            progressBar2.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.VISIBLE);
            SetCreateOrderButtonAvailableState(false);
        }
    }

    private void HideProgressBar() {
        if(isVisible()) {
            progressBar2.setVisibility(View.INVISIBLE);
//            progressBar.setVisibility(View.INVISIBLE);
            SetCreateOrderButtonAvailableState(true);
        }
    }

    @OnClick(R.id.fragment_status_create_order_from_to_view_search_from_address)
    public void OnSearchFromAddressClick() {
//        Log.d("taxi5", "Show search addresse");
        if(FragmentMap.getMapFragment() != null) {
            FragmentMap.getMapFragment().ShowSearchAddressView(true);
        }
    }

    @OnClick(R.id.fragment_status_create_order_from_to_view_search_to_address)
    public void OnSearhToAddressClick() {
//        Log.d("taxi5", "Show search addresse");
        if(toLocation != null) {
            this.setToLocation(null);
        }
        else {
            FragmentMap.getMapFragment().ShowSearchAddressView(false);
        }
    }

    @OnClick(R.id.fragment_status_create_order_from_to_view_search_to_address_full)
    public void OnSearchToAddressClickFull() {
        FragmentMap.getMapFragment().ShowSearchAddressView(false);
    }

    @OnClick(R.id.fragment_status_create_order_my_location_button)
    public void OnMyLocationClick() {
        FragmentMap.getMapFragment().ScrollMaptoPos(AppData.getInstance().nullPoint, false);
    }

    public void CheckAndLoadPriceAndRoute() {
        HideEstimatedPrice();
        if(fromLocation != null && toLocation != null) {
            LoadAndShowPriceAndRoute(fromLocation.latitude, fromLocation.longitude, toLocation.latitude, toLocation.longitude);
        }
    }

    Call<EstimatedPriceAndRouteResponceData> priceAndRouteCall;
    void LoadAndShowPriceAndRoute(double startLatitude, double startLongitude, double finishLatitude, double finishLongitude) {
        if(priceAndRouteCall != null) {
            priceAndRouteCall.cancel();
        }
        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }
        priceAndRouteCall = taxi5SDK.GetPriceAndRoute(TokenData.getInstance().getToken(),
                startLatitude, startLongitude, finishLatitude, finishLongitude);

        priceAndRouteCall.enqueue(new Callback<EstimatedPriceAndRouteResponceData>() {
            @Override
            public void onResponse(Call<EstimatedPriceAndRouteResponceData> call, Response<EstimatedPriceAndRouteResponceData> response) {
                if(response.isSuccessful()) {
                    if(response.body() != null && response.body().getResponseData() != null &&
                            response.body().getResponseData().amount != null && response.body().getResponseData().amount.size() > 0) {
                        Long price = 0L;
                        for (AmountData amount:response.body().getResponseData().amount) {
                            if(amount.currency.equalsIgnoreCase("byr")) {
                                price = amount.value/100;
                            }
                            else if(amount.currency.equalsIgnoreCase("byn")) {
                                price = amount.value;
                            }
                        }
                        ShowEstimatedPrice(price);
                    }
                    else {
                        HideEstimatedPrice();
                    }
                }
                else {
                    HideEstimatedPrice();
                }
            }

            @Override
            public void onFailure(Call<EstimatedPriceAndRouteResponceData> call, Throwable t) {
                HideEstimatedPrice();
            }
        });
    }

    @OnClick(R.id.fragment_status_create_order_features_view)
    public void OnFeaturesButtonClick() {
        Drawable mDrawable = featuresButtonBackImage.getDrawable();
        if(fromToLayout.getVisibility() == View.VISIBLE) {
            fromToLayout.setVisibility(View.INVISIBLE);
            featuresLayout.setVisibility(View.VISIBLE);
            mDrawable.setColorFilter(AppData.getInstance().getColor(R.color.activeFeaturesButtonBackColor), PorterDuff.Mode.SRC_ATOP);
            featuresButtonBackImage.setImageDrawable(mDrawable);
        }
        else {
            fromToLayout.setVisibility(View.VISIBLE);
            featuresLayout.setVisibility(View.INVISIBLE);
            mDrawable.setColorFilter(AppData.getInstance().getColor(R.color.hintsColor), PorterDuff.Mode.SRC_ATOP);
            featuresButtonBackImage.setImageDrawable(mDrawable);
        }
    }

    private void RefillFeatures() {
        Drawable babyDrawable = isBabyIconImage.getDrawable();
        Drawable animalDrawable = isAnimalIconImage.getDrawable();
        Drawable escortDrawable = isEscortIconImage.getDrawable();

        if(this.isBaby) {
            babyDrawable.setColorFilter(AppData.getInstance().getColor(R.color.defaultBlue), PorterDuff.Mode.SRC_ATOP);
            isBabyTextView.setTextColor(AppData.getInstance().getColor(R.color.defaultBlue));
        }
        else {
            babyDrawable.setColorFilter(AppData.getInstance().getColor(R.color.approximatePriceTextColor), PorterDuff.Mode.SRC_ATOP);
            isBabyTextView.setTextColor(AppData.getInstance().getColor(R.color.approximatePriceTextColor));
        }

        if(this.isAnimal) {
            animalDrawable.setColorFilter(AppData.getInstance().getColor(R.color.defaultBlue), PorterDuff.Mode.SRC_ATOP);
            isAnimalTextView.setTextColor(AppData.getInstance().getColor(R.color.defaultBlue));
        }
        else {
            animalDrawable.setColorFilter(AppData.getInstance().getColor(R.color.approximatePriceTextColor), PorterDuff.Mode.SRC_ATOP);
            isAnimalTextView.setTextColor(AppData.getInstance().getColor(R.color.approximatePriceTextColor));
        }

        if(this.isEscort) {
            escortDrawable.setColorFilter(AppData.getInstance().getColor(R.color.defaultBlue), PorterDuff.Mode.SRC_ATOP);
            isEscortTextView.setTextColor(AppData.getInstance().getColor(R.color.defaultBlue));
        }
        else {
            escortDrawable.setColorFilter(AppData.getInstance().getColor(R.color.approximatePriceTextColor), PorterDuff.Mode.SRC_ATOP);
            isEscortTextView.setTextColor(AppData.getInstance().getColor(R.color.approximatePriceTextColor));
        }

        this.isBabyIconImage.setImageDrawable(babyDrawable);
        this.isEscortIconImage.setImageDrawable(escortDrawable);
        this.isAnimalIconImage.setImageDrawable(animalDrawable);
    }

    @OnClick(R.id.fragment_status_create_order_options_baggage_button)
    public void OnFeatureEscortClick() {
        this.isEscort = !this.isEscort;
        RefillFeatures();
    }
    @OnClick(R.id.fragment_status_create_order_options_baby_button)
    public void OnFeatureBabyClick() {
        this.isBaby = !this.isBaby;
        RefillFeatures();
    }
    @OnClick(R.id.fragment_status_create_order_options_animal_button)
    public void OnFeatureAnimalClick() {
        this.isAnimal = !this.isAnimal;
        RefillFeatures();
    }


    public void HideAnimated() {
        if(!isBottomHidden) {
            isBottomHidden = true;
            this.mainView.animate().translationY(AppData.getInstance().dpToPx(125)).setDuration(300).start();
        }
    }

    public void ShowAnimated() {
        if(isBottomHidden) {
            isBottomHidden = false;
            this.mainView.animate().translationY(0).setDuration(300).start();
        }
    }
}
