package com.joey.cheetah.core.net;

import com.joey.cheetah.core.net.interceptor.ApiCommonParamsInterceptor;
import com.joey.cheetah.core.net.interceptor.HttpLoggingInterceptor;
import com.joey.cheetah.core.utils.CLog;

import java.util.HashMap;
import java.util.concurrent.Executor;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description: Builder to create service
 * author:Joey
 * date:2018/9/4
 */
public class NetworkServiceBuilder<T> {
    private Retrofit.Builder builder;
    private Class<T> clazz;
    private int id;
    private OkHttpClient.Builder clientBuilder;
    private Headers headers;
    private HashMap<String, String> params;
    private CallAdapter.Factory callFactory;
    private Converter.Factory converterFactory;

    NetworkServiceBuilder (int id, Class<T> clazz) {
        builder = new Retrofit.Builder();
        this.id = id;
        this.clazz = clazz;
        this.headers = NetworkCore.inst().headers();
        this.params = NetworkCore.inst().params();
    }

    /**
     * base url for this api service
     */
    public NetworkServiceBuilder<T> baseUrl(String url) {
        builder.baseUrl(url);
        return this;
    }

    public NetworkServiceBuilder<T> baseUrl(HttpUrl url) {
        builder.baseUrl(url);
        return this;
    }

    /**
     * OkhttpClient Builder for this api Service
     */
    public NetworkServiceBuilder<T> client(OkHttpClient.Builder clientBuilder) {
        this.clientBuilder = clientBuilder;
        return this;
    }

    /**
     * add CallAdapterFactory
     */
    public NetworkServiceBuilder<T> addCallAdapterFactory(CallAdapter.Factory factory) {
        this.callFactory = factory;
        return this;
    }

    /**
     * add converter factory
     */
    public NetworkServiceBuilder<T> addConverterFactory(Converter.Factory factory) {
        this.converterFactory = factory;
        return this;
    }

    /**
     * add headers for this api ,default use NetworkCore headers, pass null to cancel headers
     */
    public NetworkServiceBuilder<T> headers(Headers headers) {
        this.headers = headers;
        return this;
    }

    /**
     * add common params for this api, default use NetworkCore params, pass null to cancel common params
     */
    public NetworkServiceBuilder<T> params(HashMap<String, String> params) {
        this.params = params;
        return this;
    }

    /**
     * call this method to create service instance and register in NetworkCore
     */
    public T register() {
        if (clientBuilder == null) {
            clientBuilder = new OkHttpClient.Builder();
        }
        clientBuilder.addInterceptor(new ApiCommonParamsInterceptor(headers, params))
                .addNetworkInterceptor(new HttpLoggingInterceptor(message -> CLog.d(CLog.LOG_API, message)).setLevel(HttpLoggingInterceptor.Level.BODY));
        if (callFactory == null) callFactory = RxJava2CallAdapterFactory.create();
        if (converterFactory == null) converterFactory = GsonConverterFactory.create();
        T service = builder.client(clientBuilder.build())
                .addCallAdapterFactory(callFactory)
                .addConverterFactory(converterFactory)
                .build()
                .create(clazz);
        return NetworkCore.inst().register(id, service);
    }

    public NetworkServiceBuilder<T> callbackExecutor(Executor executor) {
        builder.callbackExecutor(executor);
        return this;
    }

    public NetworkServiceBuilder<T> callFactory(okhttp3.Call.Factory factory) {
        builder.callFactory(factory);
        return this;
    }

    public NetworkServiceBuilder<T> validateEagerly(boolean validateEagerly) {
        builder.validateEagerly(validateEagerly);
        return this;
    }


}
