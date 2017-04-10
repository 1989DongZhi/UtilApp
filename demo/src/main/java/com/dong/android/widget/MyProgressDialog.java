package com.dong.android.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dong.android.R;
import com.dong.utils.UIUtils;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/29.
 * 描述：
 */

public class MyProgressDialog extends Dialog {
    private TextView info;
    private Context context;

    public MyProgressDialog(Context context) {
        super(context, R.style.ProgressDia);
        this.context = context;
        initView();
    }

    private void initView() {
        setCanceledOnTouchOutside(false);
        View view = View.inflate(context, R.layout.dialog_progress, null);
        this.info = (TextView) view.findViewById(R.id.progress_info);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dip2px(100),
                UIUtils.dip2px(100));
        this.setContentView(view, params);
    }

    public void setTextInfo(String info) {
        this.info.setText(info);
    }
}
