package com.joey.cheetah.sample.dbdemo.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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