package com.anashidgames.gerdoo.pages.profile.view;

import android.content.Context;
import android.util.AttributeSet;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.model.Friend;
import com.anashidgames.gerdoo.pages.profile.ProfileActivity;
import com.anashidgames.gerdoo.view.row.ItemsRow;
import com.anashidgames.gerdoo.view.row.RowItem;
import com.anashidgames.gerdoo.view.row.SimpleItemsRow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 4/17/16.
 */
public class FriendsRow extends ItemsRow<FriendItemRow, Friend> {

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
    public FriendItemRow newItemView(Context context) {
        return new FriendItemRow(context);
    }

    @Override
    public void bind(FriendItemRow view, Friend data) {
        view.setFriend(data);
    }
}
