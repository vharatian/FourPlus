package com.anashidgames.gerdoo.core.service;

import com.anashidgames.gerdoo.core.service.model.Category;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
import com.anashidgames.gerdoo.core.service.model.ChangeImageParams;
import com.anashidgames.gerdoo.core.service.model.FileParams;
import com.anashidgames.gerdoo.core.service.model.GetScoreParams;
import com.anashidgames.gerdoo.core.service.model.GetSkillParams;
import com.anashidgames.gerdoo.core.service.model.GetSkillResponse;
import com.anashidgames.gerdoo.core.service.model.LeaderBoardParams;
import com.anashidgames.gerdoo.core.service.model.MatchData;
import com.anashidgames.gerdoo.core.service.model.Rank;
import com.anashidgames.gerdoo.core.service.model.ShopItem;
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
import com.anashidgames.gerdoo.pages.topic.list.PsychoListResponse;

import java.util.List;
import java.util.Map;

import ir.pegahtech.backtory.models.messages.MatchFoundMessage;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by psycho on 3/22/16.
 */

interface GerdooService {

    @POST("/lambda/{cloudCodeId}/getHome")
    Call<List<HomeItem>> getHome(@Path("cloudCodeId") String cloudCodeId);

    @POST("/lambda/{cloudCodeId}/getCategoryTopics")
    Call<List<CategoryTopic>> getCategoryTopics(
            @Path("cloudCodeId") String cloudCodeId,
            @Body GetCategoryTopicsParams params
    );

    @POST("/lambda/{cloudCodeId}/getSubCategories")
    Call<List<Category>> getSubCategories(
            @Path("cloudCodeId") String cloudCodeId,
            @Body GetSubCategoriesParams params
    );

    @POST("/lambda/{cloudCodeId}/getTopPlayers")
    Call<List<Rank>> getTopPlayers(
            @Path("cloudCodeId") String cloudId,
            @Body LeaderBoardParams params
    );

    @POST("/lambda/{cloudCodeId}/getAroundMe")
    Call<List<Rank>> getAroundMe(
            @Path("cloudCodeId") String cloudId,
            @Body LeaderBoardParams params
    );

    @POST("/lambda/{cloudCodeId}/getMySkill")
    Call<GetSkillResponse> getScore(
            @Path("cloudCodeId") String cloudId,
            @Body GetScoreParams params
    );

    @POST("/lambda/{cloudCodeId}/getMatchData")
    Call<MatchData> getMatchData(
            @Path("cloudCodeId") String cloudCodeId,
            @Body MatchFoundMessage matchFoundMessage
    );

    @POST("/lambda/{cloudCodeId}/getBriefProfile")
    Call<UserInfo> getUserInfo(
            @Path("cloudCodeId") String cloudCodeId
    );

    @GET("/")
    Call<ProfileInfo> getProfile(String userId);

    @GET("/")
    Call<List<Friend>> getFriends(String userId);

    @GET("/")
    Call<List<Gift>> getGifts(String userId);

    @GET("/")
    Call<FollowToggleResponse> toggleFollow(@Url String followToggleUrl);

    @Multipart
    @POST()
    Call<ChangeImageResponse> changeImage(
            @Url String cdnUrl,
            @Header("x-backtory-cdn-secret") String secretKey,
            @PartMap Map<String, RequestBody> parts
    );

    @POST("/lambda/{cloudCodeId}/changeProfileImage")
    Call<ChangeImageResponse> changeImage(
            @Path("cloudCodeId") String cloudId,
            @Body ChangeImageParams params
    );

    @POST("/lambda/{cloudCodeId}/getShopItems")
    Call<List<ShopItem>> getShopItems(@Path("cloudCodeId") String cloudId);
}
