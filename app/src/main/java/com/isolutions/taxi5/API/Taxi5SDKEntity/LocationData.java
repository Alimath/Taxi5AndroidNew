package com.isolutions.taxi5.API.Taxi5SDKEntity;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.isolutions.taxi5.AppData;
import com.isolutions.taxi5.R;

import java.lang.reflect.Type;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class LocationData {
    @SerializedName("latitude")
    @Expose
    public Double latitude;

    @SerializedName("longitude")
    @Expose
    public Double longitude;

    @SerializedName("type")
    @Expose
    public String type;
    
    public LocationDescription locationDescription;
    public String locationStringDescription;

    @SerializedName("details")
    public LocationDescription details;


    public String getStringDescription() {
        if(locationDescription != null) {
            if(!TextUtils.isEmpty(locationDescription.getStringDescription())) {
                return locationDescription.getStringDescription();
            }
        }
        else if(details != null) {
            if(!TextUtils.isEmpty(details.getStringDescription())) {
                return details.getStringDescription();
            }
        }
        else if(!TextUtils.isEmpty(locationStringDescription)) {
            return locationStringDescription;
        }
        else {
            String longitudeString = Location.convert(longitude, Location.FORMAT_SECONDS);
            String latitudeString = Location.convert(latitude, Location.FORMAT_SECONDS);

            String[] separatedLng = longitudeString .split(":");
            String[] separatedLat = latitudeString.split(":");

            String latNS = "N";
            if(latitude < 0) {
                latNS = "S";
            }
            String longWE = "E";
            if(longitude < 0) {
                longWE = "W";
            }

            Context appCtx = AppData.getInstance().getAppContext();

            longitudeString = separatedLng[0] + appCtx.getString(R.string.degree_sign) +
                    separatedLng[1] + appCtx.getString(R.string.degree_minute_sign) +
                    separatedLng[2].split(",")[0] + appCtx.getString(R.string.degree_second_sign)+longWE;

            latitudeString = separatedLat[0] + appCtx.getString(R.string.degree_sign) +
                    separatedLat[1] + appCtx.getString(R.string.degree_minute_sign) +
                    separatedLat[2].split(",")[0] + appCtx.getString(R.string.degree_second_sign)+latNS;

            return latitudeString + " " + longitudeString;
        }

        return "";
    }
}
