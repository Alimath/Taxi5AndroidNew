package com.isolutions.taxi5.API.Taxi5SDKEntity;

import android.media.session.MediaSession;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.paperdb.Paper;

/**
 * Created by fedar.trukhan on 25.07.16.
 */

public class TokenData {
    private static volatile TokenData instance;

    public static TokenData getInstance() {
        TokenData localInstance = instance;
        if(localInstance == null) {
            synchronized(TokenData.class) {
                localInstance = instance;
                if(localInstance == null) {
                    instance = localInstance = new TokenData();
                }
            }

        }

        localInstance.loadTokenData();


        return localInstance;
    }
    public String getToken() {
        return type+" "+accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getType() {
        return type;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @SerializedName("access_token")
    @Expose
    private String accessToken;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("expires_in")
    @Expose
    private String expiresIn;

    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;

    public Boolean getAuthorized() {
        return isAuthorized;
    }

    public void setAuthorized(Boolean authorized) {
        isAuthorized = authorized;
    }

    private Boolean isAuthorized = false;

    public String getDescription() {
        return "" + this.accessToken + ", " + this.type + ", " + this.expiresIn + ", " + this.refreshToken + ", " + this.isAuthorized;
    }

    private TokenData() {
//        if(Paper.book().read("taxi5AndroidTokenData") == null) {
//            Log.d("taxi5", "null saved data");
//        }
//        else {
//            return Paper.book().read("taxi5AndroidTokenData");
//        }
    }

    public void saveTokenData() {
        Paper.book().write("taxi5AndroidTokenData", this);
    }

    private void loadTokenData() {
        TokenData tData = (TokenData) Paper.book().read("taxi5AndroidTokenData");

        if(tData != null) {
            this.accessToken = tData.accessToken;
            this.type = tData.type;
            this.expiresIn = tData.expiresIn;
            this.refreshToken = tData.refreshToken;
            this.isAuthorized = tData.isAuthorized;
        }
        else {
            this.accessToken = null;
            this.type = null;
            this.expiresIn = null;
            this.refreshToken = null;
            this.isAuthorized = null;
        }
    }

    public static void ClearTokenData() {
        Paper.book().delete("taxi5AndroidTokenData");
    }
}
