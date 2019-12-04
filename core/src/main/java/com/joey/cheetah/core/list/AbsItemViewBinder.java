package com.joey.cheetah.core.list;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
public abstract class AbsItemViewBinder<T> extends ItemViewBinder<T, AbsViewHolder> {

    protected OnItemClickListener<T> clickListener;
    protected OnItemLongClickListener<T> longClickListener;

    public AbsItemViewBinder<T> setOnClickListener(OnItemClickListener<T> clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public AbsItemViewBinder<T> setOnLongClickListener(OnItemLongClickListener<T> longClickListener) {
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
    protected AbsViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(layout(), parent, false);
        return createViewHolder(itemView);
    }

    protected AbsViewHolder createViewHolder(@NonNull View itemView){
        return new AbsViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull AbsViewHolder holder, @NonNull T item) {
        if (clickListener != null) {
            holder.itemView.setOnClickListener(v -> clickListener.onItemClick(holder.getAdapterPosition(), item));
        } else  {
            holder.itemView.setOnClickListener(this::onItemClick);
        }
        if (longClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> longClickListener.onItemLongClick(holder.getAdapterPosition(), item));
        } else  {
            holder.itemView.setOnLongClickListener(this::onLongItemClick);
        }
       onBind(holder, item);
    }

    private boolean onLongItemClick(View view) {
        return false;
    }


    protected void onItemClick(View view) {

    }

    protected abstract void onBind(@NonNull AbsViewHolder holder, @NonNull T item);

}
