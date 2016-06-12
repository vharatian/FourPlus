package com.anashidgames.gerdoo.pages.shop.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.PsychoImageLoader;
import com.anashidgames.gerdoo.core.service.model.ShopCategoryData;
import com.anashidgames.gerdoo.pages.topic.list.PsychoViewHolder;

/**
 * Created by psycho on 5/23/16.
 */
public class ShopItemViewHolder extends PsychoViewHolder<ShopCategoryData>{




    public static ShopItemViewHolder createHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.view_shop_item, parent, false);
        return new ShopItemViewHolder(view);
    }


    private ImageView imageView;
    private TextView descriptionView;
    private TextView priceView;

    private ShopCategoryData item;
    private OnClickListener clickListener;

    public ShopItemViewHolder(View itemView) {
        super(itemView);
        init(itemView);
    }

    private void init(View view) {
        imageView = (ImageView) view.findViewById(R.id.imageView);
        descriptionView = (TextView) view.findViewById(R.id.descriptionView);
        priceView = (TextView) view.findViewById(R.id.priceView);

        view.setOnClickListener(new InnerListener());
    }

    @Override
    public void bind(ShopCategoryData item) {
        super.bind(item);
        this.item = item;

//        Context context = itemView.getContext();
//        PsychoImageLoader.loadImage(context, item.getImageUrl(), R.drawable.home_topic_place_holder, imageView);
//        descriptionView.setText(item.getDescription());
//        priceView.setText(context.getString(R.string.price).replace("price", "" + item.getPrice()));
    }

    public void setOnClickListener(OnClickListener clickListener){
        this.clickListener = clickListener;
    }


    public interface OnClickListener {
        void onClick(View v, ShopCategoryData item);
    }

    private class InnerListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (clickListener != null){
                clickListener.onClick(v, item);
            }
        }
    }
}
