package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class OrderStatus {
    @SerializedName("status")
    public OrderStatusType status;

    @SerializedName("is_terminal")
    public boolean isTerminal;
}
