package com.anashidgames.gerdoo.pages.shop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.payment.IabHelper;
import com.anashidgames.gerdoo.core.payment.IabResult;
import com.anashidgames.gerdoo.core.payment.Purchase;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.call.ConverterCall;
import com.anashidgames.gerdoo.core.service.model.ShopItem;
import com.anashidgames.gerdoo.pages.GerdooActivity;
import com.anashidgames.gerdoo.pages.shop.view.ShopItemViewHolder;
import com.anashidgames.gerdoo.pages.topic.list.PsychoAdapter;
import com.anashidgames.gerdoo.pages.topic.list.PsychoDataProvider;
import com.anashidgames.gerdoo.pages.topic.list.PsychoListResponse;
import com.anashidgames.gerdoo.pages.topic.list.PsychoViewHolder;

import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 5/23/16.
 */
public class ShopActivity extends GerdooActivity {

    static final int REQUEST_CODE = 18723;
    public static final String PUBLIC_KEY = "";
    public static final String SKU = "";


    public static Intent newIntent(Context context){
        return new Intent(context, ShopActivity.class);
    }

    private IabHelper cafeBazaarHelper;
    private ProgressDialog cafeBazaarProgressDialog;
    private PurchaseFinishedListener purchaceFinishListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        findViewById(R.id.backButton).setOnClickListener(new BackListener());
        initRecyclerView();

        connectToCafeBazaar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cafeBazaarHelper != null) {
            cafeBazaarHelper.dispose();
            cafeBazaarHelper = null;
        }
    }

    private void initCafeBazaarProgressDialog() {
        cafeBazaarProgressDialog = new ProgressDialog(this);
        cafeBazaarProgressDialog.setMessage(getString(R.string.connectingToCafeBazaar));
        cafeBazaarProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cafeBazaarHelper.dispose();
                finish();
            }
        });
    }

    private void connectToCafeBazaar() {
        initCafeBazaarProgressDialog();
        cafeBazaarHelper = new IabHelper(this, PUBLIC_KEY);
        cafeBazaarHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (result.isSuccess()) {
                    cafeBazaarProgressDialog.dismiss();
                }else{
                    finish();
                }
            }
        });

        purchaceFinishListener = new PurchaseFinishedListener();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(new ShopAdapter());
    }

    private static class ShopDataProvider extends PsychoDataProvider<ShopItem> {
        public ShopDataProvider(Context context) {
            super(context);
        }

        @Override
        protected Call<PsychoListResponse<ShopItem>> callServer(String nextPage) {
            Call<List<ShopItem>> call = GerdooServer.INSTANCE.getShopItems();
            return new ConverterCall<PsychoListResponse<ShopItem>, List<ShopItem>>(call) {
                @Override
                public PsychoListResponse<ShopItem> convert(List<ShopItem> data) {
                    return new PsychoListResponse<>(data);
                }
            };
        }
    }

    private class BackListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    }

    private class ShopAdapter extends PsychoAdapter<ShopItem> {
        public ShopAdapter() {
            super(ShopActivity.this, new ShopDataProvider(ShopActivity.this));
        }

        @Override
        public PsychoViewHolder<ShopItem> createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
            ShopItemViewHolder holder = ShopItemViewHolder.createHolder(inflater, parent);
            holder.setOnClickListener(new PurchaseListener());
            return holder;
        }
    }

    private class PurchaseFinishedListener implements IabHelper.OnIabPurchaseFinishedListener {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {

        }
    }

    private class PurchaseListener implements ShopItemViewHolder.OnClickListener {
        @Override
        public void onClick(View v, ShopItem item) {
            cafeBazaarHelper.launchPurchaseFlow(ShopActivity.this, item.getCafeBazaarKey(), REQUEST_CODE, purchaceFinishListener);
        }
    }
}
