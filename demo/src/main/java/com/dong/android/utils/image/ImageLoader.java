package com.dong.android.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.dong.utils.R;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/27.
 * 描述：
 */

public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();
    private static ImageLoader instance;
    private static Context context;
    private static int[] mColors = {R.color.load_image_color0,
            R.color.load_image_color1, R.color.load_image_color2, R.color.load_image_color3,
            R.color.load_image_color4, R.color.load_image_color5, R.color.load_image_color6,
            R.color.load_image_color7, R.color.load_image_color8, R.color.load_image_color9,
            R.color.load_image_color10, R.color.load_image_color11, R.color.load_image_color12,
            R.color.load_image_color13, R.color.load_image_color14, R.color.load_image_color15
    };

    private ImageLoader(Context context) {
        this.context = context;
    }

    public static ImageLoader getInstance(Context context) {
        if (context == null) {
            Log.e(TAG, "Context is null", new NullPointerException());
            return null;
        }
        if (instance == null) {
            instance = new ImageLoader(context);
        }
        return instance;
    }

    private static Context getContext() {
        if (instance.context == null) {
            Log.e(TAG, "ImageLoader cannot be instantiated", new UnsupportedOperationException());
        }
        return instance.context;
    }

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
            request = Glide.with(getContext()).load(resId);
        } else if (resId == 0 && url != null) {
            request = Glide.with(getContext()).load(url);
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
            request.bitmapTransform(TransformationUtils.bitmapTransform(getContext(), type));
        }
        request.into(iv);
    }

    public static void display(@NonNull int resId, String url, @NonNull ImageView iv,
                               @NonNull int resourceId, @NonNull int width, @NonNull int height,
                               Transformation<Bitmap>[] transformations) {
        DrawableTypeRequest request;
        if (resId != 0 && url == null) {
            request = Glide.with(getContext()).load(resId);
        } else if (resId == 0 && url != null) {
            request = Glide.with(getContext()).load(url);
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
