package com.joey.cheetah.sample.dbdemo.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import com.joey.cheetah.core.global.Global

/**
 * Description:
 * author:Joey
 * date:2018/9/11
 */
@Database(entities = [(User::class)], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        val INSTANCE:AppDataBase by lazy {
            Room.databaseBuilder(Global.context(), AppDataBase::class.java, "Sample_DataBase")
                    .build()
        }
    }

}