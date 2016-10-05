package com.isolutions.taxi5.APIAssist;

import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderResponseActionData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ReviewData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;
import com.isolutions.taxi5.APIAssist.Entities.AssistOrderStatusResponseData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by fedar.trukhan on 29.09.16.
 */

public interface AssistSDK {
    @FormUrlEncoded
    @POST("https://pay140.paysec.by/orderresult/orderresult.cfm")
    Call<AssistOrderStatusResponseData> GetOrderStatus(
            @Field("Login") String Login,
            @Field("Password") String Password,
            @Field("Merchant_ID") String Merchant_ID,
            @Field("OrderNumber") String OrderNumber,
            @Field("Format") Integer Format,
            @Field("StartDay") String StartDay,
            @Field("StartMonth") String StartMonth,
            @Field("StartYear") String StartYear
    );

    @FormUrlEncoded
    @POST("https://pay140.paysec.by/cancel/cancel.cfm")
    Call<AssistOrderStatusResponseData> CancelPayment(
            @Field("Login") String Login,
            @Field("Password") String Password,
            @Field("Merchant_ID") String Merchant_ID,
            @Field("Billnumber") String Billnumber,
            @Field("Format") Integer Format
    );

    @FormUrlEncoded
    @POST("https://pay140.paysec.by/recurrent/rp.cfm")
    Call<AssistOrderStatusResponseData> PayReccurent(
            @Field("Billnumber") String BillNumber,
            @Field("OrderNumber") String OrderNumber,
            @Field("Merchant_ID") String Merchant_ID,
            @Field("Login") String Login,
            @Field("Password") String Password,
            @Field("Amount") Double Amount,
            @Field("Currency") String Currency,
            @Field("Format") String Format
    );

}
