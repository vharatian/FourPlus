package com.anashidgames.gerdoo.core.service;

import com.anashidgames.gerdoo.core.payment.Purchase;
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
import com.anashidgames.gerdoo.core.service.model.parameters.GetCategoryTopicsParams;
import com.anashidgames.gerdoo.core.service.model.parameters.GetSubCategoriesParams;
import com.anashidgames.gerdoo.core.service.model.server.ChangeImageResponse;
import com.anashidgames.gerdoo.core.service.model.server.FriendRequestResponse;
import com.anashidgames.gerdoo.core.service.model.Friend;
import com.anashidgames.gerdoo.core.service.model.Gift;
import com.anashidgames.gerdoo.core.service.model.HomeItem;
import com.anashidgames.gerdoo.core.service.model.ProfileInfo;
import com.anashidgames.gerdoo.core.service.model.UserInfo;
import com.anashidgames.gerdoo.core.service.model.server.LeaderBoardResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import ir.pegahtech.backtory.models.messages.MatchFoundMessage;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Url;
import retrofit2.mock.BehaviorDelegate;

/**
 * Created by psycho on 3/22/16.
 */
class MockService implements GerdooService {


    public static final String MY_RANK = "MyRank,";
    public static final String HOME = "home";
    private BehaviorDelegate<GerdooService> behaviorDelegate;
    private Random random;

    public MockService(BehaviorDelegate<GerdooService> behaviorDelegate) {
        this.behaviorDelegate = behaviorDelegate;
        random = new Random(System.currentTimeMillis());
    }

    @Override
    public Call<List<HomeItem>> getHome(String instanceId) {
        List<HomeItem> response = new ArrayList<>();
        List<String> banners = Arrays.asList(
                "https://i.imgsafe.org/c26dc81.jpg",
                "https://i.imgsafe.org/c6595c3.jpg"
        );


        double categoryBannerImageAspectRatio = 3;
        String urlBannerImageUrl = "https://i.imgsafe.org/5b75feb.jpg";
        double urlBannerImageAspectRatio = 8.372;
        String title = "عنوان   ";

        int dataSize = 10 + random.nextInt(15);
        for(int i = 0; i< dataSize; i++){
            HomeItem item;
            int rand = random.nextInt(4);
            if (rand == 0){
                String bannerUrl = banners.get(random.nextInt(banners.size()));
                item = new HomeItem(title + i, bannerUrl, categoryBannerImageAspectRatio, "");
            }else if (rand == 1){
                item = new HomeItem(urlBannerImageUrl, urlBannerImageAspectRatio, urlBannerImageUrl);
            }else {
                item = new HomeItem(title + i, HOME);
            }

            response.add(item);
        }

        return behaviorDelegate.returningResponse(response).getHome(instanceId);
    }

    @Override
    public Call<List<CategoryTopic>> getCategoryTopics(String cloudCodeId, GetCategoryTopicsParams params) {
        List<CategoryTopic> response = new ArrayList<>();
        List<String> images = Arrays.asList(
                "https://i.imgsafe.org/2fb7d09.png",
                "https://i.imgsafe.org/3059ad7.png",
                "https://i.imgsafe.org/3118243.png",
                "https://i.imgsafe.org/31aff12.png",
                "https://i.imgsafe.org/32c6fc4.png"
        );
        String title = "عنوان  ";

        int dataSize = 5 + random.nextInt(15);
        for(int i = 0; i< dataSize; i++){
            String imageUrl = images.get(random.nextInt(images.size()));

            String categoryTitle = title;
            while (random.nextBoolean()) {
                categoryTitle += title;
            }

            response.add(new CategoryTopic(imageUrl, "احسان خواجه امیری", categoryTitle, "", "https://i.imgsafe.org/c77e5e5.jpg"));
        }

        return behaviorDelegate.returningResponse(response).getCategoryTopics(cloudCodeId, params);
    }

    @Override
    public Call<List<Category>> getSubCategories(String cloudCodeId, GetSubCategoriesParams params) {
        List<Category> response = new ArrayList<>();
        List<String> images = Arrays.asList(
                "https://i.imgsafe.org/c2218e1.png",
                "https://i.imgsafe.org/c2642df.png",
                "https://i.imgsafe.org/c31308b.png",
                "https://i.imgsafe.org/c734641.png",
                "https://i.imgsafe.org/c4853e0.png",
                "https://i.imgsafe.org/c8a4323.png",
                "https://i.imgsafe.org/c7d4087.png"
        );
        List<String> colors = Arrays.asList(
            "248a8a",
            "16bfbf",
            "a0e5e3",
            "238080",
            "d2f2ed"
        );
        String title = "عنوان  ";

        int dataSize = 10 + random.nextInt(30);
        for(int i = 0; i< dataSize; i++){
            String imageUrl = images.get(random.nextInt(images.size()));

            String categoryTitle = title;
            while(random.nextBoolean()) {
                categoryTitle += title;
            }

            categoryTitle += i;

            String color = colors.get(random.nextInt(colors.size()));
            response.add(new Category("", categoryTitle, imageUrl, random.nextInt()%4 == 0, color));
        }


        return behaviorDelegate.returningResponse(response).getSubCategories(cloudCodeId, params);
    }

