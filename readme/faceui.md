# 多人脸 UI 追踪框架

### 支持功能

- 蒙层
- 多人脸
- 自定义人脸框样式
- 人脸框展示回调

### 使用

- 在布局文件中添加控件，其中background是用来添加蒙层，如果你希望使用蒙层，加上background属性即可，一般使用透明蒙层

```Xml
<com.joey.cheetah.core.ui.face.FaceRecognitionLayout
        android:id="@+id/faceView"
        android:background="#55000000"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

- 自定义样式接口设置

```kotlin
faceView.setIFaceRecognitionView(FaceView())// 设置接口

// 用于自定义样式的接口
class FaceView:IFaceRecognitionView {
  // 设置缓存view大小，根据应用的使用场景，人脸个数来决定
    override fun cacheViewSize(): Int {
        return 3
    }

  // 是否使用自定义样式
    override fun useView(): Boolean {
        return true
    }

  //自定义样式中框的形状，该方法主要用于在蒙层上扣出对应的透明人脸框，详细可以查看注释
    override fun viewShape(info: FaceRecognitionLayout.FaceInfo, view: View): Path? {
        return null
    }

  // 自定义样式框的位置
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

  // 当框展示时会回调该方法，可以做一些操作
    override fun onViewShow(faceInfo: FaceRecognitionLayout.FaceInfo, view: View) {
        if (view is TextView && faceInfo.info is String) view.text = faceInfo.info as String
    }

  // 当缓存view不够时，会调用该方法创建新的人脸框view
    override fun createView(context:Context): View {
        val textView = TextView(context)
        textView.setBackgroundResource(R.color.black)
        textView.textSize = sp2px(12).toFloat()
        textView.setTextColor(ResGetter.color(R.color.white))
        return textView
    }
```

- 开始追踪

```kotlin
faceView.showFaceInfo(faceList)// 传入 FaceInfo 的列表
// 人脸信息
data class FaceInfo(val x: Float,// 人脸框x坐标，一般通过人脸识别sdk获取到
                        val y: Float,// 人脸框y坐标
                        val width: Float,// 宽
                        val height: Float,/ 高
                        val info: Any// 携带自定义的一些信息，如年龄，姓名等
    )
```

- 注意事项

**人脸sdk获取到的人脸坐标可能是基于相机预览图的，在进行追踪时要根据分辨率等信息转成实际UI布局中的坐标才能正常展示**