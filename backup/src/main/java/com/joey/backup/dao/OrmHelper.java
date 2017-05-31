package com.joey.backup.dao;

import android.text.TextUtils;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * description - 数据库通用帮助类
 * 增删改查
 * author - Joe.
 * create on 16/7/14.
 * change
 * change on .
 */
public class OrmHelper {
    static LiteOrm liteOrm;


    /**
     * 获取liteOrm
     */
    public static LiteOrm getLiteOrm() {
        if (liteOrm == null) {
            liteOrm = LiteOrm.newSingleInstance(Erp.getContext(), Constant.DATABASE_ERP);
        }
        liteOrm.setDebugged(Constant.IS_DEBUG);
        return liteOrm;
    }

    public static <T extends BaseBean>WhereBuilder getWhereBuilder(Class<T> clazz, String[] columns, Object[]...values){
        WhereBuilder whereBuilder = new WhereBuilder(clazz);
        for(int i = 0;i<columns.length;i++){
            if(values[i].length <2){
                whereBuilder.and("("+columns[i]+"=?)",(Object[]) values[i]);
            }else {
                whereBuilder.and("("+columns[i]+"=?",(Object[]) values[i]);
            }
            for(int j = 0;j<values[i].length-1;j++){
                if(j == values[i].length-2){
                    whereBuilder.or(columns[i]+"=?)");
                }else{
                    whereBuilder.or(columns[i]+"=?");
                }

            }
        }
        return whereBuilder;
    }
//增    

    /**
     * 插入单条数据
     *
     * @param t 要插入的bean
     * @return 插入数据的id
     */
    public static <T extends BaseBean> long save(@NonNull T t) {
        LiteOrm liteOrm = getLiteOrm();
        long id = liteOrm.save(t);
        return id;
    }

    /**
     * 插入多条数据
     *
     * @param collection 多条数据集合
     * @return 插入数据的id
     */
    public static int save(Collection<? extends BaseBean> collection) {
        LiteOrm liteOrm = getLiteOrm();
        int count = liteOrm.save(collection);
        return count;
    }

    /**
     * 删除指定数据
     *
     * @param t 数据
     * @return 删除条数
     */
//删
    public static <T extends BaseBean> int delete(T t) {
        if (t.getTableId() == 0) {
            ELog.w("OrmHelper", t.getClass().getSimpleName() + " doesn't have id!can't delete by id");
            return 0;
        }
        return getLiteOrm().delete(t);
    }
    public static <T extends BaseBean> int delete(Collection<T> collection){
        return getLiteOrm().delete(collection);

    }

    public static <T extends BaseBean> int delete(Class<T> clazz, long start, long end, String orderAscColumn){
        return getLiteOrm().delete(clazz,start,end,orderAscColumn);
    }

    /**
     * 条件删除 单个列
     * @param clazz 表
     * @param column 列名
     * @param whereArgs 列值
     * @return
     */
    public static <T extends BaseBean> int delete(Class<T> clazz, String column, Object...whereArgs){
        WhereBuilder whereBuilder = new WhereBuilder(clazz,column+"=?",whereArgs);
        for(int i = 0;i<whereArgs.length-1;i++){
            whereBuilder.or(column+"=?");
        }
        return getLiteOrm().delete(whereBuilder);
    }

    /**
     * 多条件删除 多个列
     * 注意在调用该方法时 whereArgs的顺序必须跟columns的顺序一致，否则会报错
     * @param clazz 表
     * @param columns 列
     * @param whereArgs 列值
     * @param <T>
     * @return
     */
    public static <T extends BaseBean> int delete(Class<T> clazz, String[] columns, Object[]...whereArgs){
        WhereBuilder whereBuilder =getWhereBuilder(clazz,columns,whereArgs);
        ELog.d("OrmHelper",whereBuilder.getWhere());
        return getLiteOrm().delete(whereBuilder);
    }
    /**
     * 删除该表中的所有数据
     *
     * @param clazz 表类
     * @return
     */
    public static <T extends BaseBean> int deleteAll(Class<T> clazz) {
        return getLiteOrm().deleteAll(clazz);
    }
//改

