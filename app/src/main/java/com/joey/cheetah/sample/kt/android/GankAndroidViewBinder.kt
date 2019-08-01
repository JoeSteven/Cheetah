package com.joey.cheetah.sample.kt.android

import android.view.View
import com.joey.cheetah.core.ktextension.loadUrl
import com.joey.cheetah.core.list.AbsItemViewBinder
import com.joey.cheetah.core.list.AbsViewHolder
import com.joey.cheetah.sample.R
import com.joey.cheetah.sample.kt.GankData
import kotlinx.android.synthetic.main.item_gank_android.*

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
class GankAndroidViewBinder : AbsItemViewBinder<GankData>(){
    override fun onBind(holder: AbsViewHolder, item: GankData) {
        holder.tvTitle.text = item.desc
        if (item.images != null && item.images.isNotEmpty()) {
            holder.ivIcon.loadUrl(item.images[0])
            holder.ivIcon.visibility = View.VISIBLE
        } else {
            holder.ivIcon.visibility = View.GONE
        }
        holder.tvAuthor.text = item.who
        holder.tvTime.text = item.publishedAt
    }

    override fun layout(): Int {
        return R.layout.item_gank_android
    }



}