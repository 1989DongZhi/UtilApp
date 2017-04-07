package com.dong.android.base.presenter;

import com.dong.android.base.model.BaseModel;
import com.dong.android.base.view.BaseView;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/29.
 * 描述：
 */

public abstract class BasePresenter<V extends BaseView, M extends BaseModel> implements Presenter {

    protected V mView;
    protected M mModel;
    private boolean isViewAttached;

    public BasePresenter() {
        mModel = getModelImpl();
    }

    /**
     * 创建对应的model
     *
     * @return
     */
    protected abstract M getModelImpl();

    @Override
    public void attachView(BaseView view) {
        mView = (V) view;
        isViewAttached = true;
    }

    @Override
    public void detachView() {
        isViewAttached = false;
        mView = null;
        mModel = null;
    }

    public boolean isViewAttached() {
        return isViewAttached;
    }
}