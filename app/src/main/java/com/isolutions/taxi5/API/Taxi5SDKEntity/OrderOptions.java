package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class OrderOptions {
    @SerializedName("developer")
    @Expose
    public Boolean developer;

    @SerializedName("ignored")
    @Expose
    public Boolean ignored;
}
