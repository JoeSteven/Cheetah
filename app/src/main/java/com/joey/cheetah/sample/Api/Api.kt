package com.joey.cheetah.sample.Api

import com.joey.cheetah.core.net.NetworkCore

/**
 * Description:
 * author:Joey
 * date:2018/8/1
 */
object Api {

    private const val GANK = 1


    fun init(){
        NetworkCore.init(1)
                .registerService(GANK, "http://gank.io/", GankService::class.java)
    }

    fun gank():GankService{
        return NetworkCore.inst().service<GankService>(GANK)
    }
}