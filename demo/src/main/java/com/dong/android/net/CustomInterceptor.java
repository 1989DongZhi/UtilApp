package com.dong.android.net;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.http.Body;

/**
 * @author <dr_dong>
 * @time 2017/4/1 11:27
 */
public class CustomInterceptor implements Interceptor {

    public static final String TAG = CustomInterceptor.class.getSimpleName();

    private static final String HTTP_GET_METHOD = "GET";
    private static final String HTTP_POST_METHOD = "POST";
    public static String LOGON;
    public static String LOGON_OUT;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        /** 添加SessionId**/
        String sessionId = CookieUtils.getSessionIdFromPref();
        if (!TextUtils.isEmpty(sessionId)) {
            builder.addHeader("Cookie", "JSESSIONID=" + sessionId);
        }
        builder.addHeader("user-agent", "YiQiXiu Android 2.3.0");

        /** 添加参数 **/
        request = addParameters(builder);

        /**获取SessionId **/
        Response response = chain.proceed(request);
        responseInterceptor(response);
        return response;
    }

    /**
     * 添加参数
     *
     * @param builder
     * @return
     */
    public Request addParameters(Request.Builder builder) {
        Request request = builder.build();
        String method = builder.build().method();
        if (HTTP_GET_METHOD.equals(method)) {
            HttpUrl httpUrl = request.url();
            String queryParams = httpUrl.query();
            String newUri;
            if (TextUtils.isEmpty(queryParams)) {
                newUri = httpUrl.uri().toString();
            } else {
                newUri = httpUrl.uri().toString();
            }
            return builder.url(newUri).build();
        }

        if (HTTP_POST_METHOD.equals(method)) {
            RequestBody body = request.body();
            FormBody.Builder newFormBody = new FormBody.Builder();

            /** 1.没有POST参数 **/
            try {
                if (body.contentLength() == 0) {
                    builder.method(method, newFormBody.build());
                    return builder.build();
                }
            } catch (IOException e) {
                return builder.build();
            }

            /** 2.表单提交的参数 **/
            if (body instanceof FormBody) {
                FormBody oldFormBody = (FormBody) request.body();
                for (int i = 0; i < oldFormBody.size(); i++) {
                    newFormBody.addEncoded(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
                }
                builder.method(method, newFormBody.build());
                return builder.build();
            }

            /** 3.Body方式提交的参数 **/
            if (body instanceof Body) {
                return builder.build();
            }
        }
        return builder.build();
    }

    /**
     * 登录、注册、三方登录返回时获取sessionID
     *
     * @param response
     */
    public void responseInterceptor(Response response) {
        try {
            String requestUrl = response.request().url().toString();
            if (requestUrl.startsWith(LOGON)) {
                List<String> list = response.headers("Set-Cookie");
                if (list == null) return;
                for (String str : list) {
                    if (str.contains("JSESSIONID=")) {
                        String[] cookieParams = str.split(";");
                        if (cookieParams == null) break;
                        for (String param : cookieParams) {
                            param = param.trim();
                            String sessionId;
                            String domain;
                            if (param.startsWith("JSESSIONID")) {
                                sessionId = param.split("=")[1];
                                CookieUtils.addSessionIdToPref(sessionId);
                            }
                            if (param.startsWith("Domain")) {
                                domain = param.split("=")[1];
                                CookieUtils.addDomainToPref(domain);
                            }
                        }
                    }
                }
            }

            if (requestUrl.startsWith(LOGON_OUT)) {
                CookieUtils.addSessionIdToPref("");
                CookieUtils.addDomainToPref("");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
