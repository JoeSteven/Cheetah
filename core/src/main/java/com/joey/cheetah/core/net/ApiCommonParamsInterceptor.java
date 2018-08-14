package com.joey.cheetah.core.net;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description:
 * author:Joey
 * date:2018/7/26
 */
public class ApiCommonParamsInterceptor implements Interceptor {
    private Headers mHeaders;
    private String mCommonParams;

    public ApiCommonParamsInterceptor(Headers headers, String query) {
        this.mHeaders = headers;
        this.mCommonParams = query;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();


        // 添加新的参数
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
                .query(createQuery(oldRequest.url().query()));

        // 新的请求
        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .headers(mHeaders)
                .build();
        return chain.proceed(newRequest);
    }

    private String createQuery(String query) {
        if (TextUtils.isEmpty(query)) return mCommonParams;
        return mCommonParams + "&" + query;
    }


}
