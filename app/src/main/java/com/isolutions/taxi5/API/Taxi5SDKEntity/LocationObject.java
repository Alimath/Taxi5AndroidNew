package com.isolutions.taxi5.API.Taxi5SDKEntity;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class LocationObject {
    @SerializedName("name")
    @Expose
    public String nameOfObject;

    @SerializedName("type")
    @Expose
    public String type;

    public String getStringDescription() {
        if(!TextUtils.isEmpty(nameOfObject)) {
            return nameOfObject;
        }

        return null;
    }
}
