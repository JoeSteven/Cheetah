package com.joey.cheetah.sample.face

import android.content.Context
import android.graphics.Path
import android.graphics.RectF
import android.view.View
import android.widget.TextView
import com.joey.cheetah.core.ui.face.FaceRecognitionLayout
import com.joey.cheetah.core.ui.face.IFaceRecognitionView
import com.joey.cheetah.core.utils.ResGetter
import com.joey.cheetah.core.utils.UIUtil.dp2px
import com.joey.cheetah.core.utils.UIUtil.sp2px
import com.joey.cheetah.sample.R

/**
 * Description:
 * author:Joey
 * date:2019/1/3
 */
class FaceView:IFaceRecognitionView {
    override fun cacheViewSize(): Int {
        return 3
    }

    override fun useView(): Boolean {
        return true
    }

    override fun viewShape(info: FaceRecognitionLayout.FaceInfo, view: View): Path? {
        return null
    }

    override fun viewPosition(info: FaceRecognitionLayout.FaceInfo, view: View): RectF? {
        val rectF = RectF()
        rectF.left = info.x
        rectF.right = rectF.left + info.width
        rectF.top = info.y - dp2px(25)
        if (rectF.top < 0) {
            rectF.top = info.y + info.height
        }
        rectF.bottom = rectF.top + dp2px(24)
        return rectF
    }

    override fun onViewShow(faceInfo: FaceRecognitionLayout.FaceInfo, view: View) {
        if (view is TextView) view.text = faceInfo.name
    }

    override fun createView(context:Context): View {
        val textView = TextView(context)
        textView.setBackgroundResource(R.color.black)
        textView.textSize = sp2px(12).toFloat()
        textView.setTextColor(ResGetter.color(R.color.white))
        return textView
    }
}