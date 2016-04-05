package com.anashidgames.gerdoo.pages.topic.list;

/**
 * Created by psycho on 3/31/16.
 */
public interface DataCallback<T> {
    void onData(T data, boolean finished);
    void onError();
}
