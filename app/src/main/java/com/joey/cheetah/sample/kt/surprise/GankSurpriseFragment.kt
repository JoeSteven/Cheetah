package com.joey.cheetah.sample.kt.surprise

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.View
import com.joey.cheetah.core.list.CheetahAdapter
import com.joey.cheetah.mvp.AbsFragment
import com.joey.cheetah.mvp.auto.Presenter
import com.joey.cheetah.sample.R
import com.joey.cheetah.sample.kt.GankData
import com.joey.cheetah.sample.kt.GankPresenter
import com.joey.cheetah.sample.kt.IGankView
import com.joey.cheetah.sample.kt.android.GankAndroidViewBinder
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * Description:
 * author:Joey
 * date:2018/8/15
 */
class GankSurpriseFragment : AbsFragment(), IGankView {

    @Presenter
    private val presenter = GankPresenter(this)

    private val adapter = CheetahAdapter()
    override fun initLayout(): Int {
        return R.layout.fragment_list
    }

    override fun initArguments(arguments: Bundle?) {
    }

    override fun initView() {
        adapter.register(GankData::class.java, GankSurpriseViewBinder().setOnLongClickListener { _, data ->  download(data)})
        rvList.layoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(2, androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL)
        rvList.adapter = adapter
    }

    private fun download(data: GankData) : Boolean{
        presenter.download(data.url)
        return true
    }

    override fun initData() {
        presenter.querySurprise()
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