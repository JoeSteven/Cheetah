package com.joey.cheetah.core.list;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public abstract class AbsItemViewBinder<T, VH extends AbsViewHolder<T>> extends ItemViewBinder<T, VH> {

    protected OnItemClickListener<T> clickListener;
    protected OnItemLongClickListener<T> longClickListener;

    public AbsItemViewBinder<T, VH> setOnClickListener(OnItemClickListener<T> clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public AbsItemViewBinder<T, VH> setOnLongClickListener(OnItemLongClickListener<T> longClickListener) {
        this.longClickListener = longClickListener;
        return this;
    }

    public interface OnItemClickListener<T>{
        void onItemClick(int position, T data);
    }

    public interface OnItemLongClickListener<T>{
        boolean onItemLongClick(int position, T data);
    }

    protected abstract int layout();

    @NonNull
    @Override
    protected VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(layout(), parent, false);
        VH holder = createViewHolder(itemView);
        if (clickListener != null) {
            holder.itemView.setOnClickListener(v -> clickListener.onItemClick(holder.getAdapterPosition(), holder.data));
        }
        if (longClickListener != null) {
            itemView.setOnLongClickListener(v -> longClickListener.onItemLongClick(holder.getAdapterPosition(), holder.data));
        }
        return holder;
    }

    protected abstract VH createViewHolder(View itemView);

    @Override
    protected void onBindViewHolder(@NonNull VH holder, @NonNull T item) {
       holder.bind(item);
       onBind(holder, item);
    }

    protected abstract void onBind(VH holder, T item);

}
