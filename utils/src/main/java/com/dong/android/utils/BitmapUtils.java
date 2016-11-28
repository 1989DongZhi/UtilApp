package com.dong.android.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/26.
 * 描述：
 */

public class BitmapUtils {

    /**
     * 对图片进行二次采样，生成缩略图。放置加载过大图片出现内存溢出
     */
    public static Bitmap createThumbnail(byte[] data, int newWidth,
                                         int newHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        int oldWidth = options.outWidth;
        int oldHeight = options.outHeight;

        int ratioWidth = 0;
        int ratioHeight = 0;

        if (newWidth != 0 && newHeight == 0) {
            ratioWidth = oldWidth / newWidth;
            options.inSampleSize = ratioWidth;
        } else if (newWidth == 0 && newHeight != 0) {
            ratioHeight = oldHeight / newHeight;
            options.inSampleSize = ratioHeight;
        }
        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory
                .decodeByteArray(data, 0, data.length, options);
        return bm;
    }

    /**
     * 对图片进行二次采样，生成缩略图。放置加载过大图片出现内存溢出
     */
    private Bitmap createThumbnail(String filePath, int newWidth, int newHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int originalWidth = options.outWidth;
        int originalHeight = options.outHeight;

        int ratioWidth = originalWidth / newWidth;
        int ratioHeight = originalHeight / newHeight;

        options.inSampleSize = ratioHeight > ratioWidth ? ratioHeight
                : ratioWidth;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
}
