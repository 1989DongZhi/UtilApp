package com.dong.android.ui.test.net;

import com.dong.android.base.model.BaseModel;
import com.dong.android.net.JsonCallback;
import com.dong.android.net.RequestManager;
import com.dong.android.net.StringCallback;

import static com.dong.android.net.RequestManager.CALL_TYPE_STRING;

/**
 * @author <dr_dong>
 * @time 2017/4/1 16:45
 */
public class NetworkTestModel extends BaseModel<NetworkTestEngine> {

    @Override
    protected NetworkTestEngine createEngine() {
        return RequestManager.createCommon(NetworkTestEngine.class);
    }

    public void test(String loginfo, JsonCallback callback) {
        RequestManager.createCommon(NetworkTestEngine.class).testRequest(loginfo).enqueue(callback);
    }

    public void request(StringCallback callback) {
        RequestManager.createCommon(NetworkTestEngine.class, CALL_TYPE_STRING).requestLogInfo()
                .enqueue(callback);
    }

}
