package com.anashidgames.gerdoo.pages.home.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.model.Category;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.home.CategoryFragment;
import com.bumptech.glide.Glide;

/**
 * Created by psycho on 4/3/16.
 */
public class CategoryView extends LinearLayout {

    private FragmentContainerActivity activity;

    private TextView titleView;
    private ImageView iconView;
    private ExpandableRelativeLayout expandableLayout;
    private CategoryTopicsRow topicsRow;
    private View headerView;

    protected OnExpansionListener expansionListener;


    private boolean firstExpansion;
    private Category category;

    public CategoryView(FragmentContainerActivity activity) {
        super(activity);
        this.activity = activity;

        init(activity);
    }

    public CategoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CategoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CategoryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_category, this);

        titleView = (TextView) findViewById(R.id.titleView);
        iconView = (ImageView) findViewById(R.id.iconView);
        expandableLayout = (ExpandableRelativeLayout) findViewById(R.id.expandable);
        topicsRow = (CategoryTopicsRow) findViewById(R.id.topicsRow);

        headerView = findViewById(R.id.titleLayout);
    }

    public void setCategory(Category category){
        setCategory(category, false, true);
    }

    public void setCategory(String title, String dataUrl){
        setCategory(new Category(title, null, dataUrl, false), true, false);
    }


    public void setCategory(Category category, boolean expanded, boolean clickable){
        this.category = category;

        firstExpansion = true;
        titleView.setText(category.getTitle());
        Glide.with(iconView.getContext()).load(category.getIconUrl()).into(iconView);
        expandableLayout.setExpanded(expanded);
        checkTopicsState();
        checkBackgroundColor();

        if (clickable) {
            initClickBehavior(category);
        }
    }

    private void initClickBehavior(Category category) {
        if (category.hasSubCategory()) {
            headerView.setOnClickListener(new OpenSubCategoryClickListener());
        } else {
            headerView.setOnClickListener(new ToggleClickListener());
        }
    }

    private void checkTopicsState() {
        if(expandableLayout.isExpanded() && firstExpansion){
            topicsRow.setData(category.getTitle(), category.getDataUrl(), category.getIconUrl());
            firstExpansion = false;
        }
    }

    private void checkBackgroundColor() {
        if (expandableLayout.isExpanded()){
            setBackgroundResource(R.color.categoryBackgroundOpen);
        }else {
            setBackgroundResource(R.color.categoryBackgroundClose);
        }
    }

    public void setExpansionListener(OnExpansionListener expansionListener) {
        this.expansionListener = expansionListener;
    }

    public void close(boolean animate){
        if (animate)
            expandableLayout.collapse();
        else
            expandableLayout.setExpanded(false);

        checkBackgroundColor();
    }

    public void open(boolean animate) {
        if (animate)
            expandableLayout.expand();
        else
            expandableLayout.setExpanded(true);

        checkBackgroundColor();
    }


    private class ToggleClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            expandableLayout.toggle();

            checkTopicsState();
            checkBackgroundColor();

            if (expansionListener != null)
                expansionListener.expanded(CategoryView.this);
        }
    }

    private class OpenSubCategoryClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            activity.changeFragment(CategoryFragment.newInstance(category.getDataUrl()));
        }
    }

    public interface OnExpansionListener{
        void expanded(View view);
    }


}
