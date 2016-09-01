package com.isolutions.taxi5.API.Taxi5SDKEntitySerialezersDeserialezers;

/**
 * Created by fedar.trukhan on 29.08.16.
 */

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;

public class ProfileDataSerializer implements JsonSerializer<ProfileData> {
    @Override
    public JsonElement serialize(ProfileData src, Type typeOfSrc, JsonSerializationContext context) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//            registerTypeAdapter(LocationData.class, new LocationDataSerializer()).create();

        JsonObject result = (JsonObject)gson.toJsonTree(src);
        result.remove("avatar");
        if(src.getAvatarImage() != null) {
            String encodedImage = encodeToBase64(src.getAvatarImage(), Bitmap.CompressFormat.PNG, 100);
            result.addProperty("avatar", "data:image/png;base64,"+encodedImage);
        }


        return result;
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
