package com.anashidgames.gerdoo.view.row;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.PsychoImageLoader;

/**
 * Created by psycho on 4/3/16.
 */
public abstract class ItemsRow<V extends View, T> extends LinearLayout implements ScrollableRow.DataProvider<T>, ScrollableRow.RowAdapter<V, T> {

    private TextView titleView;
    private ImageView iconView;
    private ExpandableRelativeLayout expandableLayout;
    private ScrollableRow<V, T> itemsRow;
    private View headerView;
    private View lineView;
    private ImageView arrowView;

    protected ExpandableRelativeLayout.OnExpansionListener listenerWrapper;
    private ShowAllListener showAllListener;


    private boolean firstExpansion;
    private Row row;
    private boolean toggleable;

    public ItemsRow(Context context) {
        super(context);
        init(context);
    }

    public ItemsRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ItemsRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ItemsRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    protected void init(Context context) {
        inflate(context, R.layout.view_items_row, this);

        titleView = (TextView) findViewById(R.id.titleView);
        iconView = (ImageView) findViewById(R.id.iconView);
        expandableLayout = (ExpandableRelativeLayout) findViewById(R.id.expandable);
        itemsRow = (ScrollableRow) findViewById(R.id.topicsRow);
        lineView =  findViewById(R.id.lineView);
        headerView = findViewById(R.id.titleLayout);
        arrowView = (ImageView) findViewById(R.id.arrowView);

        itemsRow.setDataProvider(this);
        itemsRow.setRowAdapter(this);
    }

    public void setExpansionHeight(int height){
        itemsRow.setExpansionHeight(height);
    }

    public Row getRow() {
        return row;
    }

    public void setData(Row row){
        setData(row, false, true);
    }

    public void setData(String title){
        setData(new Row(title, null, null), true, false);
    }


    public void
    setData(Row row, boolean expanded, boolean toggleable){
        this.row = row;
        this.toggleable = toggleable;

        firstExpansion = true;
        expandableLayout.setExpanded(expanded);
        checkItemsState();
        checkColor(row.getColor());
        setHeader(row);
    }

    private void setIcon(Row row) {
        if (row.getIconUrl() != null){
            iconView.setVisibility(VISIBLE);
            PsychoImageLoader.loadImage(iconView.getContext(), row.getIconUrl(), iconView);
        }else{
            iconView.setVisibility(GONE);
        }
    }

    public void setRowAdapter(ScrollableRow.RowAdapter<V, T> rowAdapter) {
        itemsRow.setRowAdapter(rowAdapter);
    }

    private void setHeader(Row row) {
        setIcon(row);

        titleView.setText(row.getTitle());
        headerView.setOnClickListener(new ToggleClickListener());
        setArrowIcon();
    }

    private void setArrowIcon() {
        if (expandableLayout.isExpanded() || !toggleable)
//            arrowView.setImageResource(R.drawable.back);
            arrowView.setImageDrawable(null);
        else
            arrowView.setImageResource(R.drawable.open);
    }

    private void checkColor(String color) {
        if(color == null || color.isEmpty()){
            lineView.setVisibility(GONE);
            return;
        }

        if (color.charAt(0) != '#'){
            color = "#" + color;
        }

        int colorValue = Color.parseColor(color);


        arrowView.setColorFilter(colorValue);
        iconView.setColorFilter(colorValue);

        lineView.setVisibility(VISIBLE);
    }

    private void checkItemsState() {
        if(expandableLayout.isExpanded() && firstExpansion){
            itemsRow.setData();
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

        setArrowIcon();
    }

    public void open(boolean animate) {
        if (animate)
            expandableLayout.expand();
        else
            expandableLayout.setExpanded(true);

//        arrowView.setImageResource(R.drawable.back);
        setArrowIcon();
    }

    public void setShowAllListener(ShowAllListener showAllListener) {
        this.showAllListener = showAllListener;
    }

    private class ToggleClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (!isEnabled())
                return;

            if (showAllListener != null && (expandableLayout.isExpanded() || !toggleable))
                showAllListener.showAll(ItemsRow.this);
            else if(toggleable)
                expandableLayout.toggle();

            checkItemsState();
            checkBackgroundColor();
        }
    }

    private class InnerExpansionListener implements ExpandableRelativeLayout.OnExpansionListener {
        private ExpandableRelativeLayout.OnExpansionListener innerListener;
        public InnerExpansionListener(ExpandableRelativeLayout.OnExpansionListener innerListener) {
            this.innerListener = innerListener;
        }

        @Override
        public void expanded(View view) {
            innerListener.expanded(ItemsRow.this);
        }

        @Override
        public void onExpansion(View view) {
            innerListener.onExpansion(ItemsRow.this);
        }
    }

    public static interface ShowAllListener{
        void showAll(ItemsRow view);
    }
}
