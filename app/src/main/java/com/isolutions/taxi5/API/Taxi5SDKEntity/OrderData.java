package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class OrderData {
    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("created_at")
    @Expose
    public Long createdAt;

    @SerializedName("confirmation_expires_at")
    @Expose
    public Long confirmationExpiresAt;

    @SerializedName("customer")
    @Expose
    public CustomerData customerData;

    @SerializedName("status")
    @Expose
    public OrderStatus status;

    @SerializedName("from")
    @Expose
    public LocationData from;

    @SerializedName("to")
    @Expose
    public LocationData to;

    @SerializedName("arrive_at")
    @Expose
    public Long arriveAt;

    @SerializedName("eta")
    @Expose
    public Long eta;

    @SerializedName("features")
    @Expose
    public OrderFeatures features;

    @SerializedName("options")
    @Expose
    public OrderOptions options;

    @SerializedName("comment")
    @Expose
    public String comment;

    @SerializedName("vehicle")
    @Expose
    public VehicleData vehicle;

    @SerializedName("driver")
    @Expose
    public DriverData driver;

    @SerializedName("review")
    @Expose
    public ReviewData review;

    @SerializedName("amount")
    @Expose
    public List<AmountData> amount;

    @SerializedName("amount_paid")
    @Expose
    public List<AmountData> amountPaid;

    @Override
    public String toString() {
        return "test";
    }
}
