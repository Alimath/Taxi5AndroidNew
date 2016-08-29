package com.isolutions.taxi5.API.Taxi5SDKEntity;

import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;

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

    public static void setInstance(ProfileData instance) {
        ProfileData.instance = instance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsid() {
        return msid;
    }

    public void setMsid(String msid) {
        this.msid = msid;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public Integer getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Integer birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public Boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public Boolean isPasswordExists() {
        return isPasswordExists;
    }

    public void setPasswordExists(boolean passwordExists) {
        isPasswordExists = passwordExists;
    }

    public ProfileDataOptions getOptions() {
        return options;
    }

    public void setOptions(ProfileDataOptions options) {
        this.options = options;
    }

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("msid")
    @Expose
    private String msid;

    @SerializedName("avatar")
    @Expose
    private String avatarURL;

    @SerializedName("date_of_birth")
    @Expose
    private Integer birthDate;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("is_blocked")
    @Expose
    private Boolean isBlocked;

    @SerializedName("is_confirmed")
    @Expose
    private Boolean isConfirmed;

    @SerializedName("password_exists")
    @Expose
    private Boolean isPasswordExists;

    @SerializedName("options")
    @Expose
    private ProfileDataOptions options;

    public Bitmap getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(Bitmap avatarImage) {
        this.avatarImage = avatarImage;
    }

    private Bitmap avatarImage;

    private ProfileData() {
    }

    public void saveProfileData() {
        Paper.book().write("taxi5AndroidProfileData", this);
    }

    private void loadProfileData() {
        ProfileData tData = (ProfileData) Paper.book().read("taxi5AndroidProfileData");

        if(tData != null) {
            this.id = tData.id;
            this.name = tData.name;
            this.msid = tData.msid;
            this.avatarURL = tData.avatarURL;
            this.birthDate = tData.birthDate;
            this.email = tData.email;
            this.isBlocked = tData.isBlocked;
            this.isConfirmed = tData.isConfirmed;
            this.isPasswordExists = tData.isPasswordExists;

            this.options = tData.options;

            if(tData.avatarImage != null) {
                this.avatarImage = tData.avatarImage;
            }
        }
    }
}

