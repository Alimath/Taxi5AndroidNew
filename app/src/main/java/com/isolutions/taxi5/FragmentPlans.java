package com.isolutions.taxi5;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.PlanEntity;
import com.isolutions.taxi5.API.Taxi5SDKEntity.PlanResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;
import com.isolutions.taxi5.AdapterPlans;
import com.isolutions.taxi5.AppData;
import com.isolutions.taxi5.R;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 30.08.16.
 */

public class FragmentPlans extends Fragment {

    //    HashMap<PlanResponseData.PlanTypes, PlanEntity> plansDatas = null;
    @BindView(R.id.fragment_plans_list_view) ListView listView;

    @BindView(R.id.fragment_plans_button_byn)
    Button BYNButton;

    @BindView(R.id.fragment_plans_button_byr)
    Button BYRButton;

    private ArrayList<Map.Entry> plansData = new ArrayList<>();

    AdapterPlans adapterPlans;
    LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plans, container, false);
        ButterKnife.bind(this, view);
        this.inflater = inflater;

        LoadAndFillPlans();

        adapterPlans = new AdapterPlans(AppData.getInstance().getAppContext(), plansData);
        listView.setAdapter(adapterPlans);

        OnBYNButtonClick();

        return view;
    }


    public void LoadAndFillPlans() {
        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        if(taxi5SDK == null) {
            return;
        }
        Call<PlanResponseData> call = taxi5SDK.GetPlans(TokenData.getInstance().getToken());

        call.enqueue(new Callback<PlanResponseData>() {
            @Override
            public void onResponse(Call<PlanResponseData> call, Response<PlanResponseData> response) {
                if(response.isSuccessful()) {
                    Log.d("taxi5", "load plans ok");

                    if(response.body() != null && response.body().plansDatas != null && response.body().plansDatas.size() > 0) {
//                        plansDatas = new HashMap<PlanResponseData.PlanTypes, PlanEntity>();
                        plansData.clear();

                        if(response.body().plansDatas.get(PlanResponseData.PlanTypes.CITY) != null) {
//                            plansDatas.put(PlanResponseData.PlanTypes.CITY, response.body().plansDatas.get(PlanResponseData.PlanTypes.CITY));
                            Map.Entry<PlanResponseData.PlanTypes,PlanEntity> entry =
                                    new AbstractMap.SimpleEntry<>(PlanResponseData.PlanTypes.CITY,
                                            response.body().plansDatas.get(PlanResponseData.PlanTypes.CITY));
                            plansData.add(entry);
                        }
                        if(response.body().plansDatas.get(PlanResponseData.PlanTypes.COUNTRY) != null) {
                            Map.Entry<PlanResponseData.PlanTypes,PlanEntity> entry =
                                    new AbstractMap.SimpleEntry<>(PlanResponseData.PlanTypes.COUNTRY,
                                            response.body().plansDatas.get(PlanResponseData.PlanTypes.COUNTRY));
                            plansData.add(entry);
                        }
                        if(response.body().plansDatas.get(PlanResponseData.PlanTypes.IDLE) != null) {
                            Map.Entry<PlanResponseData.PlanTypes,PlanEntity> entry =
                                    new AbstractMap.SimpleEntry<>(PlanResponseData.PlanTypes.IDLE,
                                            response.body().plansDatas.get(PlanResponseData.PlanTypes.IDLE));
                            plansData.add(entry);
                        }
                        if(response.body().plansDatas.get(PlanResponseData.PlanTypes.BASE) != null) {
                            Map.Entry<PlanResponseData.PlanTypes,PlanEntity> entry =
                                    new AbstractMap.SimpleEntry<>(PlanResponseData.PlanTypes.BASE,
                                            response.body().plansDatas.get(PlanResponseData.PlanTypes.BASE));
                            plansData.add(entry);
                        }
                        if(response.body().plansDatas.get(PlanResponseData.PlanTypes.AIRPORT) != null) {
                            Map.Entry<PlanResponseData.PlanTypes,PlanEntity> entry =
                                    new AbstractMap.SimpleEntry<>(PlanResponseData.PlanTypes.AIRPORT,
                                            response.body().plansDatas.get(PlanResponseData.PlanTypes.AIRPORT));
                            plansData.add(entry);
                        }

                        adapterPlans.updateResource(plansData);
                    }
                }
                else {
                    Log.d("taxi5", "load plans error");
                }
            }

            @Override
            public void onFailure(Call<PlanResponseData> call, Throwable t) {
                Log.d("taxi5", "load plans failure");
                Log.d("taxi5", "f: " + t.getLocalizedMessage());
            }
        });
    }

    @OnClick(R.id.fragment_plans_button_byn)
    public void OnBYNButtonClick() {
        AppData.getInstance().selectedPlansCurrencyNew = true;
        BYRButton.setTextColor(AppData.getInstance().getColor(R.color.defaultBlack));
        BYNButton.setTextColor(AppData.getInstance().getColor(R.color.defaultBlue));
        adapterPlans.updateResource(plansData);
    }

    @OnClick(R.id.fragment_plans_button_byr)
    public void OnBYRButtonClick() {
        AppData.getInstance().selectedPlansCurrencyNew = false;
        BYRButton.setTextColor(AppData.getInstance().getColor(R.color.defaultBlue));
        BYNButton.setTextColor(AppData.getInstance().getColor(R.color.defaultBlack));
        adapterPlans.updateResource(plansData);
    }

}
