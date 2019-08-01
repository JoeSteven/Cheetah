package com.joey.cheetah.sample.face

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joey.cheetah.core.async.RxJavaManager
import com.joey.cheetah.core.ktextension.screenHeight
import com.joey.cheetah.core.ktextension.screenWidth
import com.joey.cheetah.core.ui.face.FaceRecognitionLayout
import com.joey.cheetah.sample.R
import kotlinx.android.synthetic.main.activity_face.*
import java.util.*

class FaceActivity : AppCompatActivity() {
    private var loop = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face)
        val random = Random()
        var x = 1
        faceView.setIFaceRecognitionView(FaceView())
        RxJavaManager().execute {
            while (loop) {
                Thread.sleep(2000)

                val faceList = mutableListOf<FaceRecognitionLayout.FaceInfo>()
                x+=1
                val count = if (x<4) 1 else 5//random.nextInt(10)
                for (i in 0..count) {
                    var width = random.nextInt(300)
                    if (width < 200) width = 200
                    faceList.add(FaceRecognitionLayout.FaceInfo(random.nextInt(screenWidth- width).toFloat(),random.nextInt(screenHeight-width).toFloat(), width.toFloat(), width.toFloat(), info = "name"))
//                    faceList.add(FaceRecognitionLayout.FaceInfo(x.toFloat(),2*x.toFloat(), width.toFloat(), width.toFloat()))
                }
                faceView.showFaceInfo(faceList)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loop = false
    }
}
