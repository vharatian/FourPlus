package com.anashidgames.gerdoo.core.service.model;

/**
 * Created by psycho on 5/23/16.
 */
public class ShopItem {
    private String itemId;
    private String imageUrl;
    private String description;
    private int price;
    private String cafeBazaarKey;

    public ShopItem(String itemId, String imageUrl, String description, int price) {
        this.itemId = itemId;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
    }


    public String getItemId() {
        return itemId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getCafeBazaarKey() {
        return cafeBazaarKey;
    }
}
