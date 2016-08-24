package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class VehicleData {
    @SerializedName("license_tax")
    @Expose
    public String license_tax;

    @SerializedName("title")
    @Expose
    public String titleName;

    @SerializedName("color")
    @Expose
    public String colorOfVehicle;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("color_name")
    @Expose
    public String colorName;
}
