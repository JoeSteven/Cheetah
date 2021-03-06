package com.joey.cheetah.core.list;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public class AbsViewHolder<T> extends RecyclerView.ViewHolder {
    public T data;

    public AbsViewHolder(View itemView) {
        super(itemView);
    }

    @SuppressWarnings("unchecked")
    public <V> V findViewById(@IdRes int id) {
        return ((V) itemView.findViewById(id));
    }

    protected void bind(T data) {
        this.data = data;
    }
}
