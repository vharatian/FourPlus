package com.anashidgames.gerdoo.pages.shop.view;

import android.content.Context;
import android.view.View;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.model.ShopItem;
import com.anashidgames.gerdoo.core.service.model.ShopCategoryData;
import com.anashidgames.gerdoo.view.row.ItemsRow;
import com.anashidgames.gerdoo.view.row.Row;
import com.anashidgames.gerdoo.view.row.RowItem;
import com.anashidgames.gerdoo.view.row.SimpleItemsRow;
import com.anashidgames.gerdoo.view.row.SimpleRowItemView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 6/12/16.
 */
public class ShopCategoryRow extends SimpleItemsRow<ShopItem> {
    private ShopCategoryData data;
    private OnShopItemSelectedListener onShopItemSelectedListener;

    public ShopCategoryRow(Context context) {
        super(context);
    }

    @Override
    protected void init(Context context) {
        super.init(context);

        setExpansionHeight((int) getResources().getDimension(R.dimen.categoryItemsRow));
    }

    public void setData(ShopCategoryData data) {
        this.data = data;
        Row row = new Row(data.getTitle(), null, null);
        setData(row, true, false);
    }

    @Override
    public Call<List<ShopItem>> getList() {
        return GerdooServer.INSTANCE.getShopCategoryItems(data.getCategoryId());
    }

    @Override
    public List<RowItem> convert(List<ShopItem> items) {
        List<RowItem> result = new ArrayList<>();
        for(ShopItem item : items){
            result.add(new RowItem(item.getTitle(), item.getSubTitle(), item.getImageUrl(), new OnRowItemSelectedListener(item)));
        }
        return result;
    }

    public void setOnShopItemSelectedListener(OnShopItemSelectedListener onShopItemSelectedListener) {
        this.onShopItemSelectedListener = onShopItemSelectedListener;
    }

    private class OnRowItemSelectedListener implements RowItem.OnClickListener {
        private final ShopItem shopItem;

        public OnRowItemSelectedListener(ShopItem item) {
            this.shopItem = item;
        }

        @Override
        public void onClick(View v, RowItem rowItem) {
            if (onShopItemSelectedListener != null){
                onShopItemSelectedListener.onSelected(shopItem);
            }
        }
    }

    public interface OnShopItemSelectedListener {
        void onSelected(ShopItem item);
    }
}
