package com.dong.android.utils.net;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author <dr_dong>
 * @time 2017/4/1 11:08
 */
public class StringConverterFactory extends Converter.Factory {

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new StringResponseConverter();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        return new StringRequestConverter();
    }

    public static class StringResponseConverter implements Converter<ResponseBody, String> {

        @Override
        public String convert(ResponseBody value) throws IOException {
            return value.string();
        }
    }

    public static class StringRequestConverter implements Converter<String, RequestBody> {
        @Override
        public RequestBody convert(String value) throws IOException {
            return RequestBody.create(MediaType.parse("application/octet-stream"), value);
        }
    }
}