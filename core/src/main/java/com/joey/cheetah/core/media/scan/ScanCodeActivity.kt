package com.joey.cheetah.core.media.scan

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.joey.zxingdemo.ScanCodeHelper

/**
 * Description:
 * author:Joey
 * date:2018/10/17
 */
class ScanCodeActivity : AppCompatActivity() {
    var key = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        key = intent.getStringExtra("Scan_Result")
        IntentIntegrator(this).setCaptureActivity(ScanCodeHelper.scanActivity).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null ) {
            if (result.contents == null) {
                // canceled
                ScanCodeHelper.disPatchError(key, ScanCodeCancelException())
            } else {
                ScanCodeHelper.dispatchSuccess(key, result.contents)
            }
        } else {
            ScanCodeHelper.disPatchError(key, ScanCodeFailedException())
        }
        finish()
    }
}