package com.anashidgames.gerdoo.core.service;

import com.anashidgames.gerdoo.core.service.model.Category;
import com.anashidgames.gerdoo.core.service.model.CategoryItem;
import com.anashidgames.gerdoo.core.service.model.HomeItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by psycho on 3/22/16.
 */
interface GerdooService {

    @GET("/")
    Call<Boolean> checkSession(String sessionKey);

    @GET("/")
    Call<String> signUp(String email, String password);

    @GET("/")
    Call<Boolean> sendForgetPasswordMail(String email);

    @GET("/")
    Call<String> signIn(String email, String password);

    @GET("/")
    Call<List<HomeItem>> getHome();

    @GET
    Call<List<CategoryItem>> getCategoryItems(@Url String url);

    @GET
    Call<List<Category>> getCategories(@Url String url);
}
