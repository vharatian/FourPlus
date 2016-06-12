package com.anashidgames.gerdoo.pages.profile.view;

import android.content.Context;
import android.util.AttributeSet;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.model.Gift;
import com.anashidgames.gerdoo.view.row.ItemsRow;
import com.anashidgames.gerdoo.view.row.RowItem;
import com.anashidgames.gerdoo.view.row.SimpleItemsRow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 4/17/16.
 */
public class GiftsRow extends SimpleItemsRow<Gift> {

    private String userId;

    public GiftsRow(Context context) {
        super(context);
    }

    public GiftsRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GiftsRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GiftsRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setUserId(String userId){
        this.userId = userId;
        setData(getResources().getString(R.string.gifts));
    }

    @Override
    public Call<List<Gift>> getList() {
        return GerdooServer.INSTANCE.getGifts(userId);
    }

    @Override
    public List<RowItem> convert(List<Gift> friends) {
        List<RowItem> result = new ArrayList<>();
        for(Gift gift : friends){
            result.add(new RowItem(gift.getName(), null, gift.getImageUrl()));
        }
        return result;
    }
}
