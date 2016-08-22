package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class LocationResponseData {
    public LocationData getResponseData() {
        return locationData;
    }

    public Integer getStatusCode() {
        return new Integer(statusCode);
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    @SerializedName("data")
    @Expose
    private LocationData locationData;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("errors")
    @Expose
    private ArrayList<String> errors;

}