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
    private View lineView;
    private ImageView arrowView;

    protected ExpandableRelativeLayout.OnExpansionListener listenerWrapper;


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
        lineView =  findViewById(R.id.lineView);
        headerView = findViewById(R.id.titleLayout);
        arrowView = (ImageView) findViewById(R.id.arrowView);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category){
        setCategory(category, false, true);
    }

    public void setCategory(String title, String dataUrl){
        setCategory(new Category(title, null, dataUrl, false, -1), true, false);
    }


    public void setCategory(Category category, boolean expanded, boolean clickable){
        this.category = category;

        firstExpansion = true;
        expandableLayout.setExpanded(expanded);
        checkTopicsState();
        checkColor(category.getColor());
        setHeader(category, clickable);
    }

    private void setIcon(Category category) {
        if (category.getIconUrl() != null){
            iconView.setVisibility(VISIBLE);
            Glide.with(iconView.getContext()).load(category.getIconUrl()).into(iconView);
        }else{
            iconView.setVisibility(GONE);
        }
    }

    private void setHeader(Category category, boolean clickable) {
        setIcon(category);

        titleView.setText(category.getTitle());
        if (clickable) {
            initClickBehavior(category);
            arrowView.setImageResource(R.drawable.open);
        }else {
            headerView.setOnClickListener(null);
            arrowView.setImageResource(R.drawable.back);
        }
    }

    private void checkColor(int color) {
        if(color < 0){
            lineView.setVisibility(GONE);
            return;
        }


        if (color < 0x1000000)
            color += 0xff000000;

        arrowView.setColorFilter(color);
        iconView.setColorFilter(color);

        lineView.setVisibility(VISIBLE);
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
            topicsRow.setData(category.getTitle(), category.getIconUrl(), category.getDataUrl());
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

    public void setExpansionListener(ExpandableRelativeLayout.OnExpansionListener expansionListener) {
        if (expansionListener == null){
            this.listenerWrapper = null;
        }else {
            this.listenerWrapper = new InnerExpansionListener(expansionListener);
        }

        expandableLayout.setExpansionListener(this.listenerWrapper);
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
            if (!isEnabled())
                return;

            expandableLayout.toggle();

            checkTopicsState();
            checkBackgroundColor();
        }
    }

    private class OpenSubCategoryClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            activity.changeFragment(CategoryFragment.newInstance(category.getDataUrl(), category.getTitle()));
        }
    }

    private class InnerExpansionListener implements ExpandableRelativeLayout.OnExpansionListener {
        private ExpandableRelativeLayout.OnExpansionListener innerListener;
        public InnerExpansionListener(ExpandableRelativeLayout.OnExpansionListener innerListener) {
            this.innerListener = innerListener;
        }

        @Override
        public void expanded(View view) {
            innerListener.expanded(CategoryView.this);
        }

        @Override
        public void onExpansion(View view) {
            innerListener.onExpansion(CategoryView.this);
        }
    }
}
