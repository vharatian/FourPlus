package com.anashidgames.gerdoo.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;

/**
 * Created by psycho on 5/2/16.
 */
public class TextActivity extends GerdooActivity {

    public static final String TEXT_RESOURCE = "textResource";
    public static final String TITLE_RESOURCE = "titleResource";

    public static Intent newIntent(Context context, int titleResource, int textResource){
        Intent intent = new Intent(context, TextActivity.class);
        intent.putExtra(TEXT_RESOURCE, textResource);
        intent.putExtra(TITLE_RESOURCE, titleResource);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        int textResource = getIntent().getIntExtra(TEXT_RESOURCE, 0);
        int titleResource = getIntent().getIntExtra(TITLE_RESOURCE, 0);
        ((TextView) findViewById(R.id.textView)).setText(textResource);
        ((TextView) findViewById(R.id.titleView)).setText(titleResource);
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
