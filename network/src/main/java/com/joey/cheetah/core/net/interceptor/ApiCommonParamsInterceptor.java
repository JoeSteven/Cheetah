package com.joey.cheetah.core.net.interceptor;

import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    private HashMap<String, String> mCommonParams;
    private String commonQuery;

    public ApiCommonParamsInterceptor(Headers headers, HashMap<String, String> query) {
        this.mHeaders = headers;
        this.mCommonParams = query;
        if (mHeaders == null) mHeaders = new Headers.Builder().build();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();


        // 添加新的参数
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host());
        String query = createQuery(oldRequest.url().query());
        if (!TextUtils.isEmpty(query)) {
            authorizedUrlBuilder.query(createQuery(oldRequest.url().query()));
        }

        // 新的请求
        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .headers(mHeaders)
                .build();
        return chain.proceed(newRequest);
    }

    private String createQuery(String query) {
        if (mCommonParams == null || mCommonParams.isEmpty()) return query;
        if (TextUtils.isEmpty(commonQuery)) {
            StringBuilder builder = new StringBuilder();
            int i = 0;
            for (Map.Entry<String, String> entry : mCommonParams.entrySet()) {
                if (TextUtils.isEmpty(entry.getKey()) || TextUtils.isEmpty(entry.getValue())) {
                    continue;
                }
                if (i != 0) {
                    builder.append("&");
                }
                builder.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue());
                i++;
            }
            commonQuery = builder.toString();
        }
        if (TextUtils.isEmpty(query)) {
            return commonQuery;
        } else {
            return commonQuery + "&" + query;
        }
    }
}
