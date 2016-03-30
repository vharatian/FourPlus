package com.anashidgames.gerdoo.pages.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.callback.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.model.Category;
import com.anashidgames.gerdoo.pages.FragmentContainerActivity;
import com.anashidgames.gerdoo.pages.home.view.CategoryViewHolder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 3/29/16.
 */
public class CategoryFragment extends Fragment{

    public static final String DATA_URL = "dataUrl";
    public static final String ALL_CATEGORIES_URL = "dataUrl";
    private LinearLayoutManager layoutManager;


    public static Fragment newInstance(@Nullable String dataUrl) {
        if (dataUrl == null)
            dataUrl = ALL_CATEGORIES_URL;


        Bundle bundle = new Bundle();
        bundle.putString(DATA_URL, dataUrl);
        Fragment fragment = new CategoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    private View progressView;
    private RecyclerView recyclerView;

    private GerdooServer server;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_category, container, false);

        server = new GerdooServer();

        initViews(rootView);

        Bundle bundle = getArguments();
        String dataUrl = bundle.getString(DATA_URL, ALL_CATEGORIES_URL);
        loadData(dataUrl);

        return rootView;
    }

    private void hideLoading() {
        progressView.setVisibility(View.GONE);
    }

    private void loadData(String dataUrl) {
        Call<List<Category>> call = server.getCategories(dataUrl);
        call.enqueue(new CategoriesCallBack());
    }

    private void initViews(View rootView) {
        progressView = rootView.findViewById(R.id.progress);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
    }

    private void addCategories(List<Category> data) {
        recyclerView.setAdapter(new CategoryAdapter(data));
    }

    private class CategoriesCallBack extends CallbackWithErrorDialog<List<Category>> {
        public CategoriesCallBack() {
            super(getContext());
        }

        @Override
        protected void postExecution() {
            super.postExecution();
            hideLoading();
        }

        @Override
        protected void handleSuccessful(List<Category> data) {
            addCategories(data);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

        private final List<CategoryWrapper> data;

        public CategoryAdapter(List<Category> data) {
            if (data == null)
                data = new ArrayList<>();

            this.data = CategoryWrapper.convert(data);
        }

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return CategoryViewHolder.newInstance((FragmentContainerActivity) getActivity(), parent);
        }

        @Override
        public void onBindViewHolder(CategoryViewHolder holder, int position) {
            holder.setCategory(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
