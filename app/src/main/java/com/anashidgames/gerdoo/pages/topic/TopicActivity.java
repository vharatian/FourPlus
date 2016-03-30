package com.anashidgames.gerdoo.pages.topic;

import android.content.Context;
import android.content.Intent;

import com.anashidgames.gerdoo.pages.GerdooActivity;

/**
 * Created by psycho on 3/29/16.
 */
public class TopicActivity extends GerdooActivity {

    public static final String DATA_URL = "dataUrl";

    public static Intent newIntent(Context context, String dataUrl) {
        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra(DATA_URL, dataUrl);
        return intent;
    }
}
