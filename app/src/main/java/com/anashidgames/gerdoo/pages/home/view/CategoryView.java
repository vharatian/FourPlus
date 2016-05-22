package com.anashidgames.gerdoo.pages.home.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.model.Category;
import com.anashidgames.gerdoo.core.service.model.CategoryTopic;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.home.CategoryFragment;
import com.anashidgames.gerdoo.pages.topic.TopicActivity;
import com.anashidgames.gerdoo.view.row.ItemsRow;
import com.anashidgames.gerdoo.view.row.Row;
import com.anashidgames.gerdoo.view.row.RowItem;
import com.anashidgames.gerdoo.view.row.ScrollableRow;
import com.anashidgames.gerdoo.view.row.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 4/3/16.
 */
public class CategoryView extends ItemsRow<CategoryTopic> {


    private Category category;

    public CategoryView(Context context) {
        super(context);
    }

    public CategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CategoryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        setExpansionHeight((int) getResources().getDimension(R.dimen.categoryItemsRow));
    }

    @Override
    public Call<List<CategoryTopic>> getItems() {
        return GerdooServer.INSTANCE.getCategoryTopics(category.getCategoryId());
    }

    @Override
    public List<RowItem> convert(List<CategoryTopic> items) {
        List<RowItem> result = new ArrayList<>();
        for(CategoryTopic topic: items){
            result.add(new RowItem(topic.getTitle(), topic.getCategoryTitle(), topic.getImageUrl()
                    , TopicActivity.newIntent(getContext(), topic)));
        }
        return result;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category, boolean expanded, boolean toggleable) {
        this.category = category;
        Row row = new Row(category.getTitle(), category.getIconUrl(), category.getColor());
        setData(row, expanded, toggleable);
    }

    public void setCategory(String title, String categoryId) {
        setCategory(new Category(categoryId, title, null, false, null), true, false);
    }
}
