package com.anashidgames.gerdoo.core.service;

import com.anashidgames.gerdoo.core.service.model.Category;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
import com.anashidgames.gerdoo.core.service.model.GetSkillParams;
import com.anashidgames.gerdoo.core.service.model.GetSkillResponse;
import com.anashidgames.gerdoo.core.service.model.LeaderBoardParams;
import com.anashidgames.gerdoo.core.service.model.MatchData;
import com.anashidgames.gerdoo.core.service.model.Rank;
import com.anashidgames.gerdoo.core.service.model.parameters.GetCategoryTopicsParams;
import com.anashidgames.gerdoo.core.service.model.parameters.GetSubCategoriesParams;
import com.anashidgames.gerdoo.core.service.model.server.ChangeImageResponse;
import com.anashidgames.gerdoo.core.service.model.server.FollowToggleResponse;
import com.anashidgames.gerdoo.core.service.model.Friend;
import com.anashidgames.gerdoo.core.service.model.Gift;
import com.anashidgames.gerdoo.core.service.model.HomeItem;
import com.anashidgames.gerdoo.core.service.model.ProfileInfo;
import com.anashidgames.gerdoo.core.service.model.UserInfo;
import com.anashidgames.gerdoo.core.service.model.server.LeaderBoardResponse;

import java.util.List;

import ir.pegahtech.backtory.models.messages.MatchFoundMessage;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by psycho on 3/22/16.
 */

interface GerdooService {

    @POST("/api/lambda/{cloudCodeId}/getHome")
    Call<List<HomeItem>> getHome(@Path("cloudCodeId") String cloudCodeId);

    @POST("/api/lambda/{cloudCodeId}/getCategoryTopics")
    Call<List<CategoryTopic>> getCategoryTopics(
            @Path("cloudCodeId") String cloudCodeId,
            @Body GetCategoryTopicsParams params
    );

    @POST("/api/lambda/{cloudCodeId}/getSubCategories")
    Call<List<Category>> getSubCategories(
            @Path("cloudCodeId") String cloudCodeId,
            @Body GetSubCategoriesParams params
    );

    @POST("/api/lambda/{cloudCodeId}/getTopPlayers")
    Call<List<Rank>> getTopPlayers(
            @Path("cloudCodeId") String cloudId,
            @Body LeaderBoardParams params
    );

    @POST("/api/lambda/{cloudCodeId}/getAroundMe")
    Call<List<Rank>> getAroundMe(
            @Path("cloudCodeId") String cloudId,
            @Body LeaderBoardParams params
    );

    @POST("/api/lambda/{cloudCodeId}/getMySkill")
    Call<GetSkillResponse> getSkill(
            @Path("cloudCodeId") String cloudId
    );

    @POST("/api/lambda/{cloudCodeId}/getMatchData")
    Call<MatchData> getMatchData(
            @Path("cloudCodeId") String cloudCodeId,
            @Body MatchFoundMessage matchFoundMessage
    );

    @POST("/api/lambda/{cloudCodeId}/getBriefProfile")
    Call<UserInfo> getUserInfo(
            @Path("cloudCodeId") String cloudCodeId
    );

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
