package com.dong.android.net;

import android.support.annotation.NonNull;

import com.dong.android.BuildConfig;
import com.dong.android.app.AppManager;
import com.dong.android.utils.data.IOUtils;
import com.dong.android.utils.manager.AsyncTaskUtils;
import com.dong.android.utils.net.CacheControlInterceptor;
import com.dong.android.utils.net.JsonConverterFactory;
import com.dong.android.utils.net.StringConverterFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;

import static android.net.sip.SipErrorCode.TIME_OUT;

/**
 * @author <dr_dong>
 * @time 2017/4/1 10:48
 * 描述: 获取网络请求Retrofit
 */
public class RequestManager {

    /**
     * 返回为String格式
     */
    public static final int CALL_TYPE_STRING = 2;
    private static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    /**
     * OkHttp的Client对象
     * 主要Http请求配置信息
     */
    private static OkHttpClient okHttpClient = getHttpClient();

    /**
     * 通用服务器
     */
    private static Retrofit COMMON_SERVER = createCommonServer();

    /**
     * 微信服务器
     *
     * @return
     */
    private static Retrofit WX_SERVER = createWxServer();

    private static Retrofit createCommonServer() {
        return createRetrofit(ServerApi.SERVER);
    }

    private static Retrofit createWxServer() {
        return createRetrofit(ServerApi.WX_SERVER);
    }

    private static Retrofit createRetrofit(String baseurl) {
        return new Retrofit.Builder().baseUrl(baseurl).client(okHttpClient)
                .addConverterFactory(new JsonConverterFactory()).build();
    }

    private static Retrofit createRetrofit(String baseurl, int callType) {
        Converter.Factory factory;
        switch (callType) {
            case CALL_TYPE_STRING:
                factory = new StringConverterFactory();
                break;

            default:
                factory = new JsonConverterFactory();
                break;
        }

        return new Retrofit.Builder().baseUrl(baseurl).client(okHttpClient)
                .addConverterFactory(factory).build();
    }

    /**
     * 创建常用服务器指定的api接口的实现
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T createCommon(Class<T> clazz) {
        return COMMON_SERVER.create(clazz);
    }

    /**
     * 创建常用服务器指定的api接口的实现
     *
     * @param clazz
     * @param callType
     * @param <T>
     * @return
     */
    public static <T> T createCommon(Class<T> clazz, int callType) {
        return createRetrofit(ServerApi.SERVER, callType).create(clazz);
    }

    /**
     * 创建微信服务器指定的api接口的实现
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T createWeixin(Class<T> clazz) {
        return WX_SERVER.create(clazz);
    }

    /**
     * 设置网络配置参数
     *
     * @return
     */
    private static OkHttpClient getHttpClient() {
        CacheControlInterceptor cacheControlInterceptor = new CacheControlInterceptor();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .cache(new Cache(new File(AppManager.getAppContext().getCacheDir(), "net_cache"), HTTP_RESPONSE_DISK_CACHE_MAX_SIZE))
                .addInterceptor(new CustomInterceptor())
                .addInterceptor(cacheControlInterceptor)
                .addNetworkInterceptor(cacheControlInterceptor);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor bodyInterceptor = new HttpLoggingInterceptor();
            bodyInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            return builder.addInterceptor(bodyInterceptor).build();
        }

        return builder.build();
    }

    /**
     * 文件下载
     *
     * @param url
     * @param filePath
     * @param fileName
     */
    public static void downloadFile(String url, final String filePath, final String fileName, final FileDownloadCallback callback) {
        final Request request = new Request.Builder().get().url(url).build();
        new AsyncTaskUtils<File>() {
            @Override
            protected File runInBackground() {
                Response response = null;
                File file = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (null != response)
                    file = saveFile(response, filePath, fileName);
                return file;
            }

            @Override
            protected void onPostExecute(File file) {
                if (null != file) {
                    callback.downloadSucceed(file);
                } else {
                    callback.downloadFail();
                }
            }
        }.exec();
    }

    /**
     * 保存文件
     *
     * @param response
     * @param filePath
     * @param fileName
     * @return
     * @throws IOException
     */
    private static File saveFile(@NonNull Response response, @NonNull String filePath, @NonNull String fileName) {
        InputStream is;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(response.body());
            IOUtils.closeQuietly(fos);
        }
        return null;
    }

    /**
     * 下载结果回调
     */
    public interface FileDownloadCallback {

        void downloadSucceed(File file);

        void downloadFail();
    }
}
