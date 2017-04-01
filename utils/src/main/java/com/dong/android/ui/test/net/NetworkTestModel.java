package com.dong.android.ui.test.net;

import com.dong.android.base.model.BaseModel;
import com.dong.android.net.JsonCallback;
import com.dong.android.net.RequestManager;

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
        mEngine.testRequest(loginfo).enqueue(callback);
    }

}
