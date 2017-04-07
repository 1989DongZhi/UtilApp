package com.dong.android.net;

import com.dong.android.base.presenter.BasePresenter;
import com.dong.utils.UIUtils;
import com.dong.utils.log.LogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：<Dr_dong>
 * 日期：2017/4/4.
 * 描述：
 */
public abstract class StringCallback implements Callback<String> {

    private BasePresenter presenter;

    public StringCallback(BasePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
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
    public void onFailure(Call<String> call, Throwable t) {
        if (null != presenter && !presenter.isViewAttached()) return;
        onFail(null);
        onFinish();
    }

    /**
     * 网络请求成功
     *
     * @param body
     */
    protected abstract void onSuccess(String body);

    /**
     * 网络请求失败
     */
    protected void onFail(Response<String> response) {

    }

    /**
     * 请求结束
     */
    protected void onFinish() {

    }

}