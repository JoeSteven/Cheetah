package com.joey.backup.api;

import android.text.TextUtils;

import com.ujipin.erp.Erp;
import com.ujipin.erp.base.RxJavaManager;
import com.ujipin.erp.global.Constant;
import com.ujipin.erp.global.Event;
import com.ujipin.erp.helper.ConfigHelper;
import com.ujipin.erp.uitls.CommonUtil;
import com.ujipin.erp.uitls.ELog;
import com.ujipin.erp.uitls.UString;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;

/**
 * description - 所有Retrofit Service都从该类中获取
 * 封装okHttp client 以及通用header，log等，Host及相关常量放在该类中
 * 通过okHttpClient构建Retrofit,通过Retrofit开启接口service
 * author - Joe.
 * create on 16/7/13.
 * change
 * change on .
 */
public class API {
    private static API mInstance;
    public static final String HOST_TEST = "http://erp.ujipin.cn/";//测试环境
    public static final String HOST = "https://erp.ujipin.com";//正式环境
    public static final String MANAGE_API = "manage/erp/api/";//api
    public static final String HOST_LOCAL = "http://172.16.0.129:8000/";
    private static  OkHttpClient okHttpClient;
    private ApiService mApiService;
    private API() {
        mApiService = refreshApiService();
        RxJavaManager mRxJavaManager = new RxJavaManager();
        mRxJavaManager.subscribe(Event.EVENT_LOGIN_SUCCESS, new Action1<Object>() {
            @Override
            public void call(Object loginBean) {
                refreshApiService();
            }
        });
        mRxJavaManager.subscribe(Event.EVENT_EXIT_SUCCESS, new Action1<Object>() {
            @Override
            public void call(Object o) {
                ConfigHelper.saveToken("");
                ConfigHelper.saveTokenType("");
                refreshApiService();
            }
        });
    }

    public static synchronized API getInstance() {
        if (mInstance == null) mInstance = new API();
        return mInstance;
    }

    /**获取ApiService*/
    public ApiService getApiService(){
        return mApiService;
    }

    /**
     * 获取更新后的ApiService,主要是更新header,或者相应的自定义配置
     * @return ApiService
     */
    public ApiService refreshApiService(){
        mApiService = getRetrofit(mHeader, Constant.IS_DEBUG?HOST_TEST:HOST).create(ApiService.class);
        return mApiService;
    }
    /**
     * header
     */
    public static  Interceptor mHeader = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //如果token为空则所有请求header里 不加入token信息
            if (TextUtils.isEmpty(ConfigHelper.getToken())) return chain.proceed(chain.request());

            Request request = chain.request().newBuilder()
                    .addHeader("Authorization", ConfigHelper.getTokenType()+" "+ConfigHelper.getToken())
                    .addHeader("Content-Type", "application/json")
                    .addHeader("brand", UString.encodeChinese(android.os.Build.BRAND))//手机品牌
                    .addHeader("model", UString.encodeChinese(android.os.Build.MODEL))//手机型号
                    .addHeader("channel", CommonUtil.getChannel(Erp.getContext()))//渠道号
                    .addHeader("udid", CommonUtil.getDeviceId(Erp.getContext()))//设备唯一号
                    .build();
            return chain.proceed(request);
        }
    };

    /**
     * LogCat
     */
    HttpLoggingInterceptor mLog = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            ELog.api(message);
        }
    });

    /**
     * 创建okHttpClient
     */
    private OkHttpClient getOkHttpClient(Interceptor header) {
        mLog.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (okHttpClient == null){
            return new OkHttpClient.Builder()
                    .readTimeout(5000, TimeUnit.MILLISECONDS)
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .addInterceptor(header)
                    .addInterceptor(mLog)
                    .build();
        }else{
            return  okHttpClient;
        }
    }
    public static void setHeader(Interceptor header){
        List<Interceptor> interceptorList = okHttpClient.networkInterceptors();
        interceptorList.remove(mHeader);
        okHttpClient = okHttpClient.newBuilder()
                .addInterceptor(header)
                .build();
    }
    /**
     * 创建Retrofit
     */
    private Retrofit getRetrofit(Interceptor header, String host) {
        return new Retrofit.Builder()
                .client(getOkHttpClient(header))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(host)
                .build();
    }

}
