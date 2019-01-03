package com.joey.cheetah.core.ui.face

/**
 * Description:
 * author:Joey
 * date:2018/9/27
 */

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.joey.cheetah.core.ktextension.gone
import com.joey.cheetah.core.ktextension.logD
import com.joey.cheetah.core.ktextension.logE
import com.joey.cheetah.core.ktextension.visible
import com.joey.cheetah.core.utils.UIUtil


/**
 * 人脸追踪layout，支持多人脸追踪，支持追踪框自定义，支持蒙层人脸框部分透明
 * 开启自定义人脸时，内部总共维护了三条列表
 * 1.当前展示中的view列表，这些view是一定可见的
 * 2.缓存自定义view，这个列表会缓存一定数量的view，这些view都被attach到窗口上了，从该列表获取view可以减少耗时
 * 3.超出缓存的自定义view，这个列表会存一些临时的view， 当下一次展示的时候会清空该列表
 * 追踪时的展示逻辑为：
 * 1.如果当前展示中的view个数大于等于当前人脸个数，直接复用该列表中的view
 * 2.否则从缓存中取获取view来复用
 * 3.缓存中不够时，调用 createView 创建新的view，并且存入缓存中
 * 4.达到缓存上限后，创建的view存入超出缓存列表
 * 5.最终展示的view都会进入到展示中列表，即该列表个数与人脸个数刚好相等
 * 为了保证缓存的高效性，在设置缓存的时候应该根据实际场景进行设计
 * 例如：大部分时候人脸个数为5到6人，峰值人数可能在12人，那么缓存数量设计为8到10会比较合理
 * 即大部分场景下的人脸个数为缓存的三分之二，缓存容量过大会导致内存浪费
 * 峰值超出部分不大于缓存的二分之一，缓存容量过小会导致，频繁创建view，运行效率下降
 *
 * 超出缓存列表的大小是动态的，随着人脸个数的多少发生变化，承担了动态扩容缓存的作用
 * Created by Joey on 2019/1/3.
 */
class FaceRecognitionLayout : FrameLayout {
    private val tag = "FaceTrackView"
    @Volatile
    private var inValidate = false
    private val showingView = hashMapOf<FaceInfo, View>()
    private val cacheViewList = mutableListOf<View>()
    private val outOfCacheViewList = mutableListOf<View>()
    private var cacheExpanded = false
    private var cacheExpandCount = 0

    private var background: FaceRecognitionDrawable? = null
    private var faceView: IFaceRecognitionView = object : IFaceRecognitionView {
        override fun cacheViewSize(): Int {
            return 10
        }

        override fun viewShape(info: FaceInfo, view: View): Path? {
            return null
        }

        override fun viewPosition(info: FaceInfo, view: View): RectF? {
            return null
        }

        override fun useView(): Boolean {
            return false
        }

        override fun createView(context: Context): View {
            return TextView(context)
        }

        override fun onViewShow(faceInfo: FaceInfo, view: View) {}

    }

    constructor(context: Context) : super(context) {
        initView(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs, defStyleAttr)
    }

