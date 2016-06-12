package com.anashidgames.gerdoo.pages.shop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.payment.IabHelper;
import com.anashidgames.gerdoo.core.payment.IabResult;
import com.anashidgames.gerdoo.core.payment.Purchase;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.call.CallbackWithErrorDialog;
import com.anashidgames.gerdoo.core.service.model.DoneResponse;
import com.anashidgames.gerdoo.core.service.model.ShopCategoryData;
import com.anashidgames.gerdoo.core.service.model.ShopItem;
import com.anashidgames.gerdoo.pages.GerdooActivity;
import com.anashidgames.gerdoo.pages.shop.view.ShopItemViewHolder;
import com.anashidgames.gerdoo.pages.shop.view.ShopCategoryRow;

import java.util.List;

import retrofit2.Call;

/**
 * Created by psycho on 5/23/16.
 */
public class ShopActivity extends GerdooActivity {

    static final int REQUEST_CODE = 18723;
    public static final String PUBLIC_KEY = "";
    public static final String SKU = "";



    public static Intent newIntent(Context context) {
        return new Intent(context, ShopActivity.class);
    }

    private IabHelper cafeBazaarHelper;
    private ProgressDialog cafeBazaarProgressDialog;
    private PurchaseFinishedListener purchaseFinishListener;
    private View progressView;
    private LinearLayout categoriesLayout;
    private ProgressDialog progressDialog;
    private Call call;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        initProgressDialog();
        categoriesLayout = (LinearLayout) findViewById(R.id.categoriesLayout);
        progressView = findViewById(R.id.progress);
        findViewById(R.id.backButton).setOnClickListener(new BackListener());

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

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.pleaseWait));
        progressDialog.setTitle(getString(R.string.changeImage));
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancelRequesting();
            }
        });
    }

    private void cancelRequesting() {
        try {
            if (call != null){
                call.cancel();
            }

            if (progressDialog != null){
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                    loadData();
                } else {
                    finish();
                }
            }
        });

        purchaseFinishListener = new PurchaseFinishedListener();
    }

    private void loadData() {
        call = GerdooServer.INSTANCE.getShopCategories();
        call.enqueue(new ShopCategoriesCallback());
    }

    private void addCategories(List<ShopCategoryData> rows) {
        progressView.setVisibility(View.GONE);
        for (ShopCategoryData data : rows){
            ShopCategoryRow row = new ShopCategoryRow(this);
            row.setData(data);
            row.setOnShopItemSelectedListener(new PurchaseListener());
            row.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            categoriesLayout.addView(row);
        }
    }

    private void sendPurchaseInfoToServer(Purchase info) {
        progressDialog.show();
        call = GerdooServer.INSTANCE.sendPurchaseInfo(info);
        call.enqueue(new SendPurchaseInfoCallBack());
    }

    private class BackListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    }

    private class PurchaseFinishedListener implements IabHelper.OnIabPurchaseFinishedListener {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {
            if (result.isSuccess()) {
                sendPurchaseInfoToServer(info);
            }else {
                Toast.makeText(ShopActivity.this, R.string.failiurShoping, Toast.LENGTH_SHORT);
            }
        }
    }


    private class PurchaseListener implements ShopCategoryRow.OnShopItemSelectedListener {
        @Override
        public void onSelected(ShopItem item) {
            cafeBazaarHelper.launchPurchaseFlow(ShopActivity.this, item.getCafeBazaarKey(), REQUEST_CODE, purchaseFinishListener);
        }
    }

    private class ShopCategoriesCallback extends CallbackWithErrorDialog<List<ShopCategoryData>> {
        public ShopCategoriesCallback() {
            super(ShopActivity.this);
        }

        @Override
        public void handleSuccessful(List<ShopCategoryData> data) {
            addCategories(data);
        }
    }

    private class SendPurchaseInfoCallBack extends CallbackWithErrorDialog<DoneResponse> {
        public SendPurchaseInfoCallBack() {
            super(ShopActivity.this);
        }

        @Override
        protected void postExecution() {
            super.postExecution();
            progressDialog.dismiss();
        }

        @Override
        public void handleSuccessful(DoneResponse data) {
            if (data.isDone()){
                Toast.makeText(ShopActivity.this, R.string.successfullShop, Toast.LENGTH_SHORT);
            }
        }
    }
}