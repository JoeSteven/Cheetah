package com.joey.cheetah.sample.kt.surprise

import android.graphics.Bitmap
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.joey.cheetah.core.ktextension.loadUrl
import com.joey.cheetah.core.ktextension.glide
import com.joey.cheetah.core.list.AbsItemViewBinder
import com.joey.cheetah.core.list.AbsViewHolder
import com.joey.cheetah.core.utils.UIUtil
import com.joey.cheetah.sample.R
import com.joey.cheetah.sample.kt.GankData
import kotlinx.android.synthetic.main.item_gank_android.*

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
class GankSurpriseViewBinder : AbsItemViewBinder<GankData>(){
    override fun onBind(holder: AbsViewHolder, item: GankData) {
        holder.ivIcon.glide().asBitmap()
                .load(item.url)
                .into(object :SimpleTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val imageWidth = (UIUtil.screenWitdh() - UIUtil.dp2px(20))/2
                        val resizeHeight = (resource.height*1.0/resource.width)*imageWidth
                        holder.ivIcon.loadUrl(item.url, imageWidth, resizeHeight.toInt())
                    }
                })
    }

    override fun layout(): Int {
        return R.layout.item_gank_surprise
    }

}