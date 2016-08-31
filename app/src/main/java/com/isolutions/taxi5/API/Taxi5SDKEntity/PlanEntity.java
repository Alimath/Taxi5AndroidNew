package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 30.08.16.
 */

public class PlanEntity {
    @SerializedName("type")
    @Expose
    public String planType;

    @SerializedName("amount")
    @Expose
    public AmountData[] amount;

    @SerializedName("includes")
    @Expose
    public Integer includes;

    @SerializedName("meta")
    @Expose
    public String meta;

    @SerializedName("offer")
    @Expose
    public Boolean offer;
}
