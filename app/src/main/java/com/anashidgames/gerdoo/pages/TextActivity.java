package com.anashidgames.gerdoo.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.view.basic.TextView;

/**
 * Created by psycho on 5/2/16.
 */
public class TextActivity extends GerdooActivity {

    public static final String TEXT_RESOURCE = "textResource";

    public static Intent newIntent(Context context, int textResource){
        Intent intent = new Intent(context, TextActivity.class);
        intent.putExtra(TEXT_RESOURCE, textResource);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        int textResource = getIntent().getIntExtra(TEXT_RESOURCE, 0);
        ((TextView) findViewById(R.id.textView)).setText(textResource);
    }
}
