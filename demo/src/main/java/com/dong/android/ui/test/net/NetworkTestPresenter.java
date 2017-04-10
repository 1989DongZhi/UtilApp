package com.dong.android.ui.test.net;

import com.dong.android.R;
import com.dong.android.base.presenter.BasePresenter;
import com.dong.android.net.JsonCallback;
import com.dong.android.net.StringCallback;
import com.dong.utils.UIUtils;
import com.dong.utils.net.NetworkUtils;

import org.json.JSONObject;

/**
 * @author <dr_dong>
 * @time 2017/4/1 16:41
 */
public class NetworkTestPresenter extends BasePresenter<NetworkTestView, NetworkTestModel> {

    public static final String TAG = NetworkTestPresenter.class.getSimpleName();

    @Override
    protected NetworkTestModel getModelImpl() {
        return new NetworkTestModel();
    }

    public void test(String loginfo) {
        if (NetworkUtils.isAvailable()) {
            mModel.test(loginfo, new JsonCallback(this) {
                @Override
                protected void onSuccess(JSONObject body) {
                    if (body.optInt("code") == 0) {
                        mView.updateSuccess();
                    } else {
                        mView.updateFail();
                    }
                }
            });

        } else {
            UIUtils.showToast(R.string.network_unavailable);
            mView.updateFail();
        }
    }

    public void requestLogInfo() {
        if (NetworkUtils.isAvailable()) {
            mModel.request(new StringCallback(this) {
                @Override
                protected void onSuccess(String body) {
                    if (body != null) {
                        mView.requestSuccess(body);
                    } else {
                        mView.requestFail();
                    }
                }
            });

        } else {
            UIUtils.showToast(R.string.network_unavailable);
            mView.requestFail();
        }
    }

}
