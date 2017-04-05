package com.dong.android.utils.files;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：<Dr_dong>
 * 日期：2016/11/29.
 * 描述：
 */

public class FileUtils {

    public static final String TAG = FileUtils.class.getSimpleName();
    public static final int CACHE_PATH = 1; // /data/user/0/com.dong.android/cache
    public static final int ROOT_PATH = 2;  // /storage/emulated/0
    public static final int DATA_PATH = 3;  // /storage/emulated/0/Android/data/com.dong.android

    private FileUtils() {
        throw new UnsupportedOperationException("FileUtils cannot be instantiated");
    }

    /**
     * 将InputStream保存为File
     *
     * @param catchFile 缓存路径
     * @param inStream  输入流
     * @return 缓存路径
     * @throws IOException
     */
    public static String putStreamToFile(String catchFile, InputStream inStream) throws IOException {
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

    /**
     * 检查是否存在SD卡
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取指定文件目录
     *
     * @param context
     * @param type
     * @return
     */
    @Nullable
    public static String getDirPath(Context context, @PATH_TYPE int type) {
        String parentDir = null;
        // 如SD卡已存在，则存储；反之存在data目录下
        if (!hasSdcard()) {
            type = CACHE_PATH;
        }
        switch (type) {
            case CACHE_PATH:
                parentDir = context.getCacheDir().getPath();
                break;
            case ROOT_PATH:
                parentDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                break;
            case DATA_PATH:
                parentDir = context.getExternalCacheDir().getParent();
                break;
        }
        return parentDir;
    }

    /**
     * 创建文件夹
     *
     * @param parentPath 创建目标路径
     * @param dirName    文件夹名称
     * @return 创建出的文件夹
     */
    @NonNull
    private static File createDir(String parentPath, String dirName) {
        File parentDir = new File(parentPath);
        if (!parentDir.exists()) {
            boolean isCreate = parentDir.mkdirs();
            Log.i(TAG, parentPath + " has created " + isCreate);
        }
        File dir = new File(parentPath, dirName);
        if (!dir.exists() || dir.isFile()) {
            boolean isCreate = dir.mkdir();
            Log.i(TAG, dirName + " has created " + isCreate);
        }
        return dir;
    }

    /**
     * 创建文件在指定目录下
     *
     * @param parentName 指定路径
     * @param fileName   文件名称
     * @return 创建出的文件
     */
    public static File createFile(String parentName, String fileName) {
        File parentDir = new File(parentName);
        if (!parentDir.exists()) {
            createDir(fileName, parentName);
        }
        File file = new File(parentName, fileName);
        if (!file.exists() || file.isDirectory()) { // 避免文件与文件夹同名
            try {
                boolean newFile = file.createNewFile();
                Log.i(TAG, fileName + " has created " + newFile);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }
        return file;
    }

    /**
     * 创建文件在指定目录下,如果存在覆盖
     *
     * @param parentName 指定路径
     * @param fileName   文件名称
     * @return 创建出的文件
     */
    public static File createNewFile(String parentName, String fileName) {
        File file = new File(parentName, fileName);
        if (file.exists() && file.isFile()) {
            boolean deleteFlag = file.delete();
            Log.i(TAG, fileName + " has delete " + deleteFlag);
        }
        return createFile(parentName, fileName);
    }

    /**
     * 获取指定目录下的文件
     *
     * @param parentName 指定路径
     * @param fileName   文件名称
     * @return 指定文件
     */
    public static File getFile(String parentName, String fileName) {
        File parentDir = new File(parentName);
        if (!parentDir.exists()) {
            Log.i(TAG, parentName + "files does not exist.");
            return null;
        }
        if (fileName == null) {
            return parentDir;
        }
        File file = new File(parentName, fileName);
        if (!file.exists()) {
            Log.i(TAG, fileName + "file does not exist.");
        }
        return file;
    }

    /**
     * 获取指定目录下的全部指定类型文件路径
     *
     * @param findPath 指定路径
     * @param fileType 文件类型，如果为空返回所有文件
     * @return 匹配文件的绝对路径列表
     */
    public static List<String> findFiles(String findPath, String fileType) {
        ArrayList<String> result = new ArrayList<>();
        File findFile = new File(findPath);
        if (!findFile.exists()) {
            Log.i(TAG, findPath + "files does not exist.");
            return result;
        }
        if (findFile.isDirectory()) {
            String[] tempList = findFile.list();
            if (tempList != null && tempList.length != 0) {
                for (String tempPath : tempList) {
                    result.addAll(findFiles(tempPath, fileType));
                }
            }
        } else if (findFile.isFile() && (fileType == null || findFile.getName().endsWith("." + fileType))) {
            result.add(findFile.getAbsolutePath());
        }
        return result;
    }

    /**
     * 删除文件（若为目录，则递归删除子目录和文件）
     *
     * @param deletePath     删除路径
     * @param deleteThisPath true代表删除参数指定file，false代表保留参数指定file
     */
    public static void deleteFile(String deletePath, boolean deleteThisPath) {
        File file = new File(deletePath);
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            String[] subFiles = file.list();
            if (subFiles != null && subFiles.length != 0) {
                // 删除子目录和文件
                for (String subFile : subFiles) {
                    deleteFile(subFile, true);
                }
            }
        }
        if (deleteThisPath) {
            boolean delete = file.delete();
            Log.i(TAG, file + "has delete " + delete);
        }
    }

    /**
     * 获取文件大小，单位为byte（若为目录，则包括所有子目录和文件）
     *
     * @param filePath 路径
     * @return 总大小
     */
    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        long size = 0L;
        if (!file.exists()) {
            return size;
        }
        if (file.isDirectory()) {
            String[] subFiles = file.list();
            if (subFiles != null && subFiles.length != 0) {
                for (String subFile : subFiles) {
                    size += getFileSize(subFile);
                }
            }
        } else {
            size += file.length();
        }
        return size;
    }

    @IntDef({CACHE_PATH, ROOT_PATH, DATA_PATH})
    @Retention(RetentionPolicy.SOURCE)
    private @interface PATH_TYPE {

    }

}
