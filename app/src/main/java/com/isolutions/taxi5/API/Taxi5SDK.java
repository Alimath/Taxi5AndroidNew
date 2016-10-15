package com.isolutions.taxi5.API;

import com.isolutions.taxi5.API.Taxi5SDKEntity.ActiveHistoryOrdersResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.EstimatedPriceAndRouteResponceData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.LocationsListResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.MyPlacesData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.MyPlacesResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseActionData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.PlanResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ReviewData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
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
            @Query("client_id") String clientID
    );

    @GET("http://api.taxi5.by/oauth/auth")
    Call<Void> GetSMSWithName(
            @Query("response_type") String responseType,
            @Query("msid") String msid,
            @Query("client_id") String clientID,
            @Query("name") String regName
    );

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

    @GET("http://api.taxi5.by/m/v2/customer/me/active-orders?delay=5")
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

    @POST("http://api.taxi5.by/m/v2/order/{id}/review")
    Call<OrderResponseActionData> SendReview(
            @Header("X-Authorization") String token,
            @Path("id") Integer orderID,
            @Body ReviewData reviewData
    );

    @GET("http://api.taxi5.by/m/v2/plans")
    Call<PlanResponseData> GetPlans(
            @Header("X-Authorization") String token
    );


    @GET("http://api.taxi5.by/m/v2/geocoding?details=true")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<LocationsListResponseData> SearchAddresses(
            @Header("X-Authorization") String token,
            @Query("query") String searchString
    );


    @GET("http://api.taxi5.by/m/v2/routing")
    Call<EstimatedPriceAndRouteResponceData> GetPriceAndRoute(
            @Header("X-Authorization") String token,
            @Query("start[latitude]") double startLatitude,
            @Query("start[longitude]") double startLongitude,
            @Query("finish[latitude]") double endLatitude,
            @Query("finish[longitude]") double endLongitude
    );

    @GET("http://api.taxi5.by/m/v2/customer/me/favorite-places?noname=true&limit=100")
    Call<MyPlacesResponseData> GetMyPlaces (
            @Header("X-Authorization") String token
    );

    @FormUrlEncoded
    @POST("http://api.taxi5.by/m/v2/order/{id}/payments")
    Call<Void> CheckPaymentRequest(
            @Header("X-Authorization") String token,
            @Path("id") Integer orderID,
            @Field("provider") String provider,
            @Field("payment_identity") String paymentIdentity,
            @Field("amount") Integer amount,
            @Field("merchant") String merchantID
    );

    @FormUrlEncoded
    @POST("http://api.taxi5.by/m/v2/payments/state/canceled")
    Call<Void> CancelCheckPaymentRequest(
            @Header("X-Authorization") String token,
            @Field("provider") String provider,
            @Field("payment_identity") String paymentIdentity
    );

    @FormUrlEncoded
    @POST("http://api.taxi5.by/m/v2/customer/me/favorite-places/{placeID}")
    Call<Void> UpdateFavoritePlace(
            @Header("X-Authorization") String token,
            @Path("placeID") Integer placeID,
            @Field("alias") String alias,
            @Field("is_favorite") boolean isFavorite
    );

    @DELETE("http://api.taxi5.by/m/v2/customer/me/favorite-places/{placeID}")
    Call<Void> DeleteFavoritePlace(
            @Header("X-Authorization") String token,
            @Path("placeID") Integer placeID
    );
}
