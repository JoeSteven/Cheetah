package com.joey.cheetah.sample.dbdemo

import android.os.Bundle
import com.joey.cheetah.core.ktextension.logD
import com.joey.cheetah.core.ktextension.logE
import com.joey.cheetah.mvp.AbsPresenter
import com.joey.cheetah.sample.dbdemo.db.User
import java.util.*

/**
 * Description:
 * author:Joey
 * date:2018/9/12
 */
class DBDemoPresenter(view: DBDemoView) : AbsPresenter<DBDemoView>(view) {
    private var data: List<User>? = null
    private val userRepo = UserRepository()

    override fun onSaveData(outState: Bundle?) {
        outState?.putParcelableArrayList("user_db", data as ArrayList<out User>)
    }

    override fun onRestoredData(savedInstanceState: Bundle?) {
        data = savedInstanceState?.getParcelableArrayList("user_db")
        data?.let {
            mView?.showUsers(data)
        }
    }

    fun insert(user: User) {
        add(userRepo.insert(user)
                .doOnSuccess { logD("DB_demo", "insert success:$it") }
                .doOnError { logE("DB_demo", "insert error:$it") }
                .subscribe({ mView?.toast("insert success:$it") },
                        { mView?.toast("insert error:$it") }))
    }

    fun queryAll() {
        add(userRepo.queryAll()
                .doOnNext { data = it }
                .subscribe({ mView?.showUsers(it) },
                        { mView?.toast("error occurred:$it") }))
    }


    fun delete(user: User) {
        add(userRepo.delete(user)
                .doOnSuccess { logD("DB_demo", "delete user success:$it") }
                .doOnError { logD("DB_demo", "delete user failed:$it") }
                .subscribe({ mView?.toast("delete user:$it") },
                        { mView?.toast("delete error:$it") }))
    }
}