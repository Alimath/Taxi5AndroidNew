package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class OrderFeatures {
    @SerializedName("rta")
    public boolean rta;

    @SerializedName("animal")
    public boolean animal;

    @SerializedName("baby")
    public boolean baby;

    @SerializedName("escort")
    public boolean escort;

    @SerializedName("terminal")
    public boolean terminal;

    @SerializedName("wifi")
    public boolean wifi;

    @SerializedName("vehicle")
    public String vehicle;
}
