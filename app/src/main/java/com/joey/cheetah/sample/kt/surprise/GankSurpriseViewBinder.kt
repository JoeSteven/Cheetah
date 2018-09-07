package com.joey.cheetah.sample.kt.surprise

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.joey.cheetah.core.ktextension.loadUrl
import com.joey.cheetah.core.ktextension.screenWith
import com.joey.cheetah.core.ktextension.with
import com.joey.cheetah.core.list.AbsItemViewBinder
import com.joey.cheetah.core.list.AbsViewHolder
import com.joey.cheetah.core.utils.ResGetter
import com.joey.cheetah.core.utils.UIUtil
import com.joey.cheetah.sample.R
import com.joey.cheetah.sample.kt.GankData

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
class GankSurpriseViewBinder : AbsItemViewBinder<GankData, GankSurpriseViewBinder.GankSurpriseViewHolder>(){
    inner class GankSurpriseViewHolder(itemView: View):AbsViewHolder<GankData>(itemView) {
        val ivIcon: ImageView = this.findViewById(R.id.ivImage)
    }

    override fun layout(): Int {
        return R.layout.item_gank_surprise
    }

    override fun createViewHolder(itemView: View): GankSurpriseViewHolder {
        return GankSurpriseViewHolder(itemView)
    }

    override fun onBind(holder: GankSurpriseViewHolder, item: GankData) {
        holder.ivIcon.with().asBitmap()
                .load(item.url)
                .into(object :SimpleTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        var imageWidth = (UIUtil.screenWitdh() - UIUtil.dp2px(20))/2
                        var resizeHeight = (resource.height*1.0/resource.width)*imageWidth
                        holder.ivIcon.loadUrl(item.url, imageWidth, resizeHeight.toInt())
                    }
                })
    }

}