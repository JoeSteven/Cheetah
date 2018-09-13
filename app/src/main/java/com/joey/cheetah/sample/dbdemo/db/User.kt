package com.joey.cheetah.sample.dbdemo.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Description:
 * author:Joey
 * date:2018/9/11
 */
@Keep
@Parcelize
@Entity
data class User constructor(
        @SerializedName("name")
        var name: String,

        @SerializedName("age")
        var age: Int,

//        @ColumnInfo(name = "id")
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0
) : Parcelable
