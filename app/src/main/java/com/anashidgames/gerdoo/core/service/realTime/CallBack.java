package com.anashidgames.gerdoo.core.service.realTime;

import android.support.annotation.Nullable;

/**
 * Created by psycho on 5/9/16.
 */
public interface CallBack<T> {
    void onData(T data);
    void onFailure(String message, @Nullable Throwable t);
}
