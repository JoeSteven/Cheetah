package com.joey.cheetah.core.list;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public abstract class DiffCallback extends DiffUtil.Callback {
    protected List<?> newItems;
    protected List<?> oldItems;

    public void setNewItems(List<?> items) {
        this.newItems = items;
    }


    public void setOldItems(List<?> items) {
        this.oldItems = items;
    }

    @Override
    public int getNewListSize() {
        return newItems != null ? newItems.size() : 0;
    }

    @Override
    public int getOldListSize() {
        return oldItems != null ? oldItems.size() : 0;
    }

}
