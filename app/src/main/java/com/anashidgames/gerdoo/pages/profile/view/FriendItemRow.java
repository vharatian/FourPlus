package com.anashidgames.gerdoo.pages.profile.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.PsychoImageLoader;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.call.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.model.DoneResponse;
import com.anashidgames.gerdoo.core.service.model.Friend;
import com.anashidgames.gerdoo.pages.profile.ProfileActivity;

import retrofit2.Call;

/**
 * Created by psycho on 6/12/16.
 */
public class FriendItemRow extends LinearLayout {

    private ImageView imageView;
    private TextView nameView;
    private LinearLayout answerLayout;
    private Friend friend;

    public FriendItemRow(Context context) {
        super(context);
        init(context);
    }

    public FriendItemRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FriendItemRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FriendItemRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_friend_item_row, this);


        imageView = (ImageView) findViewById(R.id.imageView);
        nameView = (TextView) findViewById(R.id.nameView);
        answerLayout = (LinearLayout) findViewById(R.id.requestLayout);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });

        OnClickListener requestListener = new RequestClickListener();
        findViewById(R.id.acceptButton).setOnClickListener(requestListener);
        findViewById(R.id.denyButton).setOnClickListener(requestListener);
    }

    private void openProfile() {
        if (friend == null)
            return;

        getContext().startActivity(ProfileActivity.newIntent(getContext(), friend.getUserId()));
    }

    public void setFriend(Friend friend){
        this.friend = friend;
        PsychoImageLoader.loadImage(getContext(), friend.getImageUrl(), R.drawable.user_image_place_holder, imageView);
        nameView.setText(friend.getName());


        if (friend.isRequesting()){
            answerLayout.setVisibility(VISIBLE);
            answerLayout.setEnabled(true);
        }else {
            answerLayout.setVisibility(INVISIBLE);
        }
    }

    private class RequestClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            answerLayout.setEnabled(false);
            boolean answer = false;
            if (v.getId() == R.id.acceptButton){
                answer = true;
            }else if(v.getId() == R.id.denyButton){
                answer = false;
            }

            Call<DoneResponse> call = GerdooServer.INSTANCE.answerFriendRequest(friend.getUserId(), answer);
            call.enqueue(new AnswerRequestCallback(friend));
        }
    }

    private class AnswerRequestCallback extends CallbackWithErrorDialog<DoneResponse> {
        private Friend friend;

        public AnswerRequestCallback(Friend friend) {
            super(getContext());
            this.friend = friend;
        }

        @Override
        public void handleSuccessful(DoneResponse data) {
            answerLayout.setEnabled(true);
            if (data.isDone()){
                friend.requestAnswered();
                if (FriendItemRow.this.friend == friend){
                    setFriend(friend);
                }
            }
        }
    }
}
