package com.joey.cheetah.core.net.coverter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Description:
 * author:Joey
 * date:2018/9/3
 */
public class ApiGsonConverterFactory extends  Converter.Factory  {
    public static ApiGsonConverterFactory create(IApiResponseParser parser) {
        return create(new Gson(), parser);
    }

    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static ApiGsonConverterFactory create(Gson gson, IApiResponseParser parser) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new ApiGsonConverterFactory(gson, parser);
    }

    private final Gson gson;
    private final IApiResponseParser parser;

    private ApiGsonConverterFactory(Gson gson, IApiResponseParser parser) {
        this.gson = gson;
        this.parser = parser;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new ApiGsonResponseBodyConverter<>(parser, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new ApiGsonRequestBodyConverter<>(gson, adapter);
    }
}
