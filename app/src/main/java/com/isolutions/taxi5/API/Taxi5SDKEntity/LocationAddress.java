package com.isolutions.taxi5.API.Taxi5SDKEntity;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class LocationAddress {
    @SerializedName("street")
    @Expose
    public String street;

    @SerializedName("building")
    @Expose
    public String building;

    @SerializedName("country")
    @Expose
    public String country;

    @SerializedName("settlement")
    @Expose
    public String settlement;

    @SerializedName("porch")
    @Expose
    public String porch;

    @SerializedName("apartment")
    @Expose
    public String apartment;

    @SerializedName("section")
    @Expose
    public String section;

    public String getStringDescription() {
        String retVal = "";
        if(!TextUtils.isEmpty(settlement)) {
            if(!TextUtils.equals(settlement, "Минск")) {
                retVal += settlement + ", ";
            }
        }

        if(!TextUtils.isEmpty(street)) {
            retVal += street + ", ";
        }

        if(!TextUtils.isEmpty(building)) {
            retVal += building;
        }

        if(!TextUtils.isEmpty(section)) {
            retVal += "к" + section;
        }

        return retVal;
    }
}
