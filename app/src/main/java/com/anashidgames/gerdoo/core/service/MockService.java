package com.anashidgames.gerdoo.core.service;

import com.anashidgames.gerdoo.core.service.model.AuthenticationInfo;
import com.anashidgames.gerdoo.core.service.model.Category;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
import com.anashidgames.gerdoo.core.service.model.ChangeImageResponse;
import com.anashidgames.gerdoo.core.service.model.FollowToggleResponse;
import com.anashidgames.gerdoo.core.service.model.Friend;
import com.anashidgames.gerdoo.core.service.model.Gift;
import com.anashidgames.gerdoo.core.service.model.HomeItem;
import com.anashidgames.gerdoo.core.service.model.ProfileInfo;
import com.anashidgames.gerdoo.core.service.model.Rank;
import com.anashidgames.gerdoo.core.service.model.SignUpInfo;
import com.anashidgames.gerdoo.core.service.model.SignUpParameters;
import com.anashidgames.gerdoo.core.service.model.UserInfo;
import com.anashidgames.gerdoo.pages.topic.list.PsychoListResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Part;
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
    public Call<SignUpInfo> signUp(String authenticationId, String authenticationKey, SignUpParameters parameters) {
        SignUpInfo info;
        if(parameters.getEmail().equals("test@test.com") && parameters.getPassword().equals("test")){
            info = new SignUpInfo(parameters.getEmail());
        }else{
            info = new SignUpInfo(null);
        }

        return behaviorDelegate.returningResponse(info).signUp(authenticationId, authenticationKey, parameters);
    }

    @Override
    public Call<Boolean> sendForgetPasswordMail(String email) {
        return behaviorDelegate.returningResponse(random.nextBoolean()).sendForgetPasswordMail(email);
    }

    @Override
    public Call<AuthenticationInfo> signIn(String authenticationId, String authenticationKey, String email, String password) {
        AuthenticationInfo info;
        if(email.equals("test@test.com") && password.equals("test")){
            info = new AuthenticationInfo(UUID.randomUUID().toString(), UUID.randomUUID().toString(), 4000);
        }else{
            info = new AuthenticationInfo(null, null, 0);
        }

        return behaviorDelegate.returningResponse(info).signIn(authenticationId, authenticationKey, email, password);
    }

    @Override
    public Call<List<HomeItem>> getHome() {
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
                item = new HomeItem(HomeItem.TYPE_BANNER_CATEGORY, bannerUrl, title + i, categoryBannerImageAspectRatio, bannerUrl);
            }else if (rand == 1){
                item = new HomeItem(HomeItem.TYPE_BANNER_URL, urlBannerImageUrl, urlBannerImageAspectRatio, urlBannerImageUrl);
            }else {
                item = new HomeItem(HomeItem.TYPE_CATEGORY, HOME, title + i);
            }

            response.add(item);
        }

        return behaviorDelegate.returningResponse(response).getHome();
    }

    @Override
    public Call<List<CategoryTopic>> getCategoryTopics(@Url String url) {
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

            String itemTitle = title;
            while(random.nextBoolean()) {
                itemTitle += title;
            }

            String categoryTitle = "";
            if (url != null && url.contains(HOME)) {
                categoryTitle = title;
                while (random.nextBoolean()) {
                    categoryTitle += title;
                }
            }

            itemTitle += i;

            int myRank = random.nextInt(3000);
            response.add(new CategoryTopic(imageUrl, "احسان خواجه امیری", categoryTitle, "", "", MY_RANK + myRank, "https://i.imgsafe.org/c77e5e5.jpg", myRank));
        }

        return behaviorDelegate.returningResponse(response).getCategoryTopics(url);
    }

    @Override
    public Call<List<Category>> getCategories(@Url String url) {
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
        List<Integer> colors = Arrays.asList(
            0x248a8a,
            0x16bfbf,
            0xa0e5e3,
            0x238080,
            0xd2f2ed
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

            Integer color = colors.get(random.nextInt(colors.size()));
            response.add(new Category(categoryTitle, imageUrl, "", random.nextInt()%4 == 0, color));
        }


        return behaviorDelegate.returningResponse(response).getCategories(url);
    }

    @Override
    public Call<PsychoListResponse<Rank>> getRanking(@Url String url) {
        List<Rank> ranking = new ArrayList<>();
        String name = "اسم فامیل ";

        String nextPage = "";
        int rank = 1;
        int size = 20;
        if(url.contains(MY_RANK)){
            size = 5;
            nextPage = null;
            rank = Integer.parseInt(url.replace(MY_RANK, "")) - 2;
            if (rank < 0)
                rank = 0;
        }else if(url != null && !url.isEmpty()) {
            rank = Integer.parseInt(url);
        }

        if (nextPage != null){
            if (random.nextInt()%8 == 0)
                nextPage = null;
            else
                nextPage += (rank + size);
        }


        for(int i = 0; i< size; i++, rank++){

            String rankName = name;
            while(random.nextBoolean()) {
                rankName += name;
            }

            int rankChange = 0;
            if (random.nextBoolean())
                rankChange = random.nextInt(1000) - 500;

            ranking.add(new Rank(rank, Math.abs(random.nextInt(10000000)), rankName, "" , rankChange));
        }



        PsychoListResponse<Rank> response = new PsychoListResponse<>(ranking, nextPage);

        return behaviorDelegate.returningResponse(response).getRanking(url);
    }

    @Override
    public Call<UserInfo> getUserInfo() {
        UserInfo response = new UserInfo("http://indiabright.com/wp-content/uploads/2015/11/profile_picture_by_kyo_tux-d4hrimy.png", "اسم و فامیل");
        return behaviorDelegate.returningResponse(response).getUserInfo();
    }

    @Override
    public Call<ProfileInfo> getProfile(Long userId) {
        String proPicUrl = "http://indiabright.com/wp-content/uploads/2015/11/profile_picture_by_kyo_tux-d4hrimy.png";
        String coverUrl = "https://i.imgsafe.org/c77e5e5.jpg";

        String followToggleUrl = "url";
        Boolean following = random.nextBoolean();
        if (userId == null){
            followToggleUrl = null;
            following = null;
        }

        Boolean online = (userId == null) || random.nextBoolean();

        int loss = random.nextInt(100);
        int tie = random.nextInt(100-loss);
        int win = 100-loss-tie;

        ProfileInfo info = new ProfileInfo("اسم فامیل", proPicUrl, coverUrl, followToggleUrl, following, proPicUrl, coverUrl, online, "", "", "", "", win, loss, tie, random.nextInt(10000));

        return behaviorDelegate.returningResponse(info).getProfile(userId);
    }

    @Override
    public Call<List<Friend>> getFriends(Long userId) {
        List<Friend> response = new ArrayList<>();
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
            response.add(new Friend(imageUrl, name, Math.abs(random.nextLong())));
        }

        return behaviorDelegate.returningResponse(response).getFriends(userId);
    }

    @Override
    public Call<List<Gift>> getGifts(Long userId) {
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
    public Call<FollowToggleResponse> toggleFollow(@Url String followToggleUrl) {
        FollowToggleResponse response = new FollowToggleResponse(random.nextBoolean(), "url");
        return behaviorDelegate.returningResponse(response).toggleFollow(followToggleUrl);
    }

    @Override
    public Call<ChangeImageResponse> changeImage(@Url String url, @Part("image") RequestBody body) {
        boolean done = random.nextBoolean();
        String newUrl = null;
        if (done)
            newUrl = url;

        ChangeImageResponse response = new ChangeImageResponse(done, newUrl);

        return behaviorDelegate.returningResponse(response).changeImage(url, body);

    }
}
