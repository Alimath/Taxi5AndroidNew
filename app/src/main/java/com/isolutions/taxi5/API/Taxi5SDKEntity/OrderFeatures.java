package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class OrderFeatures {
    @SerializedName("rta")
    @Expose
    public Boolean rta;

    @SerializedName("animal")
    @Expose
    public Boolean animal;

    @SerializedName("baby")
    @Expose
    public Boolean baby;

    @SerializedName("escort")
    @Expose
    public Boolean escort;

    @SerializedName("terminal")
    @Expose
    public Boolean terminal;

    @SerializedName("wifi")
    @Expose
    public Boolean wifi;

    @SerializedName("vehicle")
    @Expose
    public String vehicle;
}
