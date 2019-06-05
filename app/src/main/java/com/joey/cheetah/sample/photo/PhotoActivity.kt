package com.joey.cheetah.sample.photo

import android.Manifest
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.KeyEvent
import com.joey.cheetah.core.ktextension.loadUrl
import com.joey.cheetah.core.ktextension.logD
import com.joey.cheetah.core.ktextension.logI
import com.joey.cheetah.core.ktextension.toBytes
import com.joey.cheetah.core.media.photo.PhotoGetter
import com.joey.cheetah.core.permission.PermissionListener
import com.joey.cheetah.core.permission.PermissionUtil
import com.joey.cheetah.mvp.AbsActivity
import com.joey.cheetah.sample.R
import kotlinx.android.synthetic.main.activity_photo.*
import java.io.File

class PhotoActivity : AbsActivity(), PermissionListener {
    override fun permissionDenied(permission: Array<out String>) {

    }

    override fun permissionGranted(permission: Array<out String>) {
    }

    override fun initLayout(): Int {
        return R.layout.activity_photo
    }

    override fun initView() {
        PermissionUtil.requestPermission(this, this, Manifest.permission.CAMERA)
        btnPhoto.setOnClickListener {
            goPhoto()
        }

        btnSelect.setOnClickListener {
            goSelect()
        }
    }

    private fun goSelect() {
        PhotoGetter.getPhotoByGallery(this)
                .subscribe({showPhoto(it)},
                        {toast("获取图片失败:$it")})
    }


    private fun goPhoto() {
        PhotoGetter.getPhotoByCamera(this)
                .subscribe({showPhoto(it)},
                        {toast("获取图片失败:$it")})
    }


    private fun showPhoto(path: String) {
        val imageBytes = File(path).toBytes()
        val base64 = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        if (imageBytes != null) {
            logD("photo_get", "bytes = $imageBytes")
            logD("photo_get", "base64 = $base64")
        }
        val img = Base64.decode(base64, Base64.DEFAULT)
        ivShow.loadUrl(BitmapFactory.decodeByteArray(img, 0, img.size))
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }
}
