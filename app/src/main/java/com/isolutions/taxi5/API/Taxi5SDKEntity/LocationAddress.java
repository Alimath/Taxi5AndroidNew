package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class LocationAddress {
    @SerializedName("street")
    public String street;

    @SerializedName("building")
    public String building;

    @SerializedName("country")
    public String country;

    @SerializedName("settlement")
    public String settlement;

    @SerializedName("porch")
    public int porch;

    @SerializedName("apartment")
    public int apartment;

    @SerializedName("section")
    public String section;
}
