package com.dong.android.ui.test;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.dong.android.R;
import com.dong.android.base.presenter.BasePresenter;
import com.dong.android.base.view.BaseActivity;

import butterknife.BindView;

/**
 * 作者：<Dr_dong>
 * 日期：2016/12/11.
 * 描述：
 */
public class SVGTintActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {
    public static final String TAG = SVGTintActivity.class.getSimpleName();

    @BindView(R.id.svg_tint_image)
    ImageView svgTintImage;
    @BindView(R.id.seek_bar_alpha)
    SeekBar seekBarAlpha;
    @BindView(R.id.seek_bar_red)
    SeekBar seekBarRed;
    @BindView(R.id.seek_bar_green)
    SeekBar seekBarGreen;
    @BindView(R.id.seek_bar_blue)
    SeekBar seekBarBlue;
    private Drawable drawable;

    @Override
    protected int getRootView() {
        return R.layout.activity_svg_tint;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void setListener() {
        svgTintImage = (ImageView) findViewById(R.id.svg_tint_image);
        drawable = svgTintImage.getDrawable();
        drawable.mutate();

        seekBarAlpha.setMax(255);
        seekBarAlpha.setOnSeekBarChangeListener(this);
        seekBarAlpha.setProgress(255);

        seekBarRed.setMax(255);
        seekBarRed.setOnSeekBarChangeListener(this);
        seekBarRed.setProgress(0);

        seekBarGreen.setMax(255);
        seekBarGreen.setOnSeekBarChangeListener(this);
        seekBarGreen.setProgress(0);

        seekBarBlue.setMax(255);
        seekBarBlue.setOnSeekBarChangeListener(this);
        seekBarBlue.setProgress(0);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        drawable.setTint(Color.argb(seekBarAlpha.getProgress(),
                seekBarRed.getProgress(),
                seekBarGreen.getProgress(),
                seekBarBlue.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
