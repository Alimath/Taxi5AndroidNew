package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by fedar.trukhan on 13.09.16.
 */

public class MyPlacesResponseData {
    public MyPlacesData getResponseData() {
        return myPlacesData;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    @SerializedName("data")
    @Expose
    private MyPlacesData myPlacesData;

    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    @SerializedName("errors")
    @Expose
    private ArrayList<String> errors;
}
