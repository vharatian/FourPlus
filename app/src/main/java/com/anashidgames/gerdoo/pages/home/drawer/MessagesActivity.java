package com.anashidgames.gerdoo.pages.home.drawer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.call.ConverterCall;
import com.anashidgames.gerdoo.core.service.model.Message;
import com.anashidgames.gerdoo.pages.GerdooActivity;
import com.anashidgames.gerdoo.pages.home.view.MessageViewHolder;
import com.anashidgames.gerdoo.pages.topic.list.PsychoAdapter;
import com.anashidgames.gerdoo.pages.topic.list.PsychoDataProvider;
import com.anashidgames.gerdoo.pages.topic.list.PsychoListResponse;
import com.anashidgames.gerdoo.pages.topic.list.PsychoViewHolder;

import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 6/5/16.
 */
public class MessagesActivity extends GerdooActivity {
    public static Intent newIntent(Context context){
        return new Intent(context, MessagesActivity.class);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiyt_messages);

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MessageAdapter());
    }

    private class MessageProvider extends PsychoDataProvider<Message> {
        public MessageProvider() {
            super(MessagesActivity.this);
        }

        @Override
        protected Call<PsychoListResponse<Message>> callServer(String nextPage) {
            Call<List<Message>> call = GerdooServer.INSTANCE.getMessages();
            return new ConverterCall<PsychoListResponse<Message>, List<Message>>(call) {
                @Override
                public PsychoListResponse<Message> convert(List<Message> data) {
                    return new PsychoListResponse<Message>(data);
                }
            };
        }
    }

    private class MessageAdapter extends PsychoAdapter<Message> {
        public MessageAdapter() {
            super(MessagesActivity.this, new MessageProvider());
        }

        @Override
        public PsychoViewHolder<Message> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return MessageViewHolder.createHolder(inflater, parent);
        }
    }
}
