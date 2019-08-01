package com.joey.cheetah.core.media.photo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.appcompat.app.AppCompatActivity
import com.joey.cheetah.core.R
import com.joey.cheetah.core.ktextension.jumpForResult
import com.joey.cheetah.core.ktextension.jumpWithParams
import com.joey.cheetah.core.storage.FileHelper
import com.joey.cheetah.core.utils.ResGetter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Description:
 * author:Joey
 * date:2018/10/19
 */
class PhotoGetActivity: AppCompatActivity() {
    companion object {
        const val TAKE_PHOTO = 1
        const val SELECT_PHOTO = 2
    }
    var type = TAKE_PHOTO
    var key:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        key = intent.getStringExtra("subject_key")
        type = intent.getIntExtra("type", TAKE_PHOTO)
        when (type) {
            TAKE_PHOTO -> takePhoto()
            SELECT_PHOTO -> selectPhoto()
        }
    }

    var file:File? = null
    private fun takePhoto() {
        file = null
        jumpWithParams(MediaStore.ACTION_IMAGE_CAPTURE)
                .putParcelable(MediaStore.EXTRA_OUTPUT, getMediaFileUri())
                .jumpForResult(TAKE_PHOTO)
    }

    private fun selectPhoto() {
        jumpForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), SELECT_PHOTO)
    }

    private fun getMediaFileUri(): Uri {
        val dir = PhotoGetter.photoDir
        FileHelper.ensureDirExists(dir)
        val time = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val mediaFile = File("${dir.path}/photo_$time.jpg")
        file = mediaFile
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(this, ResGetter.string(R.string.photo_file_provider), mediaFile)
        }
        return Uri.fromFile(mediaFile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TAKE_PHOTO -> takeResult(resultCode)
            SELECT_PHOTO -> selectResult(resultCode, data)
        }
        finish()
    }

    private fun selectResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            try {
                val selectImageUri = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = contentResolver.query(selectImageUri, filePathColumn, null, null, null);
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val picturePath = cursor.getString(columnIndex)
                cursor.close()
                PhotoGetter.dispatchSuccess(key, picturePath)
            } catch (e: Exception) {
                PhotoGetter.disPatchError(key, e)
            }
        } else {
            PhotoGetter.disPatchError(key, PhotoTakeFailException())
        }


    }

    private fun takeResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK && file != null) {
            PhotoGetter.dispatchSuccess(key, file!!.path)
        } else {
            PhotoGetter.disPatchError(key, PhotoTakeFailException())
        }
    }
}