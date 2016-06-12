package com.anashidgames.gerdoo.core.service;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.anashidgames.gerdoo.core.MultipartUtility;
import com.anashidgames.gerdoo.core.payment.Purchase;
import com.anashidgames.gerdoo.core.service.auth.AuthenticationInterceptor;
import com.anashidgames.gerdoo.core.service.auth.AuthenticationManager;
import com.anashidgames.gerdoo.core.service.call.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.call.PsychoCallBack;
import com.anashidgames.gerdoo.core.service.model.AchievementItem;
import com.anashidgames.gerdoo.core.service.model.AnswerFriendRequestParameter;
import com.anashidgames.gerdoo.core.service.model.Category;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
import com.anashidgames.gerdoo.core.service.model.ChangeImageParams;
import com.anashidgames.gerdoo.core.service.model.DoneResponse;
import com.anashidgames.gerdoo.core.service.model.EditProfileParameters;
import com.anashidgames.gerdoo.core.service.model.EditProfileResponse;
import com.anashidgames.gerdoo.core.service.model.FriendRequestParameters;
import com.anashidgames.gerdoo.core.service.model.GetScoreParams;
import com.anashidgames.gerdoo.core.service.model.GetSkillResponse;
import com.anashidgames.gerdoo.core.service.model.LeaderBoardParams;
import com.anashidgames.gerdoo.core.service.model.MatchData;
import com.anashidgames.gerdoo.core.service.model.Message;
import com.anashidgames.gerdoo.core.service.model.Rank;
import com.anashidgames.gerdoo.core.service.model.SearchParameters;
import com.anashidgames.gerdoo.core.service.model.SearchedTopic;
import com.anashidgames.gerdoo.core.service.model.SearchedUser;
import com.anashidgames.gerdoo.core.service.model.ShopCategoryData;
import com.anashidgames.gerdoo.core.service.model.ShopCategoryItemsParameter;
import com.anashidgames.gerdoo.core.service.model.ShopItem;
import com.anashidgames.gerdoo.core.service.model.UploadResponse;
import com.anashidgames.gerdoo.core.service.model.parameters.GetCategoryTopicsParams;
import com.anashidgames.gerdoo.core.service.model.parameters.GetSubCategoriesParams;
import com.anashidgames.gerdoo.core.service.model.server.ChangeImageResponse;
import com.anashidgames.gerdoo.core.service.model.server.FriendRequestResponse;
import com.anashidgames.gerdoo.core.service.model.Friend;
import com.anashidgames.gerdoo.core.service.model.Gift;
import com.anashidgames.gerdoo.core.service.model.HomeItem;
import com.anashidgames.gerdoo.core.service.model.ProfileInfo;
import com.anashidgames.gerdoo.core.service.model.UserInfo;
import com.anashidgames.gerdoo.core.service.realTime.GameManager;
import com.anashidgames.gerdoo.core.service.realTime.MatchMakingManager;
import com.anashidgames.gerdoo.utils.PsychoUtils;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.pegahtech.backtory.models.messages.MatchFoundMessage;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;

/**
 * Created by psycho on 3/22/16.
 */
public class GerdooServer{

    public static final String AUTHENTICATION_ID = "5734df81e4b05a0b0e955409";
    public static final String AUTHENTICATION_KEY = "5734df81e4b09a527aa07444";
    private static final String REAL_TIME_INSTANCE_ID = "57359624e4b05a0b0e955468";

    public static final String CLOUD_CODE_ID = "5734df82e4b05a0b0e95540d";

    public static final GerdooServer INSTANCE = new GerdooServer(AUTHENTICATION_ID, AUTHENTICATION_KEY);

//    public static final String HOST = "http://192.168.0.99:8585";
    public static final String HOST = "http://api.backtory.com/";
//    public static final String CDN_URL = "http://192.168.0.115:8034";/files";
    public static final String CDN_URL = "http://cdn.backtory.ir/files";
    public static final String CDN_SECRET_KEY = "JUFPBDNVNUWBLOEQIOVDJXZYDUOXUU";
    public static final String CDN_DOWNLOAD_PREFIX = "http://cdn.backtory.ir/chahar";


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

    public Call<List<HomeItem>> getHome() {
        return realService.getHome(CLOUD_CODE_ID);
    }

    public Call<List<CategoryTopic>> getCategoryTopics(String categoryId) {
        return realService.getCategoryTopics(CLOUD_CODE_ID, new GetCategoryTopicsParams(categoryId));
    }

    public Call<List<Category>> getSubCategories(String categoryId) {
        return realService.getSubCategories(CLOUD_CODE_ID, new GetSubCategoriesParams(categoryId));
    }

    public Call<List<Rank>> getTopPlayers(String leaderBoardId, int page) {
        return realService.getTopPlayers(CLOUD_CODE_ID, new LeaderBoardParams(leaderBoardId, page));
    }

    public Call<List<Rank>> getAroundMe(String leaderBoardId) {
        return realService.getAroundMe(CLOUD_CODE_ID, new LeaderBoardParams(leaderBoardId, 0));
    }