    public static <T extends BaseBean> int update(T t){
        return getLiteOrm().update(t);
    }

    public static <T extends BaseBean> int update(T t, String[] updateColumns, Object[] updateValues){
        return getLiteOrm().update(t,new ColumnsValue(updateColumns,updateValues),ConflictAlgorithm.Ignore);
    }

    /**
     * 更新数据
     * @param clazz 表
     * @param whereColumns 修改的列条件
     * @param whereValues 修改的列条件值
     * @param updateColumns 要修改的列
     * @param updateValues 要修改的值
     * @param <T>
     * @return
     */
    public static <T extends BaseBean> int update(Class<T> clazz, String[] whereColumns, Object[] whereValues, String[] updateColumns, Object[] updateValues){
        WhereBuilder whereBuilder = getWhereBuilder(clazz,whereColumns,whereValues);
        ColumnsValue value = new ColumnsValue(updateColumns,updateValues);
        return getLiteOrm().update(whereBuilder,value, ConflictAlgorithm.Ignore);
    }

//查    

    /**
     * 按照id查询
     *
     * @param t 实体，id不能为空
     * @return 单条数据
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseBean> T queryById(@NonNull T t) {
        if (t.getTableId() == 0) {
            ELog.w("OrmHelper", t.getClass().getSimpleName() + " doesn't have id!can't query by id");
            return null;
        }

        return (T) queryById(t.getTableId(), t.getClass());
    }

    /**
     * 按照id查询
     *
     * @param id     id
     * @param tClass 模型类
     * @return 数据
     */
    public static <T extends BaseBean> T queryById(String id, Class<T> tClass) {
        LiteOrm liteOrm = getLiteOrm();
        T t = liteOrm.queryById(id, tClass);
        return t;
    }

    /**
     * 按照id查询
     *
     * @return 数据
     */
    public static <T extends BaseBean> T queryById(long id, Class<T> tClass) {
        LiteOrm liteOrm = getLiteOrm();
        T t = liteOrm.queryById(id, tClass);
        return t;
    }

    /**
     * 查询表中全部数据
     *
     * @param t 该表的实体类
     * @return 实体类列表
     */
    public static <T extends BaseBean> ArrayList<T> queryAll(T t) {
        return queryAll(t.getClass(), null);
    }

    public static <T extends BaseBean> ArrayList<T> queryAll(Class<T> clazz) {
        return queryAll(clazz, null);
    }


    public static <T extends BaseBean> ArrayList<T> queryAll(T t, String orderBy) {
        return queryAll(t.getClass(), orderBy);
    }


