package com.joey.cheetah.core.ui.face

import android.content.Context
import android.graphics.Path
import android.graphics.RectF
import android.view.View

/**
 * Description: 对人脸追踪UI进行自定义
 * author:Joey
 * date:2019/1/3
 */
interface IFaceRecognitionView {
    /**
     * 创建自定义view
     */
    fun createView(context: Context):View

    /**
     * 缓存自定义人脸框的个数，请根据实际使用场景来定义
     */
    fun cacheViewSize():Int

    /**
     * 是否使用自定义人脸框
     */
    fun useView():Boolean

    /**
     * 人脸追踪框形状及位置，主要用于蒙层透明
     * 返回null 则使用默认的形状，即圆角矩形
     */
    fun viewShape(info: FaceRecognitionLayout.FaceInfo, view:View): Path?

    /**
     * 自定义人脸框的位置，一般与viewShape中定义的位置大小匹配
     * RectF: 分别返回人脸框的四个顶点坐标，内部会根据坐标计算view的大小
     * 返回空则位置大小与viewShape 保持一致
     */
    fun viewPosition(info:FaceRecognitionLayout.FaceInfo, view: View):RectF?

    /**
     * 人脸框展示出来的时候会回调该方法
     * 可以在此处对你的view进行一些操作
     * 例如显示姓名，性别之类的
     */
    fun onViewShow(faceInfo: FaceRecognitionLayout.FaceInfo, view: View)

}