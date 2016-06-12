package com.anashidgames.gerdoo.pages.home.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.pages.LeaderBoardView;
import com.anashidgames.gerdoo.pages.home.SearchActivity;
import com.anashidgames.gerdoo.pages.topic.view.ToggleButton;

/**
 * Created by psycho on 4/3/16.
 */
public class SearchHeader extends LinearLayout {

    private PageSelectedListener pageSelectedListener;
    private SearchToggleButton topicButton;
    private SearchToggleButton userButton;
    private View userArrow;
    private View topicArrow;
    private boolean disabled;

    public SearchHeader(Context context) {
        super(context);
        init(context);
    }

    public SearchHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_search_header, this);

        topicButton = (SearchToggleButton) findViewById(R.id.userButton);
        userButton = (SearchToggleButton) findViewById(R.id.topicButton);

        userArrow = findViewById(R.id.userArrow);
        topicArrow = findViewById(R.id.topicArrow);

        topicButton.setData(R.string.users);
        userButton.setData(R.string.topics);

        ToggleListener listener = new ToggleListener(topicButton);
        topicButton.setOnClickListener(listener);
        userButton.setOnClickListener(listener);

        reset();
    }

    private void reset() {
        topicButton.setState(true);
        userArrow.setVisibility(VISIBLE);
        userButton.setState(false);
        topicArrow.setVisibility(INVISIBLE);
    }

    private void changeUserButtonState(boolean state){
        topicButton.setState(state);
        if(state){
            userArrow.setVisibility(VISIBLE);
        }else{
            userArrow.setVisibility(INVISIBLE);
        }
    }

    private void changeTopicButtonState(boolean state){
        userButton.setState(state);
        if(state){
            topicArrow.setVisibility(VISIBLE);
        }else{
            topicArrow.setVisibility(INVISIBLE);
        }
    }

    private void select(boolean users){
        if (disabled)
            return;

        int newPage;
        if (users){
            changeUserButtonState(true);
            changeTopicButtonState(false);

            newPage = SearchActivity.SEARCH_USERS;
        }else{
            changeTopicButtonState(true);
            changeUserButtonState(false);

            newPage = SearchActivity.SEARCH_TOPICS;
        }

        if (pageSelectedListener != null) {
            pageSelectedListener.pageSelected(newPage);
        }
    }

    public void disableAll(){
        changeUserButtonState(false);
        changeTopicButtonState(false);
    }

    public void setPageSelectedListener(PageSelectedListener pageSelectedListener) {
        this.pageSelectedListener = pageSelectedListener;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    private class ToggleListener implements OnClickListener {
        private SearchToggleButton first;

        public ToggleListener(SearchToggleButton first) {
            this.first = first;
        }

        @Override
        public void onClick(View v) {
            select(v == first);
        }
    }

    public interface PageSelectedListener{
        void pageSelected(int index);
    }
}
