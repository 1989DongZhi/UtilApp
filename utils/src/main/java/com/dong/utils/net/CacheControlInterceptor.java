package com.dong.utils.net;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author <dr_dong>
 * @time 2017/4/1 11:10
 * 描述:    缓存处理的拦截器
 */
public class CacheControlInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        if (!NetworkUtils.isAvailable() && "GET".equals(request
                .method())) {
//            Logger.d("cacheControl = %s", CacheControl.FORCE_CACHE);
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response originalResponse = chain.proceed(request);
        if (NetworkUtils.isAvailable()) {
            String cacheControl = request.cacheControl().toString().trim();
            if (TextUtils.isEmpty(cacheControl)) {

                //GET请求缓存5秒
                cacheControl = "max-age=5";
            } else {
                //读接口上的@Headers里的配置
                cacheControl = request.cacheControl().toString();
            }
//            Logger.d("cacheControl = %s", cacheControl);
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, " + cacheControl)
                    .build();
        } else {
            return originalResponse.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                    .removeHeader("Pragma")
                    .build();
        }
    }
}
