package com.isolutions.taxi5;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationsListResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderStatus;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderStatusType;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener {
    private GoogleMap mMap;

    private CountDownTimer readOrderStatusTimer;


    public FragmentStatusCreateOrder statusCreateOrderFragment = new FragmentStatusCreateOrder();
    public FragmentStatusSearchCar statusSearchCarFragment = new FragmentStatusSearchCar();
    public FragmentStatusCarFind statusCarFindFragment = new FragmentStatusCarFind();
    public FragmentStatusCarOnWay statusCarOnWayFragment = new FragmentStatusCarOnWay();
    public FragmentStatusYouOnWay statusYouOnWayFragment = new FragmentStatusYouOnWay();
    public FragmentStatusCarDelivered statusCarDeliveredFragment = new FragmentStatusCarDelivered();
    public FragmentStatusOrderComplete statusOrderCompleteFragment = new FragmentStatusOrderComplete();
    public FragmentStatusPayment statusPaymentFragment = new FragmentStatusPayment();
    public FragmentStatusInformation statusInformationFragment = new FragmentStatusInformation();
    public FragmentStatusReview statusReviewFragment = new FragmentStatusReview();
    public FragmentStatusReviewCompleted statusReviewCompletedFragment = new FragmentStatusReviewCompleted();

    public FragmentStatusCreateOrderFindAddress statusCreateOrderFindAddressFragment = new FragmentStatusCreateOrderFindAddress();

    private static volatile FragmentMap mapFragment;
    public static FragmentMap getMapFragment() {
        return mapFragment;
    }

    private Call<OrderResponseData> readOrderCall = null;

    public void ResetMap() {
        AppData.getInstance().setCurrentOrder(null, false);

        RefreshView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(AppData.getInstance().toolbar != null) {
            AppData.getInstance().toolbar.ConvertToDefaultToolbar();
        }
    }

    public void RefreshView() {
        if(AppData.getInstance().isOrderHistory) {
            if(readOrderCall != null) {
                readOrderCall.cancel();
            }

            if(AppData.getInstance().getCurrentOrder() != null) {
                changeStatus(this.statusCreateOrderFragment);
                if(AppData.getInstance().getCurrentOrder().from != null) {
                    LatLng latLng = new LatLng(AppData.getInstance().getCurrentOrder().from.latitude, AppData.getInstance().getCurrentOrder().from.longitude);
                    statusCreateOrderFragment.setFromLocation(AppData.getInstance().getCurrentOrder().from);
                    if(AppData.getInstance().getCurrentOrder().to != null) {
                        statusCreateOrderFragment.setToLocation(AppData.getInstance().getCurrentOrder().to);
                    }
                    else {
                        statusCreateOrderFragment.setToLocation(null);
                    }
                    ScrollMaptoPos(latLng, true);
                }
                else {
                    AppData.getInstance().setCurrentOrder(null, false);
                    statusCreateOrderFragment.setToLocation(null);
                    ScrollMaptoPos(AppData.getInstance().nullPoint, false);
                }
            }
            else {
                AppData.getInstance().isOrderHistory = false;
                RefreshView();
            }
        }
        else {
            if(AppData.getInstance().getCurrentOrder() == null) {
                changeStatus(this.statusCreateOrderFragment);
//                statusCreateOrderFragment.setFromText("");
//                ScrollMaptoPos(nullPoint, false);
            }
            else {
                if(AppData.getInstance().getCurrentOrder().from != null) {
                    LatLng latLng = new LatLng(AppData.getInstance().getCurrentOrder().from.latitude, AppData.getInstance().getCurrentOrder().from.longitude);
                    ScrollMaptoPos(latLng, true);
                }
                if(AppData.getInstance().getCurrentOrder().status != null && !AppData.getInstance().getCurrentOrder().status.isTerminal) {
                    changeStatusByEnum(AppData.getInstance().getCurrentOrder().status.status);
                    ReadOrderState();
                }
                else {
                    if(AppData.getInstance().getCurrentOrder().status == null || AppData.getInstance().getCurrentOrder().status.status == null) {
                        changeStatus(statusCreateOrderFragment);
                    }
                    else {
                        changeStatusByEnum(AppData.getInstance().getCurrentOrder().status.status);
                    }
                }
            }
        }
    }

    public void startTimer() {
        if(AppData.getInstance().getCurrentOrder() != null && AppData.getInstance().getCurrentOrder().status != null &&
                !AppData.getInstance().getCurrentOrder().status.isTerminal) {
            this.readOrderStatusTimer = new CountDownTimer(3000, 500) {
                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    ReadOrderState();

                    if (AppData.getInstance().getCurrentOrder() != null) {
//                    startTimer();
                    }
                }
            }.start();
        }
    }

    void ReadOrderState() {
        if(AppData.getInstance().getCurrentOrder() != null && !AppData.getInstance().isOrderHistory) {
            if(readOrderCall != null) {
                readOrderCall.cancel();
            }
            Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
            if(taxi5SDK == null) {
                return;
            }
            readOrderCall = taxi5SDK.ReadOrderStatus(TokenData.getInstance().getType() + " " + TokenData.getInstance().getAccessToken(), AppData.getInstance().getCurrentOrder().id);

            readOrderCall.enqueue(new Callback<OrderResponseData>() {
                @Override
                public void onResponse(Call<OrderResponseData> call, Response<OrderResponseData> response) {
                    if(response.isSuccessful()) {
                        if (AppData.getInstance().getCurrentOrder() == null) {
                            return;
                        }
                        OrderData order = response.body().getOrderData();
                        if (order != null) {
                            AppData.getInstance().setCurrentOrder(response.body().getOrderData(), false);
                            if (order.status != null && order.status.status != null) {
                                changeStatusByEnum(order.status.status);
                            }
                            if (order.status != null) {
                                if (order.status.isTerminal || order.status.status == OrderStatusType.OrderPaid) {
                                    AppData.getInstance().isOrderHistory = true;
                                    return;
                                }
                            }
                        }
                    }
                    startTimer();

                }

                @Override
                public void onFailure(Call<OrderResponseData> call, Throwable t) {
                    startTimer();
                }
            });
        }
    }

    private View mapView;
    private SupportMapFragment gmsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("taxi5", "on create map view");
        if(mapView != null) {
            ViewGroup parent = (ViewGroup) mapView.getParent();
        }
        try {
            super.onCreateView(inflater, container, savedInstanceState);
            mapView = inflater.inflate(R.layout.fragment_map, container, false);
            ButterKnife.bind(this, mapView);
        }
        catch (InflateException e) {
            Log.d("taxi5", "map on create error");
        }

        this.mapFragment = this;

        gmsFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        gmsFragment.getMapAsync(this);

        RefreshView();

        return mapView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AppData.getInstance().nullPoint, 17));
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        mMap.setOnCameraIdleListener(this);
        RefreshView();

