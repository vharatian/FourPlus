package com.anashidgames.gerdoo.pages.home.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.PsychoImageLoader;
import com.anashidgames.gerdoo.core.service.model.SearchedUser;
import com.anashidgames.gerdoo.pages.profile.ProfileActivity;
import com.anashidgames.gerdoo.pages.topic.list.PsychoViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by psycho on 6/12/16.
 */
public class SearchedUserViewHolder extends PsychoViewHolder<SearchedUser>{


    public static PsychoViewHolder<SearchedUser> createHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.view_search_user, parent, false);
        return new SearchedUserViewHolder(view);
    }

    private TextView nameView;
    private TextView scoreView;
    private CircleImageView imageView;

    private SearchedUser item;


    public SearchedUserViewHolder(View itemView) {
        super(itemView);
        init(itemView);
    }

    private void init(View view) {
        nameView = (TextView) view.findViewById(R.id.nameView);
        scoreView = (TextView) view.findViewById(R.id.scoreView);
        imageView = (CircleImageView) view.findViewById(R.id.imageView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserProfile();
            }
        });
    }


    @Override
    public void bind(SearchedUser item) {
        super.bind(item);
        this.item = item;

        nameView.setText(item.getName());
        scoreView.setText(getContext().getResources().getString(R.string.score).replace("score", "" + item.getScore()));
        PsychoImageLoader.loadImage(getContext(), item.getImageUrl(), R.drawable.home_topic_place_holder, imageView);
    }

    private void showUserProfile() {
        if (item == null)
            return;

        getContext().startActivity(ProfileActivity.newIntent(getContext(), item.getUserId()));
    }
}
