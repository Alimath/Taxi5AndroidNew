package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fedar.trukhan on 06.09.16.
 */

public class EstimatedPriceAndRouteData {
//    @SerializedName("distance")
//    @Expose
//    public Integer distance;
//
//    @SerializedName("duration")
//    @Expose
//    public Integer duration;

    @SerializedName("amount")
    @Expose
    public List<AmountData> amount;
}
