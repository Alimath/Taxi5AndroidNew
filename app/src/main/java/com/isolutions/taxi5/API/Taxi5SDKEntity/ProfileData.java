package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Struct;

import io.paperdb.Paper;

/**
 * Created by fedar.trukhan on 10.08.16.
 */

public class ProfileData {
    private static volatile ProfileData instance;

    public static ProfileData getInstance() {
        ProfileData localInstance = instance;
        if(localInstance == null) {
            synchronized(ProfileData.class) {
                localInstance = instance;
                if(localInstance == null) {
                    instance = localInstance = new ProfileData();
                }
            }

        }

        localInstance.loadProfileData();


        return localInstance;
    }

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("msid")
    private String msid;

    @SerializedName("avatar")
    private String avatarURL;

    @SerializedName("date_of_birth")
    private String birthDate;

    @SerializedName("email")
    private String email;

    @SerializedName("is_blocked")
    private boolean isBlocked;

    @SerializedName("is_confirmed")
    private boolean isConfirmed;

    @SerializedName("password_exists")
    private boolean isPasswordExists;

    @SerializedName("options")
    @Expose
    private ProfileDataOptions options;

//    @SerializedName("access_token")
//    private String accessToken;
//
//    @SerializedName("type")
//    private String type;
//
//    @SerializedName("expires_in")
//    private String expiresIn;
//
//    @SerializedName("refresh_token")
//    private String refreshToken;

    public String getDescription() {
        if(options.isNewsViaEmail()) {
            return "" + name + ": true";
        }
        else {
            return "" + name + ": false";
        }
    }

    private ProfileData() {
//        if(Paper.book().read("taxi5AndroidTokenData") == null) {
//            Log.d("taxi5", "null saved data");
//        }
//        else {
//            return Paper.book().read("taxi5AndroidTokenData");
//        }
    }

    public void saveProfileData() {
        Paper.book().write("taxi5AndroidProfileData", this);
    }

    private void loadProfileData() {
        ProfileData tData = (ProfileData) Paper.book().read("taxi5AndroidProfileData");

//        if(tData != null) {
//            this.accessToken = tData.accessToken;
//            this.type = tData.type;
//            this.expiresIn = tData.expiresIn;
//            this.refreshToken = tData.refreshToken;
//        }
    }
}

