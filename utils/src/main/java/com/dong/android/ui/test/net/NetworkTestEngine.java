package com.dong.android.ui.test.net;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author <dr_dong>
 * @time 2017/4/1 16:46
 */
public interface NetworkTestEngine {

    @POST("log.php")
    Call<JSONObject> testRequest(@Query("loginfo") String loginfo);
}
