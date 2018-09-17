package com.joey.cheetah.sample.dbdemo

import com.joey.cheetah.mvp.IView
import com.joey.cheetah.sample.dbdemo.db.User

/**
 * Description:
 * author:Joey
 * date:2018/9/12
 */
interface DBDemoView : IView{
    fun showUsers(data: List<User>?)
}