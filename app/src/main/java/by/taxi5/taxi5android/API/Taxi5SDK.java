package by.taxi5.taxi5android.API;

import by.taxi5.taxi5android.API.Taxi5SDKEntity.TokenData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by fedar.trukhan on 25.07.16.
 */

public interface Taxi5SDK {
    @GET("http://api.taxi5.by/oauth/auth")
    Call<Void> GetSMSCode(@Query("response_type") String responseType, @Query("msid") String msid, @Query("client_id") String clientID);

    @FormUrlEncoded
    @POST("http://api.taxi5.by/oauth/token")
    Call<TokenData> Authorization(@Field("grant_type") String grantType,
                                  @Field("msid") String msid,
                                  @Field("client_id") String clientID,
                                  @Field("client_secret") String clientSecret,
                                  @Field("code") String code);
}
