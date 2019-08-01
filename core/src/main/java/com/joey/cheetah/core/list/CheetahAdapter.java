package com.joey.cheetah.core.list;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public class CheetahAdapter extends MultiTypeAdapter{


    DiffCallback mDiffCallback;

    public void enableDiff(DiffCallback callback) {
        mDiffCallback = callback;
        DiffUtil.calculateDiff(mDiffCallback).dispatchUpdatesTo(this);
    }
    
    @Override
    public void setItems(@NonNull List<?> items) {
        if (mDiffCallback != null) {
            mDiffCallback.setOldItems(getItems());
            mDiffCallback.setNewItems(items);
        }
        super.setItems(items);

    }
}
