package com.joey.cheetah.sample.extension

import android.content.Intent
import com.joey.cheetah.core.ktextension.isBlank
import com.joey.cheetah.core.utils.Jumper
import com.joey.cheetah.mvp.AbsActivity
import com.joey.cheetah.sample.R
import kotlinx.android.synthetic.main.activity_extension_jump.*

class ExtensionJumpActivity : AbsActivity() {
    override fun initLayout(): Int {
        return R.layout.activity_extension_jump
    }

    override fun initArguments(intent: Intent) {
        super.initArguments(intent)
        tvParams.text = "params is:${intent.getStringExtra("params")}"
    }
    override fun initView() {
        btnResult.setOnClickListener { setResult(100, Jumper.make()
                .putString("result", if(etResult.isBlank()) "null result" else etResult.text.toString())
                .intent)
            finish()
        }
    }

    override fun onBackPressed() {
        setResult(100, Jumper.make().putString("result", "onback pressed").intent)
        super.onBackPressed()
    }

}
