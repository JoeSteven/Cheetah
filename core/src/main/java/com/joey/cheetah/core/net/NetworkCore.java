package com.joey.cheetah.core.net;

import android.util.SparseArray;


import com.joey.cheetah.core.utils.CLog;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description: create different services for API
 * author:Joey
 * date:2018/7/26
 */
public class NetworkCore {
    private static NetworkCore sNetworkCore;
    private SparseArray<Object> mServices;
    private HttpUrl.Builder mParams;
    private Headers.Builder mHeaders;

    /**
     * invoke init when application create
     *
     * @param serviceNumber
     * @return
     */
    public static NetworkCore init(int serviceNumber) {
        if (sNetworkCore != null) return sNetworkCore;
        sNetworkCore = new NetworkCore(serviceNumber);
        return sNetworkCore;
    }

    public static NetworkCore inst() {
        return sNetworkCore;
    }

    private NetworkCore(int serviceNumber) {
        this.mServices = new SparseArray<>(serviceNumber);
        mHeaders = new Headers.Builder();
        mParams = new HttpUrl.Builder()
                .scheme("http")
                .host("www.com.joey.cheetah.com");
    }

    /**
     * register common params for api
     */
    public NetworkCore registerParams(String key, String params) {
        mParams.addQueryParameter(key, params);
        return this;
    }

    /**
     * register common header for api
     */
    public NetworkCore registerHeader(String key, String header) {
        mHeaders.add(key, header);
        return this;
    }

    /**
     * register an api service for specified request
     *
     * @param id      service id
     * @param baseUrl service base url e.g. https://api.cheetah.com
     * @param clazz   class for specified service
     */
    public <T> T registerService(int id, String baseUrl, Class<T> clazz) {
        return registerService(id, baseUrl, clazz, addInterceptor(createClientBuilder()));
    }

    /**
     * create your own OkhttpClient for request
     */
    public  <T> T registerService(int id, String baseUrl, Class<T> clazz,
                                       OkHttpClient.Builder builder) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        T service = retrofit.create(clazz);
        mServices.append(id, service);
        return service;
    }

    /**
     * get service for request
     *
     * @param id service id
     */
    @SuppressWarnings("unchecked")
    public <T> T service(int id) {
        return (T) mServices.get(id);
    }

    private OkHttpClient.Builder addInterceptor(OkHttpClient.Builder builder) {
        return builder
                .addInterceptor(new ApiCommonParamsInterceptor(mHeaders.build(), mParams.build().query()))
                .addNetworkInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        CLog.d(CLog.LOG_API, message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY));
    }

    public OkHttpClient.Builder createClientBuilder() {
        return new OkHttpClient.Builder();
    }

}
