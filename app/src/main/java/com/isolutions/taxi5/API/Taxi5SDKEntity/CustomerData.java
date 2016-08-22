package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class CustomerData {
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("msid")
    public String msid;
}
