package com.dong.android.utils.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dong.android.utils.data.FileUtils;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/26.
 * 描述：利用采样率控制bitmap 减少内存消耗
 */

public class BitmapUtils {

    public static Bitmap getFitSampleBitmap(byte[] data, int newWidth, int newHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inSampleSize = getFitInSampleSize(newWidth, newHeight, options);
        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * BitmapFactory.decodeFileDescriptor 比 BitmapFactory.decodeFile 更节省内存
     */
    public static Bitmap getFitSampleBitmap(String filePath, int newWidth, int newHeight)
            throws IOException {
        return getFitSampleBitmap(new FileInputStream(filePath), newWidth, newHeight);
    }

    public static Bitmap getFitSampleBitmap(Resources resources, int resId, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = getFitInSampleSize(width, height, options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    public static Bitmap getFitSampleBitmap(InputStream inputStream, String catchFilePath, int
            width, int height) throws Exception {
        return getFitSampleBitmap(FileUtils.putStreamToFile(catchFilePath, inputStream), width,
                height);
    }

    public static Bitmap getFitSampleBitmap(FileInputStream fileInStream, int width, int height)
            throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        FileDescriptor fd = fileInStream.getFD();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = getFitInSampleSize(width, height, options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    public static int getFitInSampleSize(int reqWidth, int reqHeight, BitmapFactory.Options
            options) {
        int inSampleSize = 1;
        if (options.outWidth > reqWidth || options.outHeight > reqHeight) {
            int widthRatio = Math.round((float) options.outWidth / (float) reqWidth);
            int heightRatio = Math.round((float) options.outHeight / (float) reqHeight);
            inSampleSize = Math.min(widthRatio, heightRatio);
        }
        return inSampleSize;
    }

}
