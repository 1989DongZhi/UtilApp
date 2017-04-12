package com.dong.recycler.manage;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author <dr_dong>
 * @time 2017/4/10 15:48
 */
public class AnimationManage {

    public static final int ALPHAIN = 0x00000001;
    public static final int SCALEIN = 0x00000002;
    public static final int SLIDEIN_BOTTOM = 0x00000003;
    public static final int SLIDEIN_LEFT = 0x00000004;
    public static final int SLIDEIN_RIGHT = 0x00000005;
    private static final float DEFAULT_ALPHA_FROM = 0f;
    private final float mFrom;
    @AnimationType
    private int animationType = ALPHAIN;
    private Animator[] customAnimator;

    public AnimationManage() {
        this(DEFAULT_ALPHA_FROM);
    }

    public AnimationManage(float from) {
        mFrom = from;
    }

    public int getAnimationType() {
        return animationType;
    }

    public void setAnimationType(@AnimationType int animationType) {
        this.animationType = animationType;
    }

    public void setCustomAnimator(Animator[] customAnimator) {
        this.customAnimator = customAnimator;
    }

    public Animator[] getAnimators(View view) {
        if (customAnimator != null) {
            return customAnimator;
        } else {
            return getAnimators(view, getAnimationType());
        }

    }

    public Animator[] getAnimators(View view, @AnimationType int animationType) {
        switch (animationType) {
            case ALPHAIN:
                return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", mFrom, 1f)};
            case SCALEIN:
                return new ObjectAnimator[]{ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f),
                        ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f)};
            case SLIDEIN_BOTTOM:
                return new Animator[]{ObjectAnimator.ofFloat(view, "translationY", view.getMeasuredHeight(), 0)};
            case SLIDEIN_LEFT:
                return new Animator[]{ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0)};
            case SLIDEIN_RIGHT:
                return new Animator[]{ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0)};
            default:
                return new Animator[]{};
        }
    }

    @IntDef({ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }
}