    /**
     * 查询表中全部数据
     *
     * @param clazz 类
     * @return 实体类列表
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseBean> ArrayList<T> queryAll(Class<?> clazz, String orderBy) {
        LiteOrm liteOrm = getLiteOrm();
        QueryBuilder<T> queryBuilder = (QueryBuilder<T>) new QueryBuilder<>(clazz);
        if (!TextUtils.isEmpty(orderBy)) queryBuilder.orderBy(orderBy);
        ArrayList<T> arrayList = liteOrm.query(queryBuilder);
        return arrayList;
    }

    /**
     * 根据某列精确查询
     *
     * @param clazz  实体类
     * @param column 列名
     * @param params 值
     * @return 实体类列表
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseBean> ArrayList<T> query(Class<T> clazz, String column, Object... params) {
        return query(clazz, column, null, params);
    }

    /**
     * 按照多个条件精确查询
     *
     * @param clazz   实体类
     * @param columns 列 数组
     * @param params  值
     * @return 实体类列表
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseBean> ArrayList<T> query(Class<T> clazz, String[] columns, Object[]... params) {
        return query(clazz, columns, null, params);
    }

    /**
     * 按照多个条件精确查询
     *
     * @param clazz 实体类
     * @param map   key为列名，value为值
     * @return 实体类列表
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseBean> ArrayList<T> query(Class<T> clazz, Map<String, Object[]> map) {
        return query(clazz, map, null);
    }

    /**
     * 根据某列精确查询,并排序
     *
     * @param clazz   实体类
     * @param column  列名
     * @param orderBy 排序的列名
     * @param params  值
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseBean> ArrayList<T> queryOrderBy(Class<T> clazz, String column, String orderBy, Object... params) {
        return query(clazz, column, orderBy, params);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseBean> ArrayList<T> queryOrderBy(Class<T> clazz, String[] columns, String orderBy, Object[]... params) {
        return query(clazz, columns, orderBy, params);
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseBean> ArrayList<T> queryOrderBy(Class<T> clazz, Map map, String orderBy) {
        return query(clazz, map, orderBy);
    }

    /**
     * 按照某个条件精确查询
     *
     * @param clazz  类
     * @param column 列名
     * @param params 值
     * @return 实体类列表
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseBean> ArrayList<T> query(Class<T> clazz, String column, String orderBy, Object... params) {
        Map map = new HashMap<>();
        map.put(column, params);
        return query(clazz, map, -1, -1, orderBy);
    }

    /**
     * 多个条件查询
     *
     * @param clazz   类
     * @param columns 列 数组
     * @param params  值
     * @return 实体类列表
     */
    public static <T extends BaseBean> ArrayList<T> query(Class<T> clazz, String[] columns, String orderBy, Object[]... params) {
        HashMap<String, Object[]> map = new HashMap<>();
        for (int i = 0; i < columns.length; i++) {
            map.put(columns[i], params[i]);
        }
        return query(clazz, map, -1, -1, orderBy);
    }

    /**
     * 同上
     */
    public static <T extends BaseBean> ArrayList<T> query(Class<T> clazz, Map map, String orderBy) {
        return query(clazz, map, -1, -1, orderBy);
    }

    /**
     * 按照某个条件精确查询
     *
     * @param clazz    类
     * @param queryMap 查询map key为列名，value为数组
     * @param start    分页查询开头
     * @param length   分页查询长度
     * @param orderBy  排序
     * @return 实体类列表
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseBean> ArrayList<T> query(Class<T> clazz, Map queryMap, int start, int length, String orderBy) {
        LiteOrm liteOrm = getLiteOrm();
        QueryBuilder<T> queryBuilder = new QueryBuilder<>(clazz);
        if (length >= 0 && start >= 0) queryBuilder.limit(start, length);
        if (!TextUtils.isEmpty(orderBy)) queryBuilder.orderBy(orderBy);
        Set<Map.Entry> set = queryMap.entrySet();
        Iterator<Map.Entry> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            if (queryMap.get(entry.getKey()) instanceof Object[]) {
                if(((Object[]) queryMap.get(entry.getKey())).length>1){
                    queryBuilder.whereAnd("("+entry.getKey() + "=?", (Object[]) queryMap.get(entry.getKey()));
                }else{
                    queryBuilder.whereAnd("("+entry.getKey() + "=?)", (Object[]) queryMap.get(entry.getKey()));
                }

                for (int i = 0; i < ((Object[]) queryMap.get(entry.getKey())).length - 1; i++) {
                    if(i==((Object[]) queryMap.get(entry.getKey())).length - 2){
                        queryBuilder.whereOr(entry.getKey() + "=?)");
                    }else{
                        queryBuilder.whereOr(entry.getKey() + "=?");
                    }
                }
            } else {
                queryBuilder.whereAnd("("+entry.getKey() + "=?)", queryMap.get(entry.getKey()));
            }
        }
        ELog.d("OrmHelper",queryBuilder.getwhereBuilder().getWhere());
        ArrayList<T> arrayList = liteOrm.query(queryBuilder);
        return arrayList;
    }

    private static void close() {
        if (liteOrm != null) liteOrm.close();
    }
}
