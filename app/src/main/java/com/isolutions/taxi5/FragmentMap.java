package com.isolutions.taxi5;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderStatusType;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMap extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng nullPoint = new LatLng(53.902464, 27.56149);
    private CountDownTimer readOrderStatusTimer;


    public FragmentStatusCreateOrder statusCreateOrderFragment = new FragmentStatusCreateOrder();
    public FragmentStatusSearchCar statusSearchCarFragment = new FragmentStatusSearchCar();
    public FragmentStatusCarFind statusCarFindFragment = new FragmentStatusCarFind();
    public FragmentStatusCarOnWay statusCarOnWayFragment = new FragmentStatusCarOnWay();
    public FragmentStatusYouOnWay statusYouOnWayFragment = new FragmentStatusYouOnWay();
    public FragmentStatusCarDelivered statusCarDeliveredFragment = new FragmentStatusCarDelivered();
    public FragmentStatusOrderComplete statusOrderCompleteFragment = new FragmentStatusOrderComplete();
    public FragmentStatusPayment statusPaymentFragment = new FragmentStatusPayment();



    public OrderData orderData;


    public void ResetMap() {
        orderData = null;

        RefreshView();
    }

    public void RefreshView() {
        ReadOrderState();
        if(orderData == null) {
            changeStatus(this.statusPaymentFragment);
        }
        else {
            ReadOrderState();
        }
    }

    public void startTimer() {
        this.readOrderStatusTimer = new CountDownTimer(3000, 500) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                ReadOrderState();

                if(orderData != null) {
//                    startTimer();
                }
            }
        }.start();
    }

    void ReadOrderState() {
        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        Call<OrderResponseData> call = taxi5SDK.ReadOrderStatus(TokenData.getInstance().getType()+" "+ TokenData.getInstance().getAccessToken(), 609306);

        call.enqueue(new Callback<OrderResponseData>() {
            @Override
            public void onResponse(Call<OrderResponseData> call, Response<OrderResponseData> response) {
                orderData = response.body().getOrderData();
                Log.d("taxi5", orderData.id + " " +
                        orderData.status.status + " ");
//                        order.to.locationDescription.address.street + " " +
//                        order.vehicle.titleName + " " +
//                        order.driver.driverPhone + " " +
//                        order.comment);
                if(orderData.status != null) {
                    Log.d("taxi5", "refresh by enum");
                    changeStatusByEnum(orderData.status.status);
                }
                startTimer();
            }

            @Override
            public void onFailure(Call<OrderResponseData> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
//        View mapView = inflater.inflate(R.layout.fragment_map, container, false);
        Log.d("taxi5", "hallo world");
        View mapView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, mapView);


        RefreshView();
//        Fragment statusCreateOrderFragment = this.statusCreateOrderFragment;


//        getFragmentManager().getFragment(R.id.map, "sa");

//        getSMSButton.setEnabled(false);
//        getSMSButton.setClickable(false);


//        SupportMapFragment supportMapFragment = (SupportMapFragment) this.getFragmentManager().findFragmentById(R.id.map);

        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return mapView;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }


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

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(nullPoint, 17));
        mMap.getUiSettings().setRotateGesturesEnabled(false);

//        ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//        mMap.setMyLocationEnabled(true);


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
                    //TODO: make design
                    break;
                case CarApproved:
                    changeStatus(statusCarOnWayFragment);
                    break;
                case CarAssigned:
                    changeStatus(statusCarOnWayFragment);
                    break;
                case ClientApproveTimeout:
                    //TODO: make design
                    break;
                case ClientApproveReject:
                    //TODO: make design
                    break;
                case CarDelivering:
                    changeStatus(statusCarOnWayFragment);
                    break;
                case CarDelivered:
                    changeStatus(statusCarDeliveredFragment);
                    break;
                case ClientNotFound:
                    //TODO: make design
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
                    //TODO: make design
                    break;
                case ForceCanceled:
                    //TODO: make design
                    break;
            }
        }
    }

    public void changeStatus(StatusesBase statusFragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        Log.d("taxi5", "change status");
        if(orderData != null) {
            Log.d("taxi5", "fill");
            statusFragment.fillWithOrder(orderData);
        }

        ft.replace(R.id.fragment_status, statusFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

}
