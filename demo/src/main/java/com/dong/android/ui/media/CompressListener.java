package com.dong.android.ui.media;

/**
 * @author <dr_dong>
 * @time 2017/4/12 10:42
 */
public interface CompressListener {
    void onExecSuccess(String message);

    void onExecFail(String reason);

    void onExecProgress(String message);
}
