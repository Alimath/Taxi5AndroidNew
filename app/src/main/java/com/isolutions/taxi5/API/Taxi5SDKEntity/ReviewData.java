package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class ReviewData {
    @SerializedName("civility")
    @Expose
    public Integer civility;

    @SerializedName("cleanliness")
    @Expose
    public Integer cleanliness;

    @SerializedName("velocity")
    @Expose
    public Integer velocity;

    @SerializedName("comment")
    @Expose
    public String comment;
}