//        ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//        mMap.setMyLocationEnabled(true);
    }

    boolean noNeedGeocoding = false;
    public void ScrollMaptoPos(LatLng point, boolean noNeedGeocod) {
        if(noNeedGeocod) {
            Log.d("taxi5", "CHANGE NO NEED GEOCODING TRUE");
        }
        this.noNeedGeocoding = noNeedGeocod;
        if(mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCameraIdle() {

        if(noNeedGeocoding) {
            noNeedGeocoding = false;
            return;
        }

        LatLng pos = mMap.getCameraPosition().target;

        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }
        Call<LocationsListResponseData> call = taxi5SDK.ReverseGeocode(TokenData.getInstance().getToken(), pos.latitude, pos.longitude, true);

        if(statusCreateOrderFragment.isVisible()) {
            statusCreateOrderFragment.setFromLocation(null);
        }

        call.enqueue(new Callback<LocationsListResponseData>() {
            @Override
            public void onResponse(Call<LocationsListResponseData> call, Response<LocationsListResponseData> response) {
                if(response.isSuccessful()) {
                    if(AppData.getInstance().isOrderHistory) {
                        OrderData orderData = new OrderData();
                        if(AppData.getInstance().getCurrentOrder().to != null) {
                            orderData.to = AppData.getInstance().getCurrentOrder().to;
                        }
                        if(AppData.getInstance().getCurrentOrder().options != null) {
                            orderData.options = AppData.getInstance().getCurrentOrder().options;
                        }
                        if(AppData.getInstance().getCurrentOrder().comment != null) {
                            orderData.comment = AppData.getInstance().getCurrentOrder().comment;
                        }
                        if(AppData.getInstance().getCurrentOrder().features != null) {
                            orderData.features = AppData.getInstance().getCurrentOrder().features;
                        }


                        AppData.getInstance().setCurrentOrder(orderData, false);
                    }
                    RefreshView();
                    if(response.body().getResponseData().size() > 0) {
                        LatLng pos = mMap.getCameraPosition().target;
                        LocationData locationData = response.body().getResponseData().get(0);
                        locationData.latitude = pos.latitude;
                        locationData.longitude = pos.longitude;

                        statusCreateOrderFragment.setFromLocation(locationData);
                    }
                    else {
                        LatLng pos = mMap.getCameraPosition().target;

                        LocationData locationData = new LocationData();
                        locationData.latitude = pos.latitude;
                        locationData.longitude = pos.longitude;
                        statusCreateOrderFragment.setFromLocation(locationData);
                    }
                }
                else {
                    LatLng pos = mMap.getCameraPosition().target;

                    LocationData locationData = new LocationData();
                    locationData.latitude = pos.latitude;
                    locationData.longitude = pos.longitude;
                    statusCreateOrderFragment.setFromLocation(locationData);
                    Log.d("taxi5", "error to load data");
                }
            }

            @Override
            public void onFailure(Call<LocationsListResponseData> call, Throwable t) {
                Log.d("taxi5", "fail to reverse geocode" + t.getLocalizedMessage());
            }
        });

//        Log.d("taxi5", "pos: " + mMap.getCameraPosition().target.toString());
    }

    public void changeStatusByEnum(OrderStatusType statusType) {
        if(statusType != null) {
            switch (statusType) {
                case Registered:
                    changeStatus(statusSearchCarFragment);
                    break;
                case CarSearch:
                    changeStatus(statusSearchCarFragment);
                    break;
                case CarFound:
                    changeStatus(statusCarFindFragment);
                    break;
                case CarNotFound:
                    changeStatus(statusInformationFragment);
                    break;
                case CarApproved:
                    changeStatus(statusCarOnWayFragment);
                    break;
                case CarAssigned:
                    changeStatus(statusCarOnWayFragment);
                    break;
                case ClientApproveTimeout:
                    changeStatus(statusInformationFragment);
                    break;
                case ClientApproveReject:
                    changeStatus(statusInformationFragment);
                    break;
                case CarDelivering:
                    changeStatus(statusCarOnWayFragment);
                    break;
                case CarDelivered:
                    changeStatus(statusCarDeliveredFragment);
                    break;
                case ClientNotFound:
                    changeStatus(statusInformationFragment);
                    break;
                case OrderPostponed:
                    //TODO: make design
                    break;
                case OrderInProgress:
                    changeStatus(statusYouOnWayFragment);
                    break;
                case OrderCompleted:
                    changeStatus(statusPaymentFragment);
                    break;
                case OrderPendingPayment:
                    changeStatus(statusPaymentFragment);
                    break;
                case OrderPaid:
                    changeStatus(statusOrderCompleteFragment);
                    break;
                case OrderClosed:
                    changeStatus(statusOrderCompleteFragment);
                    break;
                case OrderNotPaid:
                    changeStatus(statusOrderCompleteFragment);
                    break;
                case Canceled:
                    changeStatus(statusInformationFragment);
                    break;
                case ForceCanceled:
                    changeStatus(statusInformationFragment);
                    break;
            }
        }
    }

    public void changeStatus(StatusesBaseFragment statusFragment) {
        if(AppData.getInstance().getAppForeground() && isVisible()) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();

            ft.replace(R.id.fragment_status, statusFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();

            getChildFragmentManager().executePendingTransactions();

            if (statusFragment.isVisible()) {
                statusFragment.fillWithOrder();
            }
        }
    }

    public void ShowSearchAddressView(boolean isFromAddress) {
        if(AppData.getInstance().getAppForeground() && isVisible()) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();

            ft.replace(R.id.fragment_search_addresses, statusCreateOrderFindAddressFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();

            statusCreateOrderFindAddressFragment.isFromLocationSelect = isFromAddress;

            if(AppData.getInstance().toolbar != null) {
                AppData.getInstance().toolbar.ConvertToSearchBar();
            }

            getChildFragmentManager().executePendingTransactions();

        }
    }

    public void HideSearhAddressView() {
        if(this.statusCreateOrderFindAddressFragment.isVisible()) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.remove(this.statusCreateOrderFindAddressFragment);
//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//            ft.addToBackStack("find_addresses");
            ft.commit();
//
            getChildFragmentManager().executePendingTransactions();

            AppData.getInstance().toolbar.ConvertToDefaultToolbar();
        }
    }



    public void ShowReviewStatus() {
        changeStatus(statusReviewFragment);
    }
    public void ShowReviewCompletedStatus() {
        changeStatus(statusReviewCompletedFragment);
    }

}
