package com.isolutions.taxi5.API.Taxi5SDKEntity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fedar.trukhan on 10.08.16.
 */

public class ProfileDataOptions {

    @SerializedName("news_via_email")
    @Expose
    private Boolean newsViaEmail;

    @SerializedName("news_via_sms")
    @Expose
    private Boolean newsViaSMS;

    public Boolean isNewsViaSMS() {
        return newsViaSMS;
    }

    public void setNewsViaEmail(Boolean newsViaEmail) {
        this.newsViaEmail = newsViaEmail;
    }

    public void setNewsViaSMS(Boolean newsViaSMS) {
        this.newsViaSMS = newsViaSMS;
    }

    public Boolean isNewsViaEmail() {
        return newsViaEmail;
    }


    private ProfileDataOptions() {

    }
}