package com.isolutions.taxi5.API.Taxi5SDKEntity;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class LocationDescription {
    @SerializedName("address")
    @Expose
    public LocationAddress address;

    @SerializedName("object")
    @Expose
    public LocationObject locationObject;


    public String getStringDescription() {
        if(locationObject != null) {
            if (!TextUtils.isEmpty(locationObject.getStringDescription())) {
                String retVal = locationObject.getStringDescription();
                if (address != null && !TextUtils.isEmpty(address.getStringDescription())) {
                    retVal += " (" + address.getStringDescription() + ")";
                }
                return retVal;
            }
        }
        if(address != null) {
            if (!TextUtils.isEmpty(address.getStringDescription())) {
                return address.getStringDescription();
            }
        }
        return null;
    }
}
