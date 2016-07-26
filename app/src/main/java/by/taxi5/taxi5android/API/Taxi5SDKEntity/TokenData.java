package by.taxi5.taxi5android.API.Taxi5SDKEntity;

import com.google.gson.annotations.SerializedName;

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

        return localInstance;
    }

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("type")
    private String type;

    @SerializedName("expires_in")
    private String expiresIn;

    @SerializedName("refresh_token")
    private String refreshToken;

    public String GetDescription() {
        return "" + this.accessToken + ", " + this.type + ", " + this.expiresIn + ", " + this.refreshToken;
    }

    public TokenData() {

    }
}
