package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public enum OrderStatusType {
    @SerializedName("registered")
    Registered,
    @SerializedName("car_search")
    CarSearch,
    @SerializedName("car_found")
    CarFound,
    @SerializedName("car_not_found")
    CarNotFound,
    @SerializedName("car_approved")
    CarApproved,
    @SerializedName("car_assigned")
    CarAssigned,
    @SerializedName("client_approve_timeout")
    ClientApproveTimeout,
    @SerializedName("client_approve_reject")
    ClientApproveReject,
    @SerializedName("car_delivering")
    CarDelivering,
    @SerializedName("car_delivered")
    CarDelivered,
    @SerializedName("client_not_found")
    ClientNotFound,
    @SerializedName("order_postponed")
    OrderPostponed,
    @SerializedName("order_in_progress")
    OrderInProgress,
    @SerializedName("order_completed")
    OrderCompleted,
    @SerializedName("order_paid")
    OrderPaid,
    @SerializedName("order_closed")
    OrderClosed,
    @SerializedName("order_not_paid")
    OrderNotPaid,
    @SerializedName("canceled")
    Canceled,
    @SerializedName("force_canceled")
    ForceCanceled,
    @SerializedName("order_pending_payment")
    OrderPendingPayment
}
