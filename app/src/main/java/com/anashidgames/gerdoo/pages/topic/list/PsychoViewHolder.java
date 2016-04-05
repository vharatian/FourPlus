package com.anashidgames.gerdoo.pages.topic.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by psycho on 7/9/15.
 */
public class PsychoViewHolder<T> extends RecyclerView.ViewHolder{
    public PsychoViewHolder(View itemView) {
        super(itemView);
    }

    public Context getContext(){
        return itemView.getContext();
    }

    public void bind(T item){
        if (itemView instanceof PsychoSettable)
            ((PsychoSettable)itemView).setData(item);
    }
}
