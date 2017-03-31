package com.dong.android.base.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.dong.android.base.presenter.BasePresenter;
import com.dong.android.ui.main.MainActivity;
import com.dong.android.utils.ActivitiesManager;
import com.dong.android.utils.UIUtils;
import com.dong.android.utils.permission.PermissionUtils;
import com.dong.android.widget.MyProgressDialog;

import butterknife.ButterKnife;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/29.
 * 描述：
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements
        BaseView {

    protected Context mContext;
    protected P mPresenter;
    private MyProgressDialog loadingDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        preLoad();
        ActivitiesManager.addActivity(this);
        if (getRootView() != 0) {
            setContentView(getRootView());
            ButterKnife.bind(this);
        }
        mPresenter = createPresenter();
        if (null != mPresenter) {
            mPresenter.attachView(this);
        }
        initData(savedInstanceState);
        setListener();
    }

    @Override
    protected void onDestroy() {
        if (loadingDia != null)
            loadingDia.dismiss();
        loadingDia = null;
        if (null != mPresenter) {
            mPresenter.detachView();
            mPresenter = null;
        }
        super.onDestroy();
        ActivitiesManager.removeActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionUtils.onRequestPermissionsResult(requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 预加载，setContentView之前执行
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
     * 初始化数据
     * 根据数据设置view内容
     */
    protected abstract void initData(Bundle savedInstanceState);


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
            loadingDia = new MyProgressDialog(this);
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
        if (this.isFinishing())
            return;
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
     * 跳转到其他Activity并且finish当前Activity
     */
    protected void goActivityAndFinish(final Class<?> act) {
        Intent intent = new Intent(this, act);
        startActivity(intent);
        finish();
    }

    /**
     * 跳转到其他Activity但不finish当前的Activity
     */
    protected void goActivity(final Class<?> act) {
        Intent intent = new Intent(this, act);
        startActivity(intent);
    }

    /**
     * 跳转到其他Activity但不finish当前的Activity
     */
    protected void goActivity(final Intent intent) {
        startActivity(intent);
    }

    /**
     * 如果应用未启动则启动应用
     */
    protected void startApp() {
        if (!ActivitiesManager.isActivityExist(MainActivity.class)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

}
