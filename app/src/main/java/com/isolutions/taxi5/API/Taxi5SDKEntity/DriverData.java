package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class DriverData {
    @SerializedName("name")
    @Expose
    public String driverName;

    @SerializedName("msid")
    @Expose
    public String driverPhone;
}