    @Override
    public Call<List<Rank>> getTopPlayers(String cloudId, LeaderBoardParams params) {
        List<Rank> items = new ArrayList<>();
        String firstName = "اسم";
        String lastName = "فامیل";


        for(int i = 0; i< 20; i++, i++){
            items.add(new Rank(i, random.nextInt(1000000), firstName + " " + lastName, UUID.randomUUID().toString()));
        }


        LeaderBoardResponse response = new LeaderBoardResponse(items, 14);
        return behaviorDelegate.returningResponse(response).getTopPlayers(cloudId, params);
    }

    @Override
    public Call<List<Rank>> getAroundMe(String cloudId, LeaderBoardParams params) {
        List<Rank> items = new ArrayList<>();
        String firstName = "اسم";
        String lastName = "فامیل";


        for(int i = 0; i< 20; i++, i++){
            items.add(new Rank(i, random.nextInt(1000000), firstName + " " + lastName, UUID.randomUUID().toString()));
        }


        LeaderBoardResponse response = new LeaderBoardResponse(items, 14);
        return behaviorDelegate.returningResponse(response).getAroundMe(cloudId, params);
    }

    @Override
    public Call<GetSkillResponse> getScore(@Path("cloudCodeId") String cloudId, @Body GetScoreParams params) {
        return null;
    }

    @Override
    public Call<MatchData> getMatchData(@Path("cloudCodeId") String cloudCodeId, @Body MatchFoundMessage matchFoundMessage) {
        return null;
    }

    @Override
    public Call<UserInfo> getUserInfo(String cloudCodeId) {
        UserInfo response = new UserInfo("http://indiabright.com/wp-content/uploads/2015/11/profile_picture_by_kyo_tux-d4hrimy.png", "اسم و فامیل");
        return behaviorDelegate.returningResponse(response).getUserInfo(cloudCodeId);
    }

    @Override
    public Call<ProfileInfo> getProfile(String userId) {
        String proPicUrl = "http://indiabright.com/wp-content/uploads/2015/11/profile_picture_by_kyo_tux-d4hrimy.png";
        String coverUrl = "https://i.imgsafe.org/c77e5e5.jpg";

        String followToggleUrl = "url";
        int friendshipState = 1 + random.nextInt(3);
        if (userId == null){
            followToggleUrl = null;
            friendshipState = ProfileInfo.FRIEND_SHIP_STATE_NON;
        }

        Boolean online = (userId == null) || random.nextBoolean();

        int loss = random.nextInt(100);
        int tie = random.nextInt(100-loss);
        int win = 100-loss-tie;

        ProfileInfo info = new ProfileInfo("اسم فامیل", proPicUrl, coverUrl, followToggleUrl, friendshipState, proPicUrl, coverUrl, online, "", "", "", "", win, loss, tie, random.nextInt(10000), "userId");

        return behaviorDelegate.returningResponse(info).getProfile(userId);
    }

    @Override
    public Call<List<Friend>> getFriends(String userId) {
        List<Friend> response = new ArrayList<>();
        List<String> images = Arrays.asList(
                "https://i.imgsafe.org/2fb7d09.png",
                "https://i.imgsafe.org/3059ad7.png",
                "https://i.imgsafe.org/3118243.png",
                "https://i.imgsafe.org/31aff12.png",
                "https://i.imgsafe.org/32c6fc4.png"
        );
        String title = "عنوان  ";

        int dataSize = 5 + random.nextInt(15);
        for(int i = 0; i< dataSize; i++){
            String imageUrl = images.get(random.nextInt(images.size()));

            String name = title;
            while(random.nextBoolean()) {
                name += title;
            }

            name += i;

            response.add(new Friend(imageUrl, name, UUID.randomUUID().toString(), random.nextBoolean()));
        }

        return behaviorDelegate.returningResponse(response).getFriends(userId);
    }

    @Override
    public Call<List<Gift>> getGifts(String userId) {
        List<Gift> response = new ArrayList<>();
        List<String> images = Arrays.asList(
                "https://i.imgsafe.org/2fb7d09.png",
                "https://i.imgsafe.org/3059ad7.png",
                "https://i.imgsafe.org/3118243.png",
                "https://i.imgsafe.org/31aff12.png",
                "https://i.imgsafe.org/32c6fc4.png"
        );
        String name = "احسان خواجه امیری";

        int dataSize = 5 + random.nextInt(15);
        for(int i = 0; i< dataSize; i++){
            String imageUrl = images.get(random.nextInt(images.size()));
            response.add(new Gift(name, imageUrl));
        }

        return behaviorDelegate.returningResponse(response).getGifts(userId);
    }

