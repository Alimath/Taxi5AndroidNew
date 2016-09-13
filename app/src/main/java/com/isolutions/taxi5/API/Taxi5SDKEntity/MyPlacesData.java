package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by fedar.trukhan on 13.09.16.
 */

public class MyPlacesData {


    public ArrayList<LocationData> getPlacesData() {
        return placesData;
    }

    public void setPlacesData(ArrayList<LocationData> placesData) {
        this.placesData = placesData;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @SerializedName("places")
    @Expose
    private ArrayList<LocationData> placesData;

    @SerializedName("total")
    @Expose
    private Integer total;
}
