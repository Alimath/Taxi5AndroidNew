package com.isolutions.taxi5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileResponseData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 25.07.16.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
        Call<TokenData> call = taxi5SDK.RefreshToken("refresh_token", "taxi5_ios_app", "cri2thrauoau6whucizem8aukeo9traa", TokenData.getInstance().getRefreshToken());

        call.enqueue(new Callback<TokenData>() {
            @Override
            public void onResponse(Call<TokenData> call, Response<TokenData> response) {
                onResponseRefreshToken(call, response);
            }

            @Override
            public void onFailure(Call<TokenData> call, Throwable t) {
                onFailureRefreshToken(call, t);
            }
        });


    }

    public void onResponseRefreshToken(Call<TokenData> call, Response<TokenData> response) {
        if(response.code() == 200) {
            response.body().setAuthorized(true);
            response.body().saveTokenData();
            Log.d("taxi5", TokenData.getInstance().getDescription());

            Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
            Call<ProfileResponseData> profileDataCall = taxi5SDK.GetProfile(TokenData.getInstance().getType() + " " + TokenData.getInstance().getAccessToken());

            profileDataCall.enqueue(new Callback<ProfileResponseData>() {
                @Override
                public void onResponse(Call<ProfileResponseData> call, Response<ProfileResponseData> response) {
                    onResponseGetProfile(call, response);
                }

                @Override
                public void onFailure(Call<ProfileResponseData> call, Throwable t) {
                    onFailureGetProfile(call, t);
                }
            });
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            //TODO: make a clear
        }
    }

    public void onFailureRefreshToken(Call<TokenData> call, Throwable t) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onResponseGetProfile(Call<ProfileResponseData> call, Response<ProfileResponseData> response) {
        if(response.isSuccessful()) {
            response.body().getProfileData().saveProfileData();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    public void onFailureGetProfile(Call<ProfileResponseData> call, Throwable t) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        Log.d("taxi5", "failure load profile");
    }
}
