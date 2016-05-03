package com.anashidgames.gerdoo.pages.home.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;

/**
 * Created by psycho on 4/15/16.
 */
public class DrawerItemView extends LinearLayout {

    private TextView titleView;
    private ImageView iconView;
    private DrawerItem item;

    public DrawerItemView(Context context) {
        super(context);
        init(context);
    }

    public DrawerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawerItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DrawerItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_drawer_item, this);

        titleView = (TextView) findViewById(R.id.titleView);
        iconView = (ImageView) findViewById(R.id.iconView);

        setOnTouchListener(new ColorSetter());
        setOnClickListener(new ActivityRunner());
    }

    public void setData(DrawerItem item){
        this.item = item;
        titleView.setText(item.getTitleResource());
        iconView.setImageResource(item.getIconResource());
        setColor(false);
    }

    private void setColor(boolean active){
        if (active){
            setBackgroundResource(R.color.colorPrimary);
            iconView.setColorFilter(getResources().getColor(R.color.colorAccent));
        }else{
            setBackgroundResource(R.color.colorPrimarySide);
            iconView.setColorFilter(getResources().getColor(R.color.drawerGray));
        }
    }

    private class ColorSetter implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                setColor(true);
            }else if(event.getAction() == MotionEvent.ACTION_UP){
                setColor(false);
            }

            return false;
        }
    }

    private class ActivityRunner implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (item.getIntent() != null) {
                getContext().startActivity(item.getIntent());
            }
        }
    }


    public static class DrawerItem {
        private int titleResource;
        private int iconResource;
        private Intent intent;

        public DrawerItem(int titleResource, int iconResource, Intent intent) {
            this.titleResource = titleResource;
            this.iconResource = iconResource;
            this.intent = intent;
        }

        public int getTitleResource() {
            return titleResource;
        }

        public int getIconResource() {
            return iconResource;
        }

        public Intent getIntent() {
            return intent;
        }
    }
}
