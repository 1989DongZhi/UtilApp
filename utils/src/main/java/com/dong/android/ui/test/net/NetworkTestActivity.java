package com.dong.android.ui.test.net;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dong.android.R;
import com.dong.android.base.view.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author <dr_dong>
 * @time 2017/4/1 16:40
 */
public class NetworkTestActivity extends BaseActivity<NetworkTestPresenter> implements NetworkTestView {

    public static final String TAG = NetworkTestActivity.class.getSimpleName();
    @BindView(R.id.send_btn)
    Button sendBtn;
    @BindView(R.id.log_edit)
    EditText logEdit;
    @BindView(R.id.send_result_txt)
    TextView sendResultTxt;
    @BindView(R.id.log_info_txt)
    TextView logInfoTxt;

    @OnClick({R.id.send_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_btn:
                sendLogTxt();
                break;
            default:
                break;
        }

    }

    @Override
    protected int getRootView() {
        return R.layout.activity_network_test;
    }

    @Override
    protected NetworkTestPresenter createPresenter() {
        return new NetworkTestPresenter();
    }

    @Override
    protected void setListener() {


    }

    @Override
    protected void initData(Bundle savedInstanceState) {


    }

    @Override
    public void updateSuccess() {
        sendResultTxt.setText("成功");

    }

    @Override
    public void updateFail() {
        sendResultTxt.setText("失败");
    }

    private void sendLogTxt() {
        mPresenter.test(logEdit.getText().toString());
    }

}
