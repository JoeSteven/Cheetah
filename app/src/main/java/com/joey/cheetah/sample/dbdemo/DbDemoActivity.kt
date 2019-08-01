package com.joey.cheetah.sample.dbdemo

import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.joey.cheetah.core.ktextension.isBlank
import com.joey.cheetah.core.ktextension.logD
import com.joey.cheetah.core.list.CheetahAdapter
import com.joey.cheetah.mvp.AbsActivity
import com.joey.cheetah.mvp.auto.Presenter
import com.joey.cheetah.sample.R
import com.joey.cheetah.sample.dbdemo.adapter.UserItemViewBinder
import com.joey.cheetah.sample.dbdemo.db.User
import kotlinx.android.synthetic.main.activity_db_demo.*

class DbDemoActivity : AbsActivity(), DBDemoView {

    @Presenter
    private val presenter: DBDemoPresenter = DBDemoPresenter(this)

    private val adapter = CheetahAdapter()

    override fun initLayout(): Int {
        return R.layout.activity_db_demo
    }

    override fun initView() {
        btnInsert.setOnClickListener(this::insert)
        adapter.register(User::class.java, UserItemViewBinder().setOnClickListener(this::onItemClick))
        rvUser.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rvUser.adapter = adapter
    }

    override fun initData() {
        presenter.queryAll()
    }

    private fun insert(view: View) {
        if (etAge.isBlank() || etName.isBlank()) {
            toast("Name and age can't be empty!")
            return
        }
        val name = etName.text.toString()
        val age = etAge.text.toString().toInt()
        presenter.insert(User(name, age))
    }

    private fun onItemClick(position: Int, user: User) {
        //do something
        presenter.delete(user)
    }

    override fun showUsers(data: List<User>?) {
        if (data == null) return
        adapter.items = data
        adapter.notifyDataSetChanged()
    }

}
