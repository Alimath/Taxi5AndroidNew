package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class OrderResponseData {
    public OrderData getOrderData() {
        return orderData;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    @SerializedName("data")
    @Expose
    private OrderData orderData;

    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    @SerializedName("errors")
    @Expose
    private ArrayList<String> errors;

}