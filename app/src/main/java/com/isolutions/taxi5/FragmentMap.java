package com.isolutions.taxi5;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderStatusType;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener {
    private GoogleMap mMap;
    public LatLng nullPoint = new LatLng(53.902464, 27.56149);
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

    private static volatile FragmentMap mapFragment;
    public static FragmentMap getMapFragment() {
        return mapFragment;
    }

    private Call<OrderResponseData> readOrderCall = null;

    public void ResetMap() {
        AppData.getInstance().setCurrentOrder(null);

        RefreshView();
    }

    public void RefreshView() {
        if(AppData.getInstance().getCurrentOrder() == null) {
            if(readOrderCall != null) {
                readOrderCall.cancel();
            }
            changeStatus(this.statusCreateOrderFragment);
        }
        else {
            if (AppData.getInstance().getCurrentOrder().status != null) {
                changeStatusByEnum(AppData.getInstance().getCurrentOrder().status.status);
            }
            ReadOrderState();
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
        if(AppData.getInstance().getCurrentOrder() != null) {
            if(readOrderCall != null) {
                readOrderCall.cancel();
            }
            Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
            readOrderCall = taxi5SDK.ReadOrderStatus(TokenData.getInstance().getType() + " " + TokenData.getInstance().getAccessToken(), AppData.getInstance().getCurrentOrder().id);

            readOrderCall.enqueue(new Callback<OrderResponseData>() {
                @Override
                public void onResponse(Call<OrderResponseData> call, Response<OrderResponseData> response) {
                    OrderData order = response.body().getOrderData();
                    if (order != null) {
                        AppData.getInstance().setCurrentOrder(response.body().getOrderData());
                        if (order.status != null && order.status.status != null) {
                            changeStatusByEnum(order.status.status);
                        }
                        if(order.status != null) {
                            if(order.status.isTerminal || order.status.status == OrderStatusType.OrderPaid) {
                                return;
                            }
                        }
                    }
                    startTimer();
                }

                @Override
                public void onFailure(Call<OrderResponseData> call, Throwable t) {

                }
            });
        }
    }

    private View mapView;
    private SupportMapFragment gmsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mapView != null) {
            ViewGroup parent = (ViewGroup) mapView.getParent();
        }
        try {
            super.onCreateView(inflater, container, savedInstanceState);
            mapView = inflater.inflate(R.layout.fragment_map, container, false);
            ButterKnife.bind(this, mapView);
        }
        catch (InflateException e) {
        }

        this.mapFragment = this;

        gmsFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        gmsFragment.getMapAsync(this);

        RefreshView();

        return mapView;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nullPoint, 17));
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        mMap.setOnCameraIdleListener(this);

//        ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//        mMap.setMyLocationEnabled(true);
    }

    boolean noNeedGeocoding = false;
    public void ScrollMaptoPos(LatLng point, boolean noNeedGeocod) {
        this.noNeedGeocoding = noNeedGeocod;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17));
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
            return;
        }
        else {
            noNeedGeocoding = false;
        }
        LatLng pos = mMap.getCameraPosition().target;

        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        Call<LocationsListResponseData> call = taxi5SDK.ReverseGeocode(TokenData.getInstance().getToken(), pos.latitude, pos.longitude, true);

        if(statusCreateOrderFragment.isVisible()) {
            statusCreateOrderFragment.setFromText("");
            statusCreateOrderFragment.ShowProgressBar();
        }

        call.enqueue(new Callback<LocationsListResponseData>() {
            @Override
            public void onResponse(Call<LocationsListResponseData> call, Response<LocationsListResponseData> response) {
                statusCreateOrderFragment.HideProgressBar();
                Log.d("taxi5", "ok for pos");
                if(response.body().getStatusCode() == 200) {
                    if(response.body().getResponseData().size() > 0) {
//                        Log.d("taxi5", "full: " + response.body().getResponseData().get(0).locationDescription + ", string: " + response.body().getResponseData().get(0).locationStringDescription);
                        Log.d("taxi5", "ok response with array > 0");
                        LatLng pos = mMap.getCameraPosition().target;
                        LocationData locationData = response.body().getResponseData().get(0);
                        locationData.latitude = pos.latitude;
                        locationData.longitude = pos.longitude;

                        statusCreateOrderFragment.setFromLocation(locationData);
//                        LocationAddress ad1 = response.body().getResponseData().get(0).locationDescription.address;
//                        Log.d("taxi5", ad1.country + " " + ad1.settlement + " " + ad1.street + " " + ad1.building);
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
                statusCreateOrderFragment.HideProgressBar();
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

            if (statusFragment.isVisible()) {
                statusFragment.fillWithOrder();
            }
        }
    }


    public void ShowReviewStatus() {
        changeStatus(statusReviewFragment);
    }
    public void ShowReviewCompletedStatus() {
        changeStatus(statusReviewCompletedFragment);
    }

}
