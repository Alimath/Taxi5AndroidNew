package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by fedar.trukhan on 24.08.16.
 */

public class OrderResponseActionData {
    public Integer getStatusCode() {
        return statusCode;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    @SerializedName("errors")
    @Expose
    private ArrayList<String> errors;
}
