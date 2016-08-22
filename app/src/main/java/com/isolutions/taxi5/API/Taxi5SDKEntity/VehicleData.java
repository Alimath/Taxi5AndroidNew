package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class VehicleData {
    @SerializedName("license_tax")
    public String license_tax;

    @SerializedName("title")
    public String titleName;

    @SerializedName("color")
    public String colorOfVehicle;

    @SerializedName("type")
    public String type;

    @SerializedName("color_name")
    public String colorName;
}
