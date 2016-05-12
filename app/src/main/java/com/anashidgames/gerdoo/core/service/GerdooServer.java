package com.anashidgames.gerdoo.core.service;

import android.content.Context;
import android.net.Uri;

import com.anashidgames.gerdoo.core.service.auth.AuthenticationInterceptor;
import com.anashidgames.gerdoo.core.service.auth.AuthenticationManager;
import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
import com.anashidgames.gerdoo.core.service.model.Category;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
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
import com.anashidgames.gerdoo.core.service.model.SignUpInfo;
import com.anashidgames.gerdoo.core.service.model.UserInfo;
import com.anashidgames.gerdoo.core.service.model.server.LeaderBoardResponse;
import com.anashidgames.gerdoo.core.service.realTime.GameManager;
import com.anashidgames.gerdoo.core.service.realTime.MatchMakingManager;

import java.util.List;

import ir.pegahtech.backtory.models.messages.MatchFoundMessage;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;

/**
 * Created by psycho on 3/22/16.
 */
public class GerdooServer{

    public static final String AUTHENTICATION_ID = "57163ee5e4b0cad8c4dd1844";
    public static final String AUTHENTICATION_KEY = "57163ee5e4b093ed2821a011";
    private static final String REALTIME_INSTANCE_ID = "57284ac1e4b01c017afb4015";

    public static final String CLOUD_CODE_ID = "57163effe4b0cad8c4dd184a";
    public static final String GAME_ID = "57163f07e4b0cad8c4dd184c";

    public static final GerdooServer INSTANCE = new GerdooServer(AUTHENTICATION_ID, AUTHENTICATION_KEY);

//    public static final String HOST = "http://192.168.0.99:8585";
    public static final String HOST = "http://api1.backtory.com/";


    private GerdooService mockService;
    private GerdooService realService;
    private GerdooService service;

    private final AuthenticationManager authenticationManager;
    private Context context;


    private GerdooServer(String authenticationId, String authenticationKey) {

        authenticationManager = new AuthenticationManager(HOST, authenticationId, authenticationKey);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(authenticationManager))
                .addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build();

        MockRetrofit mockRetrofit = new MockRetrofit.Builder(retrofit).build();

        realService = retrofit.create(GerdooService.class);
        BehaviorDelegate<GerdooService> behaviorDelegate = mockRetrofit.create(GerdooService.class);
        mockService = new MockService(behaviorDelegate);

        service = mockService;
    }

    public void setContext(Context context) {
        this.context = context;
        authenticationManager.setContext(context);
    }

    public void signUp(String email, String phoneNumber, String password, Callback<AuthenticationInfo> callback) {
        authenticationManager.signUp(email, phoneNumber, password, callback);
    }

    public Call<Boolean> sendForgetPasswordMail(String email) {
        return authenticationManager.sendForgetPasswordMail(email);
    }

    public Call<AuthenticationInfo>  signIn(String email, String password) {
        return authenticationManager.signIn(email, password);
    }

    public Call<List<HomeItem>> getHome() {
        return realService.getHome(CLOUD_CODE_ID);
    }

    public Call<List<CategoryTopic>> getCategoryTopics(String categoryId) {
        return realService.getCategoryTopics(CLOUD_CODE_ID, new GetCategoryTopicsParams(categoryId));
    }

    public Call<List<Category>> getSubCategories(String categoryId) {
        return realService.getSubCategories(CLOUD_CODE_ID, new GetSubCategoriesParams(categoryId));
    }

    public Call<List<Rank>> getTopPlayers(String leaderBoardId) {
        return realService.getTopPlayers(CLOUD_CODE_ID, new LeaderBoardParams(leaderBoardId));
    }

    public Call<List<Rank>> getAroundMe(String leaderBoardId) {
        return realService.getAroundMe(CLOUD_CODE_ID, new LeaderBoardParams(leaderBoardId));
    }

    public Call<UserInfo> getUserInfo() {
        return realService.getUserInfo(CLOUD_CODE_ID);
    }

    public Call<ProfileInfo> getProfile(Long userId) {
        return service.getProfile(userId);
    }

    public Call<List<Friend>> getFriends(Long userId) {
        return service.getFriends(userId);
    }

    public Call<List<Gift>> getGifts(Long userId) {
        return service.getGifts(userId);
    }

    public Call<FollowToggleResponse> toggleFollow(String followToggleUrl) {
        return service.toggleFollow(followToggleUrl);
    }

    public Call<ChangeImageResponse> changeImage(String url, Uri selectedImage) {
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), selectedImage.getPath());
        return service.changeImage(url, body);
    }

    public Call<GetSkillResponse> getSkill() {
        return realService.getSkill(CLOUD_CODE_ID);
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public MatchMakingManager createMatchMakingManager(String matchMakingName) {
        return new MatchMakingManager(context, authenticationManager, REALTIME_INSTANCE_ID, matchMakingName);
    }

    public Call<MatchData> getMatchData(MatchFoundMessage matchFoundMessage) {
        return realService.getMatchData(CLOUD_CODE_ID, matchFoundMessage);
    }

    public GameManager createGameManager(MatchData matchData) {
        return new GameManager(authenticationManager, REALTIME_INSTANCE_ID, matchData);
    }

    public void gustSignUp(Callback<AuthenticationInfo> callback) {
        authenticationManager.gustSignUp(callback);
    }
}
