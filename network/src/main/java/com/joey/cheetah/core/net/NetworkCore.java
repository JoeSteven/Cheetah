package com.joey.cheetah.core.net;

import android.util.SparseArray;

import java.util.HashMap;

import okhttp3.Headers;

/**
 * Description: create different services for API
 * author:Joey
 * date:2018/7/26
 */
public class NetworkCore {
    private static NetworkCore sNetworkCore;
    private SparseArray<Object> mServices;
    private HashMap<String, String> mParams;
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
        mParams = new HashMap<>();
    }

    /**
     * register common params for api
     */
    public NetworkCore registerParams(String key, String params) {
        mParams.put(key, params);
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
     * @return register headers
     */
    public Headers headers() {
        return mHeaders.build();
    }

    /**
     * @return register common params
     */
    public HashMap<String, String> params() {
        return mParams;
    }

    /**
     * start create an api service
     * @param id id for this api service
     * @param clazz clazz to create service
     * @return NetworkServiceBuilder
     */
    public <T> NetworkServiceBuilder<T> createService(int id, Class<T> clazz) {
        return new NetworkServiceBuilder<>(id, clazz);
    }

    /**
     * register service
     * @param id id for this api service
     * @return service instantce
     */
    public <T> T register(int id, T service) {
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
}
