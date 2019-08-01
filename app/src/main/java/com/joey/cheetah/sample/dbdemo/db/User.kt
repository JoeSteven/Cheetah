package com.joey.cheetah.sample.dbdemo.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import android.os.Parcelable
import androidx.annotation.Keep
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
