package com.joey.cheetah.sample.kt

import android.os.Bundle
import com.joey.cheetah.mvp.AbsFragmentActivity
import com.joey.cheetah.sample.R
import com.joey.cheetah.sample.kt.android.GankAndroidFragment
import com.joey.cheetah.sample.kt.surprise.GankSurpriseFragment
import kotlinx.android.synthetic.main.activity_gank.*

class GankActivity : AbsFragmentActivity() {
    private val tagAndroid = "tag_android"
    private val tagSurprise = "tag_surprise"
    private lateinit var androidFragment: GankAndroidFragment
    private lateinit var surpriseFragment: GankSurpriseFragment
    override fun initLayout(): Int {
        return R.layout.activity_gank
    }

    override fun initView() {
        btSurprise.setOnClickListener { toSurprise() }
        btAndroid.setOnClickListener {toAndroid()}
    }

    private fun toAndroid() {
        switchFragment(androidFragment, R.id.flContent, tagAndroid)
    }

    private fun toSurprise() {
        switchFragment(surpriseFragment, R.id.flContent, tagSurprise)
    }

    override fun createFragment() {
        androidFragment = GankAndroidFragment()
        surpriseFragment = GankSurpriseFragment()
    }

    override fun restoreFragment(savedInstanceState: Bundle?) {
        androidFragment = fragmentManager().findFragmentByTag(tagAndroid) as GankAndroidFragment
        surpriseFragment = fragmentManager().findFragmentByTag(tagSurprise) as GankSurpriseFragment
    }

    override fun attachFragment() {
        addFragment(androidFragment, R.id.flContent, tagAndroid)
    }

}
