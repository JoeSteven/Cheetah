package com.joey.cheetah.core.ui.face

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.Drawable

/**
 * Description:
 * author:Joey
 * date:2018/9/27
 */
class FaceRecognitionDrawable(private val innerDrawable: Drawable) : Drawable() {
    private val srcPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var srcPath = listOf<Path>()

    /**
     * 设置内部透明的部分
     *
     * @param srcPath
     */
    fun setSrcPath(srcPath: List<Path>) {
        this.srcPath = srcPath
        srcPaint.color = -0x1
    }

    override fun draw(canvas: Canvas) {
        innerDrawable.bounds = bounds
        if (srcPath.isEmpty()) {
            innerDrawable.draw(canvas)
        } else {
            val saveCount = canvas.saveLayer(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), srcPaint, Canvas.ALL_SAVE_FLAG)

            //dst 绘制目标图
            innerDrawable.draw(canvas)

            //设置混合模式
            srcPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            //src 绘制源图
            for (path in srcPath) {
                canvas.drawPath(path, srcPaint)
            }
            //清除混合模式
            srcPaint.xfermode = null
            //还原画布
            canvas.restoreToCount(saveCount)
        }
    }

    override fun setAlpha(alpha: Int) {
        innerDrawable.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        innerDrawable.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return innerDrawable.opacity
    }
}