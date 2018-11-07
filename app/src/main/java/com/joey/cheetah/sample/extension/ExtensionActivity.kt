package com.joey.cheetah.sample.extension

import android.content.Intent
import com.joey.cheetah.core.ktextension.*
import com.joey.cheetah.mvp.AbsActivity
import com.joey.cheetah.sample.R
import kotlinx.android.synthetic.main.activity_extension.*

class ExtensionActivity : AbsActivity() {
    override fun initLayout(): Int {
        return R.layout.activity_extension
    }

    override fun initView() {
        btnJump.setOnClickListener { jump(ExtensionJumpActivity::class.java) }
        btnJumpWithParams.setOnClickListener {
            jumpWithParams(ExtensionJumpActivity::class.java)
                    .putString("params", if (etParams.isBlank()) "no params" else etParams.text.toString())
                    .jump()
        }
        btnJumpForResult.setOnClickListener{jumpForResult(ExtensionJumpActivity::class.java, 102)}
        tvVersionName.text = "Version name is ${versionName()}"
        tvVersionCode.text = "Version code is ${versionCode()}"
        tvScreenWidth.text = "Screen width is $screenWidth"
        tvScreenHeight.text = "Screen height is $screenHeight"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 102 && resultCode == 100) {
            etParams.setText("result:${data?.getStringExtra("result")}")
        }
     }

}
