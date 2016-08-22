package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class OrderData {
    @SerializedName("id")
    public int id;

    @SerializedName("created_at")
    public double createdAt;

    @SerializedName("confirmation_expires_at")
    public double confirmationExpiresAt;

    @SerializedName("customer")
    public CustomerData customerData;

    @SerializedName("status")
    public OrderStatus status;

    @SerializedName("from")
    public LocationData from;

    @SerializedName("to")
    public LocationData to;

    @SerializedName("arrive_at")
    public double arriveAt;

    @SerializedName("eta")
    public Long eta;

    @SerializedName("features")
    public OrderFeatures features;

    @SerializedName("options")
    public OrderOptions options;

    @SerializedName("comment")
    public String comment;

    @SerializedName("vehicle")
    public VehicleData vehicle;

    @SerializedName("driver")
    public DriverData driver;

    @SerializedName("review")
    public ReviewData review;

    @SerializedName("amount")
    public List<AmountData> amount;

    @SerializedName("amount_paid")
    public List<AmountData> amountPaid;
}