    @SuppressLint("NewApi")
    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (getBackground() == null) return
        background = FaceRecognitionDrawable(getBackground())
        setBackground(background)
    }

    /**
     * 自定义追踪UI
     */
    fun setIFaceRecognitionView(view: IFaceRecognitionView) {
        faceView = view
    }

    private fun cacheSize(): Int {
        return if (cacheExpanded) faceView.cacheViewSize() * 2 else faceView.cacheViewSize()
    }

    /**
     * 人脸追踪，将人脸的列表信息传入，当没有人脸时请传入一个size为空的列表
     */
    @Synchronized
    fun showFaceInfo(faces: List<FaceInfo>) {
        if (inValidate) return
        inValidate = true
        post {
            logE(tag, "start tracking")
            if (faceView.useView()) {
//                checkExpandCacheSize()
                logD(tag, "use custom view")
                logD(tag, "face count is ${faces.size}")
                logD(tag, "before showing view count is ${showingView.size}")
                logD(tag, "before cache view count is ${cacheViewList.size}")
                logD(tag, "before out of cache view count is ${outOfCacheViewList.size}")
                // 如果展示中的view数量够，从展示中取
                if (showingView.size >= faces.size) {
                    val views = ArrayList<View>(showingView.values)
                    invisibleView(faces.size)
                    logD(tag, "reuse showing view")
                    for (i in faces.indices) {
                        showingView[faces[i]] = views[i]
                    }
                } else {
                    // 展示中不够，从缓存中取，清空展示中
                    invisibleView(faces.size)
                    logD(tag, "reuse from cache view")
                    for (i in faces.indices) {
                        // 缓存中够，直接取，不够创建新的view
                        if (i < cacheViewList.size) {
                            showingView[faces[i]] = cacheViewList[i]
                        } else {
                            // 创建新的view后如果没有达到缓存上限，存入到缓存中
                            val view = faceView.createView(context)
                            showingView[faces[i]] = view
                            if (i < cacheSize()) {
                                cacheViewList.add(view)
                            } else {
                                outOfCacheViewList.add(view)
                            }
                        }
                    }
                }
                logD(tag, "after showing view count is ${showingView.size}")
                logD(tag, "after cache view count is ${cacheViewList.size}")
                logD(tag, "after out of cache view count is ${outOfCacheViewList.size}")
            }

            val pathList = mutableListOf<Path>()
            for (face in faces) {
                // 扣出透明的部分，自定义形状优先
                val view = showingView[face]
                var path = faceView.viewShape(face, view ?: View(context))
                if (path == null) {
                    path = Path()
                    val oval = RectF(face.x, face.y, face.x + face.width, face.y + face.height)
                    path.addRoundRect(oval, UIUtil.dp2px(5).toFloat(), UIUtil.dp2px(5).toFloat(), Path.Direction.CCW)
                }
                pathList.add(path)

            }
            // 设置自定义view的属性
            background?.setSrcPath(pathList)
            background?.invalidateSelf()
            if (faceView.useView()) {
                for (face in faces) {
                    val view = showingView[face]
                    view?.let {
                        val position = faceView.viewPosition(face, it)
                        if (position == null) {
                            it.layoutParams = LayoutParams(face.width.toInt(), face.height.toInt())
                            it.x = face.x
                            it.y = face.y
                        } else {
                            it.layoutParams = LayoutParams((position.right - position.left).toInt(), (position.bottom - position.top).toInt())
                            it.x = position.left
                            it.y = position.top
                        }
                        if (it.parent == null) {
                            addView(it)
                        }
                        it.visible()
                        faceView.onViewShow(face, it)
                    }
                }
            }
            inValidate = false
        }
    }

    private fun invisibleView(size: Int) {
        logD(tag, "clear showing view")
        if (size >= showingView.size){
            showingView.clear()
            return
        }
        // 先从超出里面开始移除
        var left = showingView.size - size
        val showingViews = ArrayList<View>(showingView.values)
        val deleteList = mutableListOf<View>()
        for (i in 0 until left) {
            if (i >= outOfCacheViewList.size) {
                left -= i
                break
            }
            val view = outOfCacheViewList[i]
            if (view.parent != null) {
                removeView(view)
                showingViews.remove(view)
                deleteList.add(view)
            }
        }
        outOfCacheViewList.removeAll(deleteList)
        //然后从showingView 里面置为不可见
        for (view in showingViews) {
            view.gone()
        }

        showingView.clear()

    }

    // 超出缓存的列表在修改清除算法后其实已经承担了缓存扩容任务
    private fun checkExpandCacheSize() {
        // 动态扩容
        if (cacheExpanded) {
            // 扩容状态下检查峰值是否过去
            if (showingView.size <= faceView.cacheViewSize()) {
                cacheExpandCount += 1
            } else {
                cacheExpandCount = 0
            }
            if (cacheExpandCount > 4) {
                //恢复缓存大小, 清除掉缓存中多余的view
                val deleteList = mutableListOf<View>()
                for (i in faceView.cacheViewSize() until cacheViewList.size) {
                    if (cacheViewList[i].parent != null) removeView(cacheViewList[i])
                    deleteList.add(cacheViewList[i])
                }
                cacheViewList.removeAll(deleteList)
                cacheExpanded = false
                logE(tag, "reset cache size, current is ${cacheViewList.size}, max size is ${cacheSize()}")
            }
        } else {
            if (showingView.size > cacheViewList.size) {
                cacheExpandCount += 1
            } else {
                cacheExpandCount = 0
            }

            if (cacheExpandCount > 4) {
                //扩容
                cacheExpanded = true
                // 将产出缓存的view移到缓存列表，直到填满view
                logE(tag, "expand cache size, current is ${cacheViewList.size}, max size is ${cacheSize()}")
            }
        }
    }

    data class FaceInfo(val x: Float,
                        val y: Float,
                        val width: Float,
                        val height: Float,
                        val name: String = "", // 姓名
                        val isVip: Boolean = false,// 是否vip
                        val gender: String = "",// 性别
                        val motion: String = ""// 情绪
    )
}