package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class LocationData {
    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude;

    @SerializedName("type")
    public String type;


    @SerializedName("description")
    public LocationDescription locationDescription;

    @SerializedName("details")
    public LocationDescription details;


}
