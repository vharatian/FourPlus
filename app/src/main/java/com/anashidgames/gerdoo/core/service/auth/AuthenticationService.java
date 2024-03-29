package com.anashidgames.gerdoo.core.service.auth;

import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
import com.anashidgames.gerdoo.core.service.model.CompleteRegistrationParameters;
import com.anashidgames.gerdoo.core.service.model.GustSignUpInfo;
import com.anashidgames.gerdoo.core.service.model.SignUpInfo;
import com.anashidgames.gerdoo.core.service.model.parameters.SignUpParameters;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by psycho on 4/23/16.
 */
public interface AuthenticationService {
    @POST("/auth/users")
    Call<SignUpInfo> signUp(@Body SignUpParameters parameters);

    @POST("/auth/login")
    Call<AuthenticationInfo> signIn(@Query("username") String email, @Query("password") String password);

    @GET("/")
    Call<Boolean> sendForgetPasswordMail(String email);

    @FormUrlEncoded
    @Headers("X-Backtory-Authentication-Refresh: 1")
    @POST("/auth/login")
    Call<AuthenticationInfo> refreshToken(@Field("refresh_token") String refreshToken);

    @POST("/auth/guest-users")
    Call<GustSignUpInfo> gustSignUp();

    @POST("/auth/guest-users/complete-registration")
    Call<SignUpInfo> completeRegistration(@Body CompleteRegistrationParameters signUpParameters);
}
