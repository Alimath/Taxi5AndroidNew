package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class AmountData {
    @SerializedName("currency")
    public String currency;

    @SerializedName("value")
    public int value;
}
