package com.anashidgames.gerdoo.pages.home.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.home.CategoryFragment;
import com.anashidgames.gerdoo.pages.home.CategoryWrapper;
import com.anashidgames.gerdoo.pages.home.HomeActivity;
import com.bumptech.glide.Glide;

/**
 * Created by psycho on 3/30/16.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder {


    public static CategoryViewHolder newInstance(FragmentContainerActivity activity, ViewGroup parent) {
        View view = LayoutInflater.from(activity).inflate(R.layout.view_category, parent, false);
        return new CategoryViewHolder(activity, view);
    }

    private final FragmentContainerActivity activity;

    private TextView titleView;
    private ImageView iconView;
    private ExpandableRelativeLayout expandableLayout;
    private CategoryTopicsRow topicsRow;
    private View headerView;


    private boolean firstExpansion;


    private CategoryWrapper category;

    public CategoryViewHolder(FragmentContainerActivity activity, View itemView) {
        super(itemView);

        this.activity = activity;
        init(itemView);
    }

    private void init(View view) {
        titleView = (TextView) view.findViewById(R.id.titleView);
        iconView = (ImageView) view.findViewById(R.id.iconView);
        expandableLayout = (ExpandableRelativeLayout) view.findViewById(R.id.expandable);
        topicsRow = (CategoryTopicsRow) view.findViewById(R.id.topicsRow);

        headerView = view.findViewById(R.id.titleLayout);
    }

    public void setCategory(CategoryWrapper category){
        this.category = category;

        firstExpansion = true;
        titleView.setText(category.getTitle());
        Glide.with(iconView.getContext()).load(category.getIconUrl()).into(iconView);
        expandableLayout.setExpanded(category.isExpanded());
        checkTopicsState();
        checkBackgroundColor();

        if (category.hasSubCategory()){
            headerView.setOnClickListener(new OpenSubCategoryClickListener());
        }else{
            headerView.setOnClickListener(new ToggleClickListener());
        }
    }

    private void checkTopicsState() {
        if(expandableLayout.isExpanded() && firstExpansion){
            topicsRow.setData(null, category.getDataUrl());
            firstExpansion = false;
        }
    }

    private void checkBackgroundColor() {
        if (expandableLayout.isExpanded()){
            itemView.setBackgroundResource(R.color.categoryBackgroundOpen);
        }else {
            itemView.setBackgroundResource(R.color.categoryBackgroundClose);
        }
    }



    private class ToggleClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            category.toggle();
            expandableLayout.toggle();

            checkTopicsState();
            checkBackgroundColor();
        }
    }

    private class OpenSubCategoryClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            activity.changeFragment(CategoryFragment.newInstance(category.getDataUrl()));
        }
    }
}
