package com.isolutions.taxi5.API;

import com.isolutions.taxi5.API.Taxi5SDKEntity.ActiveHistoryOrdersResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationsListResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseActionData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by fedar.trukhan on 25.07.16.
 */

public interface Taxi5SDK {
    @GET("http://api.taxi5.by/oauth/auth")
    Call<Void> GetSMSCode(
            @Query("response_type") String responseType,
            @Query("msid") String msid,
            @Query("client_id") String clientID);

    @FormUrlEncoded
    @POST("http://api.taxi5.by/oauth/token")
    Call<TokenData> Authorization(
            @Field("grant_type") String grantType,
            @Field("msid") String msid,
            @Field("client_id") String clientID,
            @Field("client_secret") String clientSecret,
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST("http://api.taxi5.by/oauth/token")
    Call<TokenData> AuthorizationWithName(
            @Field("grant_type") String grantType,
            @Field("msid") String msid,
            @Field("name") String name,
            @Field("client_id") String clientID,
            @Field("client_secret") String clientSecret,
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST("http://api.taxi5.by/oauth/token")
    Call<TokenData> RefreshToken(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientID,
            @Field("client_secret") String clientSecret,
            @Field("refresh_token") String refreshToken
    );

    @GET("http://api.taxi5.by/m/v2/customer/me")
    Call<ProfileResponseData> GetProfile(
            @Header("X-Authorization") String token
    );

    @GET("http://api.taxi5.by/m/v2/order/{id}")
    Call<OrderResponseData> ReadOrderStatus (
            @Header("X-Authorization") String token,
            @Path("id") Integer orderID
    );

    @GET("http://api.taxi5.by/m/v2/geocoding")
    Call<LocationsListResponseData> ReverseGeocode (
        @Header("X-Authorization") String token,
        @Query("latitude") Double latitude,
        @Query("longitude") Double longitude,
        @Query("details") Boolean details
    );

    @POST("http://api.taxi5.by/m/v2/order")
    Call<OrderResponseData> SendOrderRequest(
            @Header("X-Authorization") String token,
            @Body OrderData orderData
    );

    @POST("http://api.taxi5.by/m/v2/order/{id}/confirm")
    Call<OrderResponseActionData> ConfirmOrderWithID (
        @Header("X-Authorization") String token,
        @Path("id") Integer orderID
    );

    @POST("http://api.taxi5.by/m/v2/order/{id}/cancel")
    Call<OrderResponseActionData> CancelOrderWithID (
            @Header("X-Authorization") String token,
            @Path("id") Integer orderID
    );

    @POST("http://api.taxi5.by/m/v2/order/{id}/repeat")
    Call<OrderResponseActionData> RepeatOrder (
            @Header("X-Authorization") String token,
            @Path("id") Integer orderID
    );

    @GET("http://api.taxi5.by/m/v2/customer/me/active-orders")
    Call<ActiveHistoryOrdersResponseData> ActiveOrders (
            @Header("X-Authorization") String token
    );

    @GET("http://api.taxi5.by/m/v2/customer/me/orders-history?limit=30")
    Call<ActiveHistoryOrdersResponseData> HistoryOrders (
            @Header("X-Authorization") String token
    );

    @POST("http://api.taxi5.by/m/v2/customer/me")
    Call<OrderResponseActionData> SendProfile(
            @Header("X-Authorization") String token,
            @Body ProfileData profileData
    );

}
