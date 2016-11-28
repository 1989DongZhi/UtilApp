package com.dong.android.utils.image;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.dong.android.R;
import com.dong.android.utils.AppUtils;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/27.
 * 描述：
 */

public class ImageLoader {

    private static int[] mColors = {R.color.load_image_color0,
            R.color.load_image_color1, R.color.load_image_color2, R.color.load_image_color3,
            R.color.load_image_color4, R.color.load_image_color5, R.color.load_image_color6,
            R.color.load_image_color7, R.color.load_image_color8, R.color.load_image_color9,
            R.color.load_image_color10, R.color.load_image_color11, R.color.load_image_color12,
            R.color.load_image_color13, R.color.load_image_color14, R.color.load_image_color15
    };

    /**
     * 获取随机颜色资源
     */
    public static int getRandomRes() {
        int resIndex = (int) (Math.random() * (mColors.length));
        return mColors[resIndex];
    }

    public static void display(@NonNull int resId, String url, @NonNull ImageView iv,
                               @NonNull int resourceId, @NonNull int width, @NonNull int height,
                               @TransformationUtils.TRANSFORMATE_TYPE int type) {
        DrawableTypeRequest request;
        if (resId != 0 && url == null) {
            request = Glide.with(AppUtils.getAppContext()).load(resId);
        } else if (resId == 0 && url != null) {
            request = Glide.with(AppUtils.getAppContext()).load(url);
        } else {
            return;
        }
        if (resourceId != 0) {
            request.placeholder(resourceId).error(resourceId);
        }
        if (width != 0 && height != 0) {
            request.override(width, height);
        }
        if (type != TransformationUtils.Original) {
            request.bitmapTransform(TransformationUtils.bitmapTransform(type));
        }
        request.into(iv);
    }

    public static void display(@NonNull int resId, String url, @NonNull ImageView iv,
                               @NonNull int resourceId, @NonNull int width, @NonNull int height,
                               Transformation<Bitmap>[] transformations) {
        DrawableTypeRequest request;
        if (resId != 0 && url == null) {
            request = Glide.with(AppUtils.getAppContext()).load(resId);
        } else if (resId == 0 && url != null) {
            request = Glide.with(AppUtils.getAppContext()).load(url);
        } else {
            return;
        }
        if (resourceId != 0) {
            request.placeholder(resourceId).error(resourceId);
        }
        if (width != 0 && height != 0) {
            request.override(width, height);
        }
        if (transformations.length > 0) {
            request.bitmapTransform(transformations);
        }
        request.into(iv);
    }

    public static void display(String url, ImageView iv) {
        display(0, url, iv, getRandomRes(), 0, 0, TransformationUtils.Original);
    }

    public static void display(int resId, ImageView iv) {
        display(resId, null, iv, getRandomRes(), 0, 0, TransformationUtils.Original);
    }

    public static void display(String url, ImageView iv, int width, int height) {
        display(0, url, iv, getRandomRes(), width, height, TransformationUtils.Original);
    }

    public static void display(int resId, ImageView iv, int width, int height) {
        display(resId, null, iv, getRandomRes(), width, height, TransformationUtils.Original);
    }

    public static void display(String url, ImageView iv,
                               @TransformationUtils.TRANSFORMATE_TYPE int type) {
        display(0, url, iv, getRandomRes(), 0, 0, type);
    }

    public static void display(int resId, ImageView iv,
                               @TransformationUtils.TRANSFORMATE_TYPE Integer type) {
        display(resId, null, iv, getRandomRes(), 0, 0, type);
    }

    public static void display(String url, ImageView iv, int width, int height,
                               @TransformationUtils.TRANSFORMATE_TYPE Integer type) {
        display(0, url, iv, getRandomRes(), width, height, type);
    }

    public static void display(int resId, ImageView iv, int width, int height,
                               @TransformationUtils.TRANSFORMATE_TYPE Integer type) {
        display(resId, null, iv, getRandomRes(), width, height, type);
    }

    public interface LoaderListener {
        void onSuccess(Bitmap bitmap);
    }
}
