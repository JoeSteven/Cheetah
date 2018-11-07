package com.joey.cheetah.sample.dbdemo.db

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Description:
 * author:Joey
 * date:2018/9/11
 */
@Dao
interface UserDao {
    @Delete
    fun delete(vararg user: User):Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(vararg user: User):Array<Long>

    @Query("SELECT * FROM user")
    fun queryAll():Flowable<List<User>>

}