package com.anashidgames.gerdoo.pages.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.call.ConverterCall;
import com.anashidgames.gerdoo.core.service.model.SearchedTopic;
import com.anashidgames.gerdoo.core.service.model.SearchedUser;
import com.anashidgames.gerdoo.pages.GerdooActivity;
import com.anashidgames.gerdoo.pages.home.view.SearchHeader;
import com.anashidgames.gerdoo.pages.home.view.SearchedTopicViewHolder;
import com.anashidgames.gerdoo.pages.home.view.SearchedUserViewHolder;
import com.anashidgames.gerdoo.pages.topic.list.PsychoAdapter;
import com.anashidgames.gerdoo.pages.topic.list.PsychoDataProvider;
import com.anashidgames.gerdoo.pages.topic.list.PsychoListResponse;
import com.anashidgames.gerdoo.pages.topic.list.PsychoViewHolder;

import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 6/12/16.
 */
public class SearchActivity extends GerdooActivity{
    public static final int SEARCH_USERS = 1;
    public static final int SEARCH_TOPICS = 2;


    private PsychoAdapter adapter;
    private EditText queryEdit;

    public static Intent newIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initToolbar();
        initRecyclerView();
        initSearchHeader();
    }

    private void initSearchHeader() {
        SearchHeader searchHeader = (SearchHeader) findViewById(R.id.searchHeader);
        searchHeader.setPageSelectedListener(new SearchHeader.PageSelectedListener() {
            @Override
            public void pageSelected(int index) {
                if (index == SEARCH_USERS){
                    setAdapter(new SearchUserAdapter());
                }else if (index == SEARCH_TOPICS){
                    setAdapter(new SearchTopicAdapter());
                }
            }
        });
    }

    private void setAdapter(PsychoAdapter adapter) {
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar() {
        queryEdit = (EditText) findViewById(R.id.queryEdit);
        queryEdit.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.refresh();
            }
        });
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setAdapter(new SearchUserAdapter());
    }

    private class SearchTopicProvider extends PsychoDataProvider<SearchedTopic> {
        int receivedCount = 0;
        public SearchTopicProvider() {
            super(SearchActivity.this);
        }

        @Override
        protected Call<PsychoListResponse<SearchedTopic>> callServer(String nextPage) {
            String query = queryEdit.getText().toString();
            Call<List<SearchedTopic>> call = GerdooServer.INSTANCE.searchTopics(query, receivedCount);
            return new ConverterCall<PsychoListResponse<SearchedTopic>, List<SearchedTopic>>(call) {
                @Override
                public PsychoListResponse<SearchedTopic> convert(List<SearchedTopic> data) {
                    receivedCount += data.size();
                    return new PsychoListResponse<>(data, "nextPage");
                }
            };
        }
    }

    private class SearchTopicAdapter extends PsychoAdapter<SearchedTopic> {
        public SearchTopicAdapter() {
            super(SearchActivity.this, new SearchTopicProvider());
        }

        @Override
        public PsychoViewHolder<SearchedTopic> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return SearchedTopicViewHolder.createHolder(inflater, parent);
        }
    }

    private class SearchedUserProvider extends PsychoDataProvider<SearchedUser> {
        int receivedCount = 0;
        public SearchedUserProvider() {
            super(SearchActivity.this);
        }

        @Override
        protected Call<PsychoListResponse<SearchedUser>> callServer(String nextPage) {
            String query = queryEdit.getText().toString();
            Call<List<SearchedUser>> call = GerdooServer.INSTANCE.searchUser(query, receivedCount);
            return new ConverterCall<PsychoListResponse<SearchedUser>, List<SearchedUser>>(call) {
                @Override
                public PsychoListResponse<SearchedUser> convert(List<SearchedUser> data) {
                    receivedCount += data.size();
                    return new PsychoListResponse<>(data, "nextPage");
                }
            };
        }
    }

    private class SearchUserAdapter extends PsychoAdapter<SearchedUser> {
        public SearchUserAdapter() {
            super(SearchActivity.this, new SearchedUserProvider());
        }

        @Override
        public PsychoViewHolder<SearchedUser> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return SearchedUserViewHolder.createHolder(inflater, parent);
        }
    }
}
