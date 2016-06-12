package com.anashidgames.gerdoo.pages.home.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.model.Message;
import com.anashidgames.gerdoo.pages.topic.list.PsychoViewHolder;

/**
 * Created by psycho on 6/5/16.
 */
public class MessageViewHolder extends PsychoViewHolder<Message>{

    public static PsychoViewHolder<Message> createHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.view_message, parent, false);
        return new MessageViewHolder(view);
    }

    private TextView titleView;
    private TextView messageView;


    public MessageViewHolder(View itemView) {
        super(itemView);

        init(itemView);
    }

    private void init(View view) {
        titleView = (TextView) view.findViewById(R.id.titleView);
        messageView = (TextView) view.findViewById(R.id.messageView);
    }

    @Override
    public void bind(Message item) {
        super.bind(item);

        titleView.setText(item.getTitle());
        messageView.setText(item.getMessage());
    }
}
