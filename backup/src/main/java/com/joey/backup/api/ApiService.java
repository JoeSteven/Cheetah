package com.joey.backup.api;

import com.google.gson.JsonObject;
import com.ujipin.erp.bean.BatchSheetBean;
import com.ujipin.erp.bean.CheckBean;
import com.ujipin.erp.bean.LoginBean;
import com.ujipin.erp.bean.ResponseBean;
import com.ujipin.erp.bean.SearchBean;
import com.ujipin.erp.bean.StorageInfoBean;
import com.ujipin.erp.bean.SwapInventoryBean;
import com.ujipin.erp.bean.UpgradeCheckBean;
import com.ujipin.erp.bean.UserBean;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * description - 封装API请求
 * url均以/结尾，开头不加/
 * <p/>
 * author - Joe.
 * create on 16/7/13.
 * change
 * change on .
 */

public interface ApiService {

    /**
     * 登陆
     * @param userInfo 用户名及密码
     * @return LoginBean
     */
    @POST(API.MANAGE_API +"sign/in/")
    Observable<ResponseBean<LoginBean>> login(@Body JsonObject userInfo);


    /**
     * 用户信息
     * @return UserBean//@Path("user_id")int userId
     */
    @GET(API.MANAGE_API+"user/me/")
    Observable<ResponseBean<UserBean>> userInfo();


    /**
     * @param code 拣货波次单条码
     * @return
     */
    @GET(API.MANAGE_API+"stock/out/{batch_code}/")
    Observable<ResponseBean<BatchSheetBean>> getPickBatch(@Path("batch_code") String code);

    /**
     * 开始拣货,获取拣货任务接口
     * @param code 拣货波次单条码
     * @return
     */
    @POST(API.MANAGE_API+"stock/out/{batch_code}/picking/start/")
    Observable<ResponseBean<BatchSheetBean>> startPicking(@Path("batch_code") String code);


    /**
     * 提交单个拣货任务接口
     * @param code 拣货波次单
     * @param task 任务信息
     * @return
     */
    @POST(API.MANAGE_API+"stock/out/{batch_code}/picking/")
    Observable<ResponseBean<BatchSheetBean>> commitPickingTask(@Path("batch_code") String code, @Body JsonObject task);

    /**
     * 结束拣货接口
     * @param code 拣货波次单
     * @return
     */
    @POST(API.MANAGE_API+"stock/out/{batch_code}/picking/finish/")
    Observable<ResponseBean<BatchSheetBean>> commitAllPicking(@Path("batch_code") String code);


    /**
     * 开始分拣
     * @param code 分拣波次单
     * @return
     */
    @POST(API.MANAGE_API+"stock/out/{batch_code}/sorting/start/")
    Observable<ResponseBean<BatchSheetBean>> startSorting(@Path("batch_code") String code);
    /**
     * 结束分拣
     * @param code 分拣波次单
     * @return
     */
    @POST(API.MANAGE_API+"stock/out/{batch_code}/sorting/finish/")
    Observable<ResponseBean<BatchSheetBean>> commitSorting(@Path("batch_code") String code);

    /**
     * 查询
     * @param code 条码
     * @return SearchBean
     */
    @GET(API.MANAGE_API+"search/{code}/")
    Observable<ResponseBean<SearchBean>> search(@Path("code") String code);

    /**
     * 盘点查询
     * @param location 库位条码
     * @param sku 单品条码
     * @return 盘点实体类
     */
    @GET(API.MANAGE_API+"take/stock/")
    Observable<ResponseBean<CheckBean>> checkInfo(@Query("storage_location") String location, @Query("code") String sku);

    /**
     * 盘点提交
     * @param jsonObject 盘点信息
     * @return 盘点实体类
     */
    @POST(API.MANAGE_API+"take/stock/")
    Observable<ResponseBean<CheckBean>> checkCommit(@Body JsonObject jsonObject);
    /**
     * 升级检查
     * @return 升级信息返回
     */
    @POST(API.MANAGE_API+"app/release/")
    Observable<ResponseBean<UpgradeCheckBean>> checkUpgrade();

    /**
     * 查询库位信息
     * @param storageId
     * @return
     */
    @GET(API.MANAGE_API+"inventory/storage/location/")
    Observable<ResponseBean<StorageInfoBean>> getStorageInfo(@Query("id") String storageId);
    /**
     * storage_location=I335&code=G037311-P0037311 库位和sku码
     * @return
     */

    @GET(API.MANAGE_API+"inventory/")
    Observable<ResponseBean<List<SwapInventoryBean>>> getInventoryList(@Query("storage_location") String storeageLoc, @Query("code") String code);

    /**
     * 获取一个sku在当前库位的详情
     * @param storeageLoc sku所在库位
     * @param code sku 码
     * @returni
     */
    @GET(API.MANAGE_API+"inventory/detail/")
    Observable<ResponseBean<SwapInventoryBean>> getStorageDetails(@Query("storage_location") String storeageLoc, @Query("code") String code);

    /**
     * 移库操作接口
     * @param sourceStoreageLoc
     * @param target_storage_location
     * @param code
     * @param number
     * @return
     */
    @FormUrlEncoded
    @POST(API.MANAGE_API+"inventory/swap/")
    Observable<ResponseBean<SwapInventoryBean>> swapInventory(@Field("source_storage_location") String sourceStoreageLoc,
                                                              @Field("target_storage_location") String target_storage_location,
                                                              @Field("code") String code,
                                                              @Field("number") int number);
}
