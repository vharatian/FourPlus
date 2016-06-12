package com.anashidgames.gerdoo.view.row;

import android.content.Context;
import android.util.AttributeSet;

import com.anashidgames.gerdoo.core.service.call.ConverterCall;

import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 6/12/16.
 */
public abstract class SimpleItemsRow<T> extends ItemsRow<SimpleRowItemView, RowItem> {
    public SimpleItemsRow(Context context) {
        super(context);
    }

    public SimpleItemsRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleItemsRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SimpleItemsRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
    }

    @Override
    public SimpleRowItemView newItemView(Context context) {
        return new SimpleRowItemView(context);
    }

    @Override
    public void bind(SimpleRowItemView view, RowItem data) {
        view.setData(data);
    }

    @Override
    public Call<List<RowItem>> getItems() {
        Call<List<T>> call = getList();
        return new InnerConverterCall(call);
    }

    protected abstract Call<List<T>> getList();

    public abstract List<RowItem> convert(List<T> items);

    private class InnerConverterCall extends ConverterCall<List<RowItem>,List<T>> {
        public InnerConverterCall(Call<List<T>> innerCall) {
            super(innerCall);
        }

        @Override
        public List<RowItem> convert(List<T> data) {
            return SimpleItemsRow.this.convert(data);
        }
    }
}
