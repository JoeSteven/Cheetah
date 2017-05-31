package com.joey.cheetah.core.base;


/**
 * description - model的基类，只操作数据以及提供原始数据，不关心数据去处
 * 对于数据的定制化操作应该放到presenter中去处理
 *
 * author - Joe.
 * create on 16/7/12.
 * change
 * change on .
 */
public class BaseModel {
    public BaseModel(){}
    public String getName() {
        return getClass().getSimpleName();
    }
}
