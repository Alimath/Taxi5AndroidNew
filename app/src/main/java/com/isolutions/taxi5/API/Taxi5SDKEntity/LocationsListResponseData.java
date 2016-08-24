package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fedar.trukhan on 23.08.16.
 */

public class LocationsListResponseData {
    public ArrayList<LocationData> getResponseData() {
        return locationsData;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    @SerializedName("data")
    @Expose
    private ArrayList<LocationData> locationsData;

    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    @SerializedName("errors")
    @Expose
    private ArrayList<String> errors;
}
