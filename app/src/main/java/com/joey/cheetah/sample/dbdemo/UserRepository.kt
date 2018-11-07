package com.joey.cheetah.sample.dbdemo

import com.joey.cheetah.core.async.schedulers.SchedulersHelper
import com.joey.cheetah.core.ktextension.toSingle
import com.joey.cheetah.sample.dbdemo.db.AppDataBase
import com.joey.cheetah.sample.dbdemo.db.User
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Description:
 * author:Joey
 * date:2018/9/13
 */
class UserRepository {
    fun insert(vararg user: User): Single<Array<Long>> {
        return AppDataBase
                .INSTANCE
                .toSingle { AppDataBase.INSTANCE.userDao().insert(*user) }
                .compose(SchedulersHelper.io_main())
    }

    fun delete(vararg user: User): Single<Int> {
        return AppDataBase
                .INSTANCE
                .toSingle { AppDataBase.INSTANCE.userDao().delete(*user) }
                .compose(SchedulersHelper.io_main())
    }

    fun queryAll(): Flowable<List<User>> {
        return AppDataBase
                .INSTANCE
                .userDao()
                .queryAll()
                .compose(SchedulersHelper.io_main())
    }
}