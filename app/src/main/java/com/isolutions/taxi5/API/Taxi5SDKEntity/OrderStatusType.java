package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public enum OrderStatusType {
    @SerializedName("registered")
    @Expose
    Registered,
    @SerializedName("car_search")
    @Expose
    CarSearch,
    @SerializedName("car_found")
    @Expose
    CarFound,
    @SerializedName("car_not_found")
    @Expose
    CarNotFound,
    @SerializedName("car_approved")
    @Expose
    CarApproved,
    @SerializedName("car_assigned")
    @Expose
    CarAssigned,
    @SerializedName("client_approve_timeout")
    @Expose
    ClientApproveTimeout,
    @SerializedName("client_approve_reject")
    @Expose
    ClientApproveReject,
    @SerializedName("car_delivering")
    @Expose
    CarDelivering,
    @SerializedName("car_delivered")
    @Expose
    CarDelivered,
    @SerializedName("client_not_found")
    @Expose
    ClientNotFound,
    @SerializedName("order_postponed")
    @Expose
    OrderPostponed,
    @SerializedName("order_in_progress")
    @Expose
    OrderInProgress,
    @SerializedName("order_completed")
    @Expose
    OrderCompleted,
    @SerializedName("order_paid")
    @Expose
    OrderPaid,
    @SerializedName("order_closed")
    @Expose
    OrderClosed,
    @SerializedName("order_not_paid")
    @Expose
    OrderNotPaid,
    @SerializedName("canceled")
    @Expose
    Canceled,
    @SerializedName("force_canceled")
    @Expose
    ForceCanceled,
    @SerializedName("order_pending_payment")
    @Expose
    OrderPendingPayment
}
