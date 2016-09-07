package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by fedar.trukhan on 06.09.16.
 */

public class EstimatedPriceAndRouteResponceData {
    public EstimatedPriceAndRouteData getResponseData() {
        return priceAndRouteData;
    }

//    public Integer getStatusCode() {
//        return statusCode;
//    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    @SerializedName("data")
    @Expose
    private EstimatedPriceAndRouteData priceAndRouteData;

//    @SerializedName("status_code")
//    @Expose
//    private Integer statusCode;

    @SerializedName("errors")
    @Expose
    private ArrayList<String> errors;

}
