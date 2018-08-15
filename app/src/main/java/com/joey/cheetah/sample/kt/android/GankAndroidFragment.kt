package com.joey.cheetah.sample.kt.android

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.joey.cheetah.core.list.CheetahAdapter
import com.joey.cheetah.core.utils.Jumper
import com.joey.cheetah.mvp.AbsFragment
import com.joey.cheetah.mvp.auto.Presenter
import com.joey.cheetah.sample.R
import com.joey.cheetah.sample.constant.Constant
import com.joey.cheetah.sample.kt.GankData
import com.joey.cheetah.sample.kt.GankPresenter
import com.joey.cheetah.sample.kt.IGankView
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
class GankAndroidFragment : AbsFragment(),IGankView {
    @Presenter
    private val presenter = GankPresenter(this)

    private val adapter = CheetahAdapter()

    override fun initLayout(): Int {
        return R.layout.fragment_list
    }

    override fun initArguments(arguments: Bundle?) {}

    override fun initView() {
        adapter.register(GankData::class.java, GankAndroidViewBinder().setOnClickListener { _, data -> toDetail(data) })
        rvList.layoutManager = LinearLayoutManager(activity)
        rvList.adapter = adapter
    }

    override fun initData() {
        presenter.queryAndroid()
    }

    private fun toDetail(data: GankData) {
        Jumper.make(activity, GankDetailActivity::class.java)
                .putString(Constant.INTENT_DETAIL_URL, data.url)
                .jump()
    }

    override fun showContent(data: List<GankData>) {
        adapter.items = data
        adapter.notifyDataSetChanged()
    }

    override fun stopLoading() {
        progress.visibility = View.GONE
    }

    override fun loading() {
        progress.visibility = View.VISIBLE
    }


}