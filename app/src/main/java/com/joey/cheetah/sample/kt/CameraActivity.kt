package com.joey.cheetah.sample.kt

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.WindowManager
import android.widget.FrameLayout
import com.joey.cheetah.mvp.AbsActivity
import com.joey.cheetah.sample.R
import kotlinx.android.synthetic.main.activity_camera.*
import android.util.DisplayMetrics
import android.util.Log
import com.joey.cheetah.core.camera.*
import java.io.File
import java.io.FileOutputStream


class CameraActivity : AbsActivity() {

    private lateinit var mCameraManager:CameraManager
    private lateinit var mCameraPreview:Camera1Preview
    private var mPreviewW:Int = 0
    private var mPreviewH:Int = 0

    override fun initView() {
        mCameraPreview = Camera1Preview(this)

        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val screenWidth = dm.widthPixels
        val screenHight = dm.heightPixels

        mPreviewW = screenWidth
        mPreviewH = (mPreviewW * 640 / 480)
        val params = FrameLayout.LayoutParams(mPreviewW,mPreviewH)
        mCameraPreview.layoutParams = params

        fl_camera.addView(mCameraPreview)
        mCameraManager = CameraManager.getInstance()
        mCameraManager.setCameraHandle(mCameraPreview)
        mCameraManager.setCameraId(CameraConstant.CAMERAID_BACK)
        mCameraManager.setCameraPreviewWH(640,480)

//        Observable.just("1","2","3").flatMap(Function<String, ObservableSource<String>> {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
//            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        }

        bt_switch.setOnClickListener {
            mCameraManager.setCameraCapture { data, width, height ->
                val file = File("/sdcard/2.jpg")
                val outStream = FileOutputStream(file)

                val bitmap = BitmapFactory.decodeByteArray(data,0,data.size)

                val rotateBitmap = ImageUtil.rotaingImageView(bitmap, CameraConstant.CAMERAID_BACK)

                if (rotateBitmap.compress(Bitmap.CompressFormat.JPEG,100,outStream)) {
                    outStream.flush()
                    outStream.close()
                }
            }
        }
    }

    override fun initLayout(): Int {
       return R.layout.activity_camera
    }

    override fun onResume() {
        super.onResume()

        mCameraManager.startCamera()

        mCameraManager.setCameraPreview{ data, cameraId, width, height -> Log.e("CAMERA","time:"+System.currentTimeMillis())}
    }

    override fun onStop() {
        super.onStop()

        mCameraManager.stopCamera()
    }
}
