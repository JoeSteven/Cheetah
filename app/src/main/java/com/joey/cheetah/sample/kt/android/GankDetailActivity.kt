package com.joey.cheetah.sample.kt.android

import android.content.Intent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.joey.cheetah.mvp.AbsActivity
import com.joey.cheetah.sample.R
import com.joey.cheetah.sample.constant.Constant
import kotlinx.android.synthetic.main.activity_gank_detail.*

class GankDetailActivity : AbsActivity() {
    lateinit var url:String
    override fun initLayout(): Int {
        return R.layout.activity_gank_detail
    }

    override fun initArguments(intent: Intent) {
        url = intent.getStringExtra(Constant.INTENT_DETAIL_URL)
    }

    override fun initView() {
        webview.loadUrl(url)
        webview.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) progress.visibility = View.GONE
            }
        }
    }

}
