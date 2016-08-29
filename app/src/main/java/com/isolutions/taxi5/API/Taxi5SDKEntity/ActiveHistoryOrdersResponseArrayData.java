package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by fedar.trukhan on 29.08.16.
 */

public class ActiveHistoryOrdersResponseArrayData {
    @SerializedName("orders")
    @Expose
    ArrayList<OrderData> orders;

    @Expose
    @SerializedName("total")
    Integer total;
}
