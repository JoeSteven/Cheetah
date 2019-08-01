package com.joey.cheetah.core.ktextension

import androidx.room.RoomDatabase
import io.reactivex.Single

/**
 * Description: extension for database
 * author:Joey
 * date:2018/9/13
 */

/**
 * for insert, delete, update method in Room
 */
fun <T> RoomDatabase.toSingle(operator: () -> T): Single<T> {
    return Single.create {
        try {
            val result = operator()
            if (result != null) it.onSuccess(result)
            else throw NullPointerException("result of operator can't be null!")
        } catch (e: Throwable) {
            it.onError(e)
        }
    }
}