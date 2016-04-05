package com.anashidgames.gerdoo.pages.home;

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
import com.anashidgames.gerdoo.pages.home.view.CategoryView;

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
        public void handleSuccessful(List<Category> data) {
            addCategories(data);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

        private List<Category> data;
        private List<CategoryView> views;
        private CategoryView.OnExpansionListener listener;

        public CategoryAdapter(List<Category> data) {
            if (data == null)
                data = new ArrayList<>();

            this.data = data;
            views = new ArrayList<>();
            listener = new ExpansionListener(views);

        }

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CategoryView view = new CategoryView((FragmentContainerActivity) getActivity());
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(new RecyclerView.LayoutParams(layoutParams));
            CategoryViewHolder holder = new CategoryViewHolder(view);
            views.add(view);

            view.setExpansionListener(listener);
            return holder;
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

    private class ExpansionListener implements CategoryView.OnExpansionListener {
        private List<CategoryView> holders;

        public ExpansionListener(List<CategoryView> holders) {
            this.holders = holders;
        }



        @Override
        public void expanded(View view) {
            for (CategoryView v: holders){
                if (v != view){
                    v.close(true);
                }
            }
        }
    }

    private class CategoryViewHolder extends RecyclerView.ViewHolder {
        private CategoryView view;

        public CategoryViewHolder(CategoryView view) {
            super(view);
            this.view = view;
        }

        public void setCategory(Category category){
            view.setCategory(category);
        }
    }
}