    public Call<UserInfo> getUserInfo() {
        return realService.getUserInfo(CLOUD_CODE_ID);
    }

    public Call<ProfileInfo> getProfile(String userId) {
        return service.getProfile(userId);
    }

    public Call<List<Friend>> getFriends(String userId) {
        return service.getFriends(userId);
    }

    public Call<List<Gift>> getGifts(String userId) {
        return service.getGifts(userId);
    }

    public Call<FriendRequestResponse> friendRequest(String userId) {
        return service.friendRequest(new FriendRequestParameters(userId));
    }

    public Call<FriendRequestResponse> unfriendRequest(String userId) {
        return service.unfriendRequest(new FriendRequestParameters(userId));
    }

    public Call<ChangeImageResponse> changeImage(String url, Uri selectedImage) {
        RequestBody file = RequestBody.create(MediaType.parse("image/*"), selectedImage.getPath());
        return service.changeImage(CDN_URL, url, null);
    }

    public Call<GetSkillResponse> getScore(String matchMakingName) {
        return realService.getScore(CLOUD_CODE_ID, new GetScoreParams(matchMakingName));
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public MatchMakingManager createMatchMakingManager(String matchMakingName) {
        return new MatchMakingManager(context, authenticationManager, REAL_TIME_INSTANCE_ID, matchMakingName);
    }

    public Call<MatchData> getMatchData(MatchFoundMessage matchFoundMessage) {
        return realService.getMatchData(CLOUD_CODE_ID, matchFoundMessage);
    }

    public GameManager createGameManager(MatchData matchData) {
        return new GameManager(authenticationManager, REAL_TIME_INSTANCE_ID, matchData);
    }

    private Call<ChangeImageResponse> changeImage(String newUrl) {
        return realService.changeImage(CLOUD_CODE_ID, new ChangeImageParams(newUrl));
    }

    public Call<List<ShopCategoryData>> getShopCategories() {
        return service.getShopCategories(CLOUD_CODE_ID);
    }

    public Call<EditProfileResponse> editProfile(String firstName, String lastName) {
        return service.editProfile(new EditProfileParameters(firstName, lastName));
    }

    public void changeImage(final InputStream inputStream, final PsychoCallBack<ChangeImageResponse> callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String name = PsychoUtils.randomString();
                try {
                    String dir = "/profiles/";
                    Calendar calendar = Calendar.getInstance();
                    dir += calendar.get(Calendar.YEAR) + "_" + calendar.get(Calendar.MONTH)
                            + "_" + calendar.get(Calendar.DAY_OF_MONTH) + "/";
                    String fileName = PsychoUtils.randomString() + ".jpg";

                    Log.i("four+", "Start requesting");
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("x-backtory-cdn-secret", CDN_SECRET_KEY);
                    MultipartUtility utility = new MultipartUtility(CDN_URL, "UTF-8", "bow", headers);
                    utility.addFormField("fileItems[0].path", dir);
                    utility.addFormField("fileItems[0].extract", "false");
                    utility.addFormField("fileItems[0].replacing", "false");
//                    utility.addFormField("secretToken", "BBB" + PsychoUtils.randomString());
                    utility.addFilePart("fileItems[0].fileToUpload", inputStream, fileName);
                    HttpURLConnection connection = utility.execute();
                    String response = IOUtils.toString(connection.getInputStream());
                    Log.i("four+", "Response is: " + response);

                    final List<String> urls = new Gson().fromJson(response, UploadResponse.class).getSavedFilesUrls();
                    if (!urls.isEmpty()) {
                        String newUrl = CDN_DOWNLOAD_PREFIX + urls.get(0);
                        Call<ChangeImageResponse> call = changeImage(newUrl);
                        call.enqueue(new CallbackWithErrorDialog<ChangeImageResponse>(context) {
                            @Override
                            public void handleSuccessful(ChangeImageResponse data) {
                                callback.handleSuccessful(data);
                            }
                        });
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.handleFailure(null, e);
                        }
                    });
                }
            }
        }).start();
    }



    private void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }

    public Call<List<AchievementItem>> getAllAchievements() {
        return service.getAllAchievements(CLOUD_CODE_ID);
    }

    public Call<List<Message>> getMessages() {
        return service.getMessages(CLOUD_CODE_ID);
    }


    public Call<List<SearchedTopic>> searchTopics(String query, int receivedCount) {
        return service.searchTopics(new SearchParameters(query, receivedCount));
    }

    public Call<List<SearchedUser>> searchUser(String query, int receivedCount) {
        return service.searchUser(new SearchParameters(query, receivedCount));
    }

    public Call<List<ShopItem>> getShopCategoryItems(String categoryId) {
        return service.getShopCategoryItems(new ShopCategoryItemsParameter(categoryId));
    }

    public Call<DoneResponse> sendPurchaseInfo(Purchase info) {
        return service.sendPurchaseInfo(info);
    }

    public Call<DoneResponse> answerFriendRequest(String userId, boolean answer) {
        return service.answerFriendRequest(new AnswerFriendRequestParameter(userId, answer));
    }
}
