package com.anashidgames.gerdoo.pages.profile.view;

import android.content.Context;
import android.util.AttributeSet;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.model.Friend;
import com.anashidgames.gerdoo.pages.profile.ProfileActivity;
import com.anashidgames.gerdoo.view.row.ItemsRow;
import com.anashidgames.gerdoo.view.row.RowItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 4/17/16.
 */
public class FriendsRow extends ItemsRow<Friend> {

    private String userId;

    public FriendsRow(Context context) {
        super(context);
    }

    public FriendsRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FriendsRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FriendsRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setUserId(String userId){
        this.userId = userId;
        setData(getResources().getString(R.string.friends));
    }

    @Override
    public Call<List<Friend>> getItems() {
        return GerdooServer.INSTANCE.getFriends(userId);
    }

    @Override
    public List<RowItem> convert(List<Friend> friends) {
        List<RowItem> result = new ArrayList<>();
        for(Friend friend : friends){
            result.add(new RowItem(friend.getName(), null, friend.getImageUrl(),
                    ProfileActivity.newIntent(getContext(), friend.getUserId())));
        }
        return result;
    }
}
