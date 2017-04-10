package com.dong.android.base.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dong.android.base.presenter.BasePresenter;
import com.dong.android.widget.MyProgressDialog;
import com.dong.utils.UIUtils;

import butterknife.ButterKnife;

/**
 * @author <dr_dong>
 * @time 2017/3/27 15:03
 */
public abstract class BaseDialogFragment<P extends BasePresenter> extends DialogFragment implements BaseView {

    protected BaseActivity mActivity;
    protected P mPresenter;
    private MyProgressDialog loadingDia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        preLoad();
        return inflater.inflate(getRootView(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mPresenter = createPresenter();
        if (null != mPresenter) {
            mPresenter.attachView(this);
        }
        initData();
        setListener();
    }

    @Override
    public void onDestroyView() {
        if (loadingDia != null)
            loadingDia.dismiss();
        loadingDia = null;
        if (null != mPresenter) {
            mPresenter.detachView();
            mPresenter = null;
        }
        super.onDestroyView();
    }


    /**
     * 显示加载进度对话框
     */
    @Override
    public void showLoading() {
        showLoading("加载中…");
    }

    /**
     * 显示加载进度对话框
     */
    public void showLoading(String info) {
        if (loadingDia == null) {
            loadingDia = new MyProgressDialog(mActivity);
        }
        loadingDia.setTextInfo(info);
        if (!loadingDia.isShowing()) {
            loadingDia.show();
        }
    }

    /**
     * 隐藏加载进度对话框
     */

    @Override
    public void dismissLoading() {
        if (mActivity.isFinishing()) {
            return;
        }
        if (loadingDia != null && loadingDia.isShowing())
            loadingDia.dismiss();
    }

    @Override
    public void showError(String msg) {
        UIUtils.showToast(msg);
    }

    @Override
    public void showNetError() {
        UIUtils.showToast("网络链接异常，请稍后重试");
    }

    /**
     * 预加载
     */
    protected void preLoad() {

    }

    /**
     * 获取布局id
     */
    protected abstract int getRootView();

    /**
     * 创建View对应的Presenter
     */
    protected abstract P createPresenter();

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 设置数据
     */
    protected abstract void initData();

    /**
     * 跳转到其他Activity
     */
    protected void goActivity(Class<?> activity) {
        Intent intent = new Intent(mActivity, activity);
        mActivity.startActivity(intent);
    }
}