package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 10.08.16.
 */

public class ProfileDataOptions {

    @SerializedName("news_via_email")
    @Expose
    private boolean newsViaEmail;

    @SerializedName("news_via_sms")
    @Expose
    private boolean newsViaSMS;

    public boolean isNewsViaSMS() {
        return newsViaSMS;
    }

    public void setNewsViaEmail(boolean newsViaEmail) {
        this.newsViaEmail = newsViaEmail;
    }

    public void setNewsViaSMS(boolean newsViaSMS) {
        this.newsViaSMS = newsViaSMS;
    }

    public boolean isNewsViaEmail() {
        return newsViaEmail;
    }


    private ProfileDataOptions() {

    }
}