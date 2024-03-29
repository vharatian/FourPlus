package com.anashidgames.gerdoo.view.basic;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;

import com.anashidgames.gerdoo.utils.PsychoUtils;

/**
 * Created by psycho on 3/17/16.
 */
public class BoldTextView extends android.widget.TextView {
    public BoldTextView(Context context) {
        super(context);
        init();
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BoldTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/IRANSansMobile_Medium.ttf");
        setTypeface(tf);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (text != null)
            text = PsychoUtils.toPersianNumber(text.toString());
        super.setText(text, type);
    }
}
