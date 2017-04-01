package com.dong.android.utils.log;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author <dr_dong>
 * @time 2017/3/29 12:36
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = CrashHandler.class.getSimpleName();
    public static String PACKAGE_VERSION_NAME;
    public static int PACKAGE_VERSION_CODE;
    private static CrashHandler instance;// CrashHandler实例
    public String packageName;
    private String crashPath;
    private Thread.UncaughtExceptionHandler mDefaultHandler;// 系统默认的UncaughtException处理类
    private Context mContext;// 程序的Context对象
    private Map<String, String> info = new HashMap<String, String>();// 用来存储设备信息和异常信息

    private CrashHandler(Context context) {
        mContext = context;
        init();
    }

    public static CrashHandler getInstance(Context context) {
        if (context == null) {
            Log.e(TAG, "Context is null", new NullPointerException());
            return null;
        }
        if (instance == null) {
            instance = new CrashHandler(context);
        }
        return instance;
    }

    /**
     * 初始化
     */
    public void init() {
        if (mContext == null) {
            return;
        }
        packageName = mContext.getPackageName();
        try {
            PackageInfo packageInfo = mContext.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            PACKAGE_VERSION_CODE = packageInfo.versionCode;
            PACKAGE_VERSION_NAME = packageInfo.versionName == null ? "null" : packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            PACKAGE_VERSION_CODE = -1;
            PACKAGE_VERSION_NAME = "null";
        }
        crashPath = Environment.getExternalStorageDirectory() + "/Android/data/" + packageName + "/crash";
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果自定义的没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);// 如果处理了，让程序继续运行3秒再退出，保证文件保存并上传到服务器
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex 异常信息
     * @return true 如果处理了该异常信息;否则返回false.
     */
    public boolean handleException(Throwable ex) {
        if (ex == null || mContext == null)
            return false;
        final String crashReport = getCrashReport(ex);
        LogUtils.i(TAG, crashReport);
        new Thread() {
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出", Toast.LENGTH_LONG).show();
                // 收集设备参数信息
                collectDeviceInfo();
                // 保存日志文件
                saveCrashInfo2File(ex);
//                File file = save2File(crashReport);
//                sendAppCrashReport(mContext, crashReport, file);
                Looper.loop();
            }
        }.start();
        return true;
    }

    /**
     * 收集设备参数信息
     */
    public void collectDeviceInfo() {
        info.put("versionName", PACKAGE_VERSION_NAME);
        info.put("versionCode", String.format("%d", PACKAGE_VERSION_CODE));
        info.put("time", getCurrentTime());

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                info.put(field.getName(), field.get("").toString());
                LogUtils.d(TAG, field.getName() + ":" + field.get(""));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取APP崩溃异常报告
     *
     * @param ex
     * @return
     */
    private String getCrashReport(Throwable ex) {
        StringBuffer exceptionStr = new StringBuffer();
        exceptionStr.append("Version: ").append(PACKAGE_VERSION_NAME)
                .append("(").append(PACKAGE_VERSION_CODE).append(")\n")
                .append("Android: ").append(Build.VERSION.RELEASE).append("(").append(Build.MODEL).append(")\n")
                .append("Exception: ").append(ex.getMessage()).append("\n");
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            exceptionStr.append(elements[i].toString()).append("\n");
        }
        return exceptionStr.toString();
    }

    /**
     * 保存异常到文件中
     */
    private String saveCrashInfo2File(Throwable ex) {
        info.put("exception", ex.toString());
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : info.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + System.lineSeparator());
        }
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        // 循环着把所有的异常信息写入writer中
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();// 记得关闭
        String result = writer.toString();
        sb.append(result);

        // 保存文件
        String fileName = "crash-" + getCurrentTime() + ".log";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            FileOutputStream fos = null;
            try {
                File dir = new File(crashPath);
                Log.i("CrashHandler", dir.toString());
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(crashPath, fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file, true);
                fos.write(sb.toString().getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                LogUtils.e(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e(e.getMessage());
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private File save2File(String crashReport) {
        String fileName = "crash-" + System.currentTimeMillis() + ".txt";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + File.separator + "crash");
                if (!dir.exists())
                    dir.mkdir();
                File file = new File(dir, fileName);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(crashReport.toString().getBytes());
                fos.close();
                return file;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void sendAppCrashReport(final Context context, final String crashReport, final File file) {
        AlertDialog mDialog = new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("程序出错啦")
                .setMessage("请把错误报告以邮件的形式提交给我们，谢谢！")
                .setPositiveButton(android.R.string.ok,
                        (dialog, which) -> {
                            try {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(Intent.EXTRA_EMAIL, "1060221762@qq.com");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "UtilsApp 错误报告");
                                if (file != null) {
                                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                                    intent.putExtra(Intent.EXTRA_TEXT, "请将此错误报告发送给我，以便我尽快修复此问题，谢谢合作!" + System.lineSeparator());
                                } else {
                                    intent.putExtra(Intent.EXTRA_TEXT, "请将此错误报告发送给我，以便我尽快修复此问题，谢谢合作！" + System.lineSeparator() + crashReport);
                                }
                                intent.setType("text/plain");
                                intent.setType("message/rfc882");
                                Intent.createChooser(intent, "Choose Email Client");
                                context.startActivity(intent);
                            } catch (Exception e) {
                                Toast.makeText(mContext, "There are no email clients installed.", Toast.LENGTH_LONG).show();
                            } finally {
                                dialog.dismiss();
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        (dialog, which) -> {
                            dialog.dismiss();
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        })
                .create();
        mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mDialog.show();
    }

    private String getCurrentTime() {
        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault());
        String time = sdf.format(date);
        return time;
    }

}