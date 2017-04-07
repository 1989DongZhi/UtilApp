package com.dong.android.net;

import com.dong.android.base.presenter.BasePresenter;
import com.dong.utils.UIUtils;
import com.dong.utils.log.LogUtils;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author <dr_dong>
 * @time 2017/4/1 16:52
 */
public abstract class JsonCallback implements Callback<JSONObject> {

    private BasePresenter presenter;

    public JsonCallback(BasePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
        //View非Attached状态不做操作
        if (null != presenter && !presenter.isViewAttached()) return;
        if (response != null && response.code() / 100 == 2 && response.body() != null) {
            onSuccess(response.body());
        } else {
            if (response != null) {
                switch (response.code()) {
                    case 400:
                    case 401:
                    case 404:
                    case 408:
                        break;

                    case 500:
                        UIUtils.showToast("服务器异常,请稍后重试");
                        break;

                    default:
                        break;
                }
                LogUtils.e("请求失败：" + response.code());
            }
            onFail(response);
        }
        onFinish();
    }

    @Override
    public void onFailure(Call<JSONObject> call, Throwable t) {
        if (null != presenter && !presenter.isViewAttached()) return;
        onFail(null);
        onFinish();
    }

    /**
     * 网络请求成功
     *
     * @param body
     */
    protected abstract void onSuccess(JSONObject body);

    /**
     * 网络请求失败
     */
    protected void onFail(Response<JSONObject> response) {

    }

    /**
     * 请求结束
     */
    protected void onFinish() {

    }

}
