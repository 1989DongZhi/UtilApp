package com.dong.android.utils.net;

import org.json.JSONException;
import org.json.JSONObject;

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
 * @time 2017/4/1 11:07
 */
public class JsonConverterFactory extends Converter.Factory {

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
//        if (!(type instanceof Class<?>)) {
//            return null;
//        }
//        for( Annotation annotation :annotations) {
//            if( annotation instanceof TypeString) {
//                return new StringResponseConverter();
//            }
//        }
//
//        return null;
        return new JsonResponseConverter();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
//        if (!(type instanceof Class<?>)) {
//            return null;
//        }
//        for( Annotation annotation :parameterAnnotations) {
//            if( annotation instanceof TypeString) {
//                return new StringRequestConverter();
//            }
//        }
//        return null;
        return new JsonRequestConverter();
    }

    public static class JsonResponseConverter implements Converter<ResponseBody, JSONObject> {

        @Override
        public JSONObject convert(ResponseBody value) throws IOException {
            try {
                return new JSONObject(value.string());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class JsonRequestConverter implements Converter<JSONObject, RequestBody> {
        @Override
        public RequestBody convert(JSONObject value) throws IOException {
            return RequestBody.create(MediaType.parse("application/octet-stream"), value.toString());
        }
    }
}