    @Override
    public Call<FriendRequestResponse> friendRequest(@Body FriendRequestParameters parameter) {
        FriendRequestResponse response = new FriendRequestResponse(random.nextBoolean());
        return behaviorDelegate.returningResponse(response).friendRequest(parameter);
    }

    @Override
    public Call<FriendRequestResponse> unfriendRequest(@Body FriendRequestParameters parameter) {
        FriendRequestResponse response = new FriendRequestResponse(random.nextBoolean());
        return behaviorDelegate.returningResponse(response).friendRequest(parameter);
    }

    @Override
    public Call<ChangeImageResponse> changeImage(@Url String cdnUrl, @Header("x-backtory-cdn-secret") String instanceId, @PartMap Map<String, RequestBody> parts){
        boolean done = random.nextBoolean();
        String newUrl = null;
        if (done)
            newUrl = instanceId;

        ChangeImageResponse response = new ChangeImageResponse(done, newUrl);

        return behaviorDelegate.returningResponse(response).changeImage(cdnUrl, instanceId, parts);

    }

    @Override
    public Call<ChangeImageResponse> changeImage(@Path("cloudCodeId") String cloudId, @Body ChangeImageParams params) {
        return null;
    }

    @Override
    public Call<List<ShopCategoryData>> getShopCategories(@Path("cloudCodeId") String cloudId) {
        List<ShopCategoryData> items = new ArrayList<>();

        for (int i = 0; i<20; i++){
            items.add(new ShopCategoryData("", "پکیج فروشی"));
        }

        return behaviorDelegate.returningResponse(items).getShopCategories(cloudId);
    }

    @Override
    public Call<List<AchievementItem>> getAllAchievements(@Path("cloudCodeId") String cloudCodeId) {
        List<AchievementItem> items = new ArrayList<>();
        String imageUrl = "http://images.akamai.steamusercontent.com/ugc/35239975375061668/ACB134564983985B087613F528F1038B1B5F6ADA/";

        for (int i=0; i<20; i++){
            items.add(new AchievementItem("title" + i, imageUrl, random.nextBoolean()));
        }

        return behaviorDelegate.returningResponse(items).getAllAchievements(cloudCodeId);
    }

    @Override
    public Call<List<Message>> getMessages(@Path("cloudCodeId") String cloudCodeId) {
        List<Message> messages = new ArrayList<>();
        for (int i=0; i<20; i++){
            messages.add(new Message("title" + i, "message" + i));
        }
        return behaviorDelegate.returningResponse(messages).getMessages(cloudCodeId);
    }

    @Override
    public Call<EditProfileResponse> editProfile(@Body EditProfileParameters editProfileParameters) {
        EditProfileResponse response = new EditProfileResponse(random.nextBoolean());
        return behaviorDelegate.returningResponse(response).editProfile(editProfileParameters);
    }

    @Override
    public Call<List<SearchedUser>> searchUser(@Body SearchParameters searchParameters) {
        List<SearchedUser> result = new ArrayList<>();

        String imageUrl = "http://indiabright.com/wp-content/uploads/2015/11/profile_picture_by_kyo_tux-d4hrimy.png";

        for (int i=0; i<20; i++){
            result.add(new SearchedUser(imageUrl, "نام نام خانوادگی", Math.abs(random.nextInt())));
        }
        return behaviorDelegate.returningResponse(result).searchUser(searchParameters);
    }

    @Override
    public Call<List<ShopItem>> getShopCategoryItems(@Body ShopCategoryItemsParameter shopCategoryItemsParameter) {
        List<ShopItem> items = new ArrayList<>();
        String imageUrl = "http://indiabright.com/wp-content/uploads/2015/11/profile_picture_by_kyo_tux-d4hrimy.png";


        for(int i=0; i<20; i++){
            items.add(new ShopItem(imageUrl, "فروشی", "فروشی", ""));
        }
        return behaviorDelegate.returningResponse(items).getShopCategoryItems(shopCategoryItemsParameter);
    }

    @Override
    public Call<DoneResponse> sendPurchaseInfo(@Body Purchase info) {
        DoneResponse response = new DoneResponse(random.nextBoolean());
        return behaviorDelegate.returningResponse(response).sendPurchaseInfo(info);
    }

    @Override
    public Call<DoneResponse> answerFriendRequest(@Body AnswerFriendRequestParameter answerFriendRequestParameter) {
        DoneResponse response = new DoneResponse(random.nextBoolean());
        return behaviorDelegate.returningResponse(response).answerFriendRequest(answerFriendRequestParameter);
    }


    @Override
    public Call<List<SearchedTopic>> searchTopics(@Body SearchParameters searchParameters) {
        List<SearchedTopic> result = new ArrayList<>();

        String imageUrl = "https://i.imgsafe.org/3118243.png";

        for (int i=0; i<20; i++){
            result.add(new SearchedTopic(imageUrl, "عنوان", "زیر عنوان"));
        }
        return behaviorDelegate.returningResponse(result).searchTopics(searchParameters);
    }
}
