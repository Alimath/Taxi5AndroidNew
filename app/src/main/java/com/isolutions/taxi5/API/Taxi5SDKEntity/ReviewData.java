package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class ReviewData {
    @SerializedName("civility")
    public int civility;

    @SerializedName("cleanliness")
    public int cleanliness;

    @SerializedName("velocity")
    public int velocity;

    @SerializedName("comment")
    public String comment;
}
