package com.joey.cheetah.core.net.coverter;

import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Description:
 * author:Joey
 * date:2018/9/3
 */
final class ApiGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final IApiResponseParser parser;
    private final TypeAdapter<T> adapter;

    ApiGsonResponseBodyConverter(IApiResponseParser parser, TypeAdapter<T> adapter) {
        this.parser = parser;
        this.adapter = adapter;
    }
    @Override
    public T convert(ResponseBody value) throws IOException {
        String data = parser.parse(value.string());
        return  adapter.fromJson(data);
    }
}
