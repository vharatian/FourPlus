package com.anashidgames.gerdoo.core.service;

import com.anashidgames.gerdoo.core.service.model.Category;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
import com.anashidgames.gerdoo.core.service.model.ChangeImageResponse;
import com.anashidgames.gerdoo.core.service.model.FollowToggleResponse;
import com.anashidgames.gerdoo.core.service.model.Friend;
import com.anashidgames.gerdoo.core.service.model.Gift;
import com.anashidgames.gerdoo.core.service.model.HomeItem;
import com.anashidgames.gerdoo.core.service.model.parameters.LeaderBoardParams;
import com.anashidgames.gerdoo.core.service.model.ProfileInfo;
import com.anashidgames.gerdoo.core.service.model.Rank;
import com.anashidgames.gerdoo.core.service.model.UserInfo;
import com.anashidgames.gerdoo.core.service.model.server.LeaderBoardResponse;
import com.anashidgames.gerdoo.pages.topic.list.PsychoListResponse;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by psycho on 3/22/16.
 */

interface GerdooService {

    @GET("/")
    Call<List<HomeItem>> getHome();

    @GET
    Call<List<CategoryTopic>> getCategoryTopics(@Url String url);

    @GET
    Call<List<Category>> getCategories(@Url String url);

    @POST("/api/game/leaderboards/top")
    Call<LeaderBoardResponse> getRanking(@Header("X-Backtory-Game-Id") String gameId, @Body LeaderBoardParams params);

    @GET("/")
    Call<UserInfo> getUserInfo();

    @GET("/")
    Call<ProfileInfo> getProfile(Long userId);

    @GET("/")
    Call<List<Friend>> getFriends(Long userId);

    @GET("/")
    Call<List<Gift>> getGifts(Long userId);

    @GET("/")
    Call<FollowToggleResponse> toggleFollow(@Url String followToggleUrl);

    @GET()
    Call<ChangeImageResponse> changeImage(@Url String url, @Part("image") RequestBody body);
}
