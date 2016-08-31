package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fedar.trukhan on 30.08.16.
 */

public class PlanResponseData {
    @SerializedName("data")
    @Expose
    public HashMap<PlanTypes, PlanEntity> plansDatas;

    @SerializedName("errors")
    @Expose
    public ArrayList<String> errors;

    public enum PlanTypes {
        @SerializedName("base")
        @Expose
        BASE,
        @SerializedName("airport")
        @Expose
        AIRPORT,
        @SerializedName("idle")
        @Expose
        IDLE,
        @SerializedName("country")
        @Expose
        COUNTRY,
        @SerializedName("city")
        @Expose
        CITY
    }
}
