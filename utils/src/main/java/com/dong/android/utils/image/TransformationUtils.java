package com.dong.android.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.IntDef;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.dong.android.R;
import com.dong.android.utils.AppUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.MaskTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.InvertFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.KuwaharaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SwirlFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/27.
 * 描述：
 */

public abstract class TransformationUtils {

    public static final int Original = 0;
    public static final int Mask = 1;
    public static final int NinePatchMask = 2;
    public static final int CropTop = 3;
    public static final int CropCenter = 4;
    public static final int CropBottom = 5;
    public static final int CropSquare = 6;
    public static final int CropCircle = 7;
    public static final int ColorFilter = 8;
    public static final int Grayscale = 9;
    public static final int RoundedCorners = 10;
    public static final int Blur = 11;
    public static final int Toon = 12;
    public static final int Sepia = 13;
    public static final int Contrast = 14;
    public static final int Invert = 15;
    public static final int Pixel = 16;
    public static final int Sketch = 17;
    public static final int Swirl = 18;
    public static final int Brightness = 19;
    public static final int Kuawahara = 20;
    public static final int Vignette = 21;

    @TRANSFORMATE_TYPE
    public static int getType(int type) {
        return type;
    }

    public static Transformation<Bitmap>[] transform(Transformation<Bitmap>... transformations) {
        return transformations;
    }

    public static Transformation<Bitmap>[] bitmapTransform(@TRANSFORMATE_TYPE Integer type) {
        Context mContext = AppUtils.getAppContext();
        switch (type) {
            case Original:
                return null;
            case Mask:
                return transform(new CenterCrop(mContext),
                        new MaskTransformation(mContext, R.drawable.mask_starfish));
            case NinePatchMask:
                return transform(new CenterCrop(mContext),
                        new MaskTransformation(mContext, R.drawable.mask_chat_right));
            case CropTop:
                return transform(new CropTransformation(mContext, 300, 100, CropTransformation
                        .CropType.TOP));
            case CropCenter:
                return transform(new CropTransformation(mContext, 300, 100, CropTransformation
                        .CropType.CENTER));
            case CropBottom:
                return transform(
                        new CropTransformation(mContext, 300, 100, CropTransformation.CropType
                                .BOTTOM));
            case CropSquare:
                return transform(new CropSquareTransformation(mContext));
            case CropCircle:
                return transform(new CropCircleTransformation(mContext));
            case ColorFilter:
                return transform(new ColorFilterTransformation(mContext, Color.argb(80,
                        255, 0, 0)));
            case Grayscale:
                return transform(new GrayscaleTransformation(mContext));
            case RoundedCorners:
                return transform(new RoundedCornersTransformation(mContext, 30, 0,
                        RoundedCornersTransformation.CornerType.BOTTOM));
            case Blur:
                return transform(
                        new BlurTransformation(mContext, 25));
            case Toon:
                return transform(
                        new ToonFilterTransformation(mContext));
            case Sepia:
                return transform(
                        new SepiaFilterTransformation(mContext));
            case Contrast:
                return transform(
                        new ContrastFilterTransformation(mContext, 2.0f));
            case Invert:
                return transform(
                        new InvertFilterTransformation(mContext));
            case Pixel:
                return transform(
                        new PixelationFilterTransformation(mContext, 20));
            case Sketch:
                return transform(
                        new SketchFilterTransformation(mContext));
            case Swirl:
                return transform(
                        new SwirlFilterTransformation(mContext, 0.5f, 1.0f,
                                new PointF(0.5f, 0.5f)));
            case Brightness:
                return transform(
                        new BrightnessFilterTransformation(mContext, 0.5f));
            case Kuawahara:
                return transform(
                        new KuwaharaFilterTransformation(mContext, 25));
            case Vignette:
                return transform(
                        new VignetteFilterTransformation(mContext,
                                new PointF(0.5f, 0.5f),
                                new float[]{0.0f, 0.0f, 0.0f}, 0f, 0.75f));
            default:
                break;
        }
        return null;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Original, Mask, NinePatchMask, CropTop, CropCenter, CropBottom, CropSquare, CropCircle,
            ColorFilter, Grayscale, RoundedCorners, Blur, Toon, Sepia, Contrast, Invert, Pixel,
            Sketch, Swirl, Brightness, Kuawahara, Vignette})
    public @interface TRANSFORMATE_TYPE {
    }

}
