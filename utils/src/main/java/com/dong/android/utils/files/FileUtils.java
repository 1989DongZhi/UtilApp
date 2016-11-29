package com.dong.android.utils.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/29.
 * 描述：
 */

public class FileUtils {

    /**
     * 将InputStream保存为File
     *
     * @param catchFile 缓存路径
     * @param inStream  输入流
     * @return 缓存路径
     * @throws IOException
     */
    public static String putStreamToFile(String catchFile, InputStream inStream) throws
            IOException {
        File tempFile = new File(catchFile);
        if (tempFile.exists()) {
            tempFile.delete();
        }
        tempFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
        }
        inStream.close();
        fileOutputStream.close();
        return catchFile;
    }

}
