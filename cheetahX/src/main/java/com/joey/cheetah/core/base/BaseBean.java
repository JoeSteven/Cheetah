package com.joey.cheetah.core.base;



/**
 * description - 实体类的基类
 * 如果需要将实体类作为数据库模型,必须实现 getTableId() 方法
 * 返回值应该是该类在数据表中的唯一id, primary key
 *
 * author - Joe.
 * create on 16/7/14.
 * change
 * change on .
 */
public abstract class BaseBean {

    public long getTableId() {
        return 0;
    }


    public abstract String getTable();

}
