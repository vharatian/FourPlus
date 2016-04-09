package com.anashidgames.gerdoo.core.service;

import com.anashidgames.gerdoo.core.service.model.Category;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
import com.anashidgames.gerdoo.core.service.model.HomeItem;
import com.anashidgames.gerdoo.core.service.model.Rank;
import com.anashidgames.gerdoo.core.service.model.SignInParameters;
import com.anashidgames.gerdoo.pages.topic.list.PsychoListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by psycho on 3/22/16.
 */

interface GerdooService {

    @GET("/")
    Call<Boolean> checkSession(String sessionKey);

    @POST("/users")
    Call<String> signUp(String email, String password);

    @GET("/")
    Call<Boolean> sendForgetPasswordMail(String email);

    @POST("/login")
    Call<String> signIn(@Body SignInParameters parameters);

    @GET("/")
    Call<List<HomeItem>> getHome();

    @GET
    Call<List<CategoryTopic>> getCategoryTopics(@Url String url);

    @GET
    Call<List<Category>> getCategories(@Url String url);

    @GET
    Call<PsychoListResponse<Rank>> getRanking(@Url String url);
}
