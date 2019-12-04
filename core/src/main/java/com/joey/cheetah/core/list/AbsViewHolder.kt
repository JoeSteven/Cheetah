package com.joey.cheetah.core.list

import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import kotlinx.android.extensions.LayoutContainer

/**
 * Description:
 * author:Joey
 * date:2018/8/14
 */
class AbsViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun <V> findViewById(@IdRes id: Int): V {
        return itemView.findViewById<View>(id) as V
    }
}
