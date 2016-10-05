package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 05.10.16.
 */

public class OrderPayments {
    public String getState() {
        return state;
    }

    @SerializedName("state")
    @Expose
    private String state;
}
