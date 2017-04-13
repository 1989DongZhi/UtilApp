package com.dong.android.ui.media;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.dong.android.R;
import com.dong.android.databinding.ActivityMediaBinding;
import com.dong.utils.GadgetUtils;
import com.dong.utils.log.LogUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <dr_dong>
 * @time 2017/4/12 9:55
 */
public class MediaActivity extends AppCompatActivity {
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_SUCCEED = 2;//视频录制成功
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_FAILED = 3;//视频录制出错
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_CANCEL = 4;//取消录制
    public static final String INTENT_EXTRA_VIDEO_PATH = "intent_extra_video_path";//录制的视频路径
    //相机权限,录制音频权限,读写sd卡的权限,都为必须,缺一不可
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE_FOR_PERMISSIONS = 0;//
    private static final int REQUEST_CODE_FOR_RECORD_VIDEO = 1;//录制视频请求码
    private static final Handler handler = new Handler();
    private final String TAG = getClass().getSimpleName();
    private final String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };
    ActivityMediaBinding mBinding;
    private Compressor mCompressor;
    private String currentInputVideoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/aaa.mp4";
    private String currentOutputVideoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/aaa_out007.mp4";
    //    private String currentOutputVideoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/media/0_out2.mp4";
    String cmd = "-y" +
            " -threads:1 4" +
            " -re" +
            " -i " + currentInputVideoPath +
//            " -tune zerolatency" +
            " -strict -2" +
            " -vcodec libx264" +
            " -acodec aac" +
            " -preset ultrafast" +
            " -ac 1" +
            " -crf 28" +
            " -ar 44100" +
            " -b:a 64k" +
            " -s 480x270" +
            " -aspect 16:9" +
            " " + currentOutputVideoPath;
    private Double videoLength = 0.00;//视频时长 s
    private long startTime;
    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_media);

        mBinding.btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CameraActivity.startActivityForResult(MediaActivity.this, REQUEST_CODE_FOR_RECORD_VIDEO);
            }
        });

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(currentInputVideoPath);
        for (int i = 0; i < 26; i++) {
            LogUtils.e("===" + i + "===" + mmr.extractMetadata(i));
        }
        mBinding.etCommand.setText(cmd);
        mBinding.btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String command = mBinding.etCommand.getText().toString();
                if (TextUtils.isEmpty(command)) {
                    Toast.makeText(MediaActivity.this, getString(R.string.compree_please_input_command)
                            , Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(currentInputVideoPath)) {
                    Toast.makeText(MediaActivity.this, R.string.no_video_tips, Toast.LENGTH_SHORT).show();
                } else {
                    File file = new File(currentOutputVideoPath);
                    if (file.exists()) {
                        file.delete();
                    }
                    execCommand(command);
                }
            }
        });
        mCompressor = new Compressor(this);
        mCompressor.loadBinary(new InitListener() {
            @Override
            public void onLoadSuccess() {
                Log.v(TAG, "load library succeed");
                textAppend(getString(R.string.compress_load_library_succeed));
            }

            @Override
            public void onLoadFail(String reason) {
                Log.i(TAG, "load library fail:" + reason);
                textAppend(getString(R.string.compress_load_library_failed, reason));
            }
        });


        PermissionsChecker mChecker = new PermissionsChecker(getApplicationContext());
        if (mChecker.lacksPermissions(PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE_FOR_PERMISSIONS, PERMISSIONS);
        }

    }

    private void execCommand(String cmd) {
        File mFile = new File(currentOutputVideoPath);
        if (mFile.exists()) {
            mFile.delete();
        }
        startTime = System.currentTimeMillis();
        mCompressor.execCommand(cmd, new CompressListener() {
            @Override
            public void onExecSuccess(String message) {
                endTime = System.currentTimeMillis();
                Log.i(TAG, "success " + message);
                textAppend(getString(R.string.compress_succeed));
                Toast.makeText(getApplicationContext(), R.string.compress_succeed, Toast.LENGTH_SHORT).show();
                String result = getString(R.string.compress_result_input_output, currentInputVideoPath
                        , getFileSize(currentInputVideoPath), currentOutputVideoPath, getFileSize(currentOutputVideoPath));
                result = result + GadgetUtils.getTime2Second(endTime - startTime);
                textAppend(result);
                new AlertDialog.Builder(MediaActivity.this)
                        .setTitle(getString(R.string.compress_succeed))
                        .setMessage(result)
                        .setPositiveButton(getString(R.string.open_video), (dialog, which) -> {
                            openFile(new File(currentOutputVideoPath));
                            dialog.dismiss();
                        })
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();

            }

            @Override
            public void onExecFail(String reason) {
                Log.i(TAG, "fail " + reason);
                textAppend(getString(R.string.compress_failed, reason));
                new AlertDialog.Builder(MediaActivity.this)
                        .setTitle(getString(R.string.compress_failed))
                        .setMessage(getString(R.string.compress_failed))
                        .setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }

            @Override
            public void onExecProgress(String message) {
                Log.i(TAG, "progress " + message);
                textAppend(getString(R.string.compress_progress, message));
                Log.v(TAG, getString(R.string.compress_progress, getProgress(message)));


            }
        });
    }

    private void textAppend(String text) {
        if (!TextUtils.isEmpty(text)) {
            mBinding.tvLog.append(text + "\n");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mBinding.scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_PERMISSIONS) {
            //权限申请
            if (PermissionsActivity.PERMISSIONS_DENIED == resultCode) {
                //权限未被授予，退出应用
                finish();
            } else if (PermissionsActivity.PERMISSIONS_GRANTED == resultCode) {
                //权限被授予
                //do nothing
            }
        } else if (requestCode == REQUEST_CODE_FOR_RECORD_VIDEO) {
            //录制视频
            if (resultCode == RESULT_CODE_FOR_RECORD_VIDEO_SUCCEED) {
                //录制成功
                String videoPath = data.getStringExtra(INTENT_EXTRA_VIDEO_PATH);
                if (!TextUtils.isEmpty(videoPath)) {
                    currentInputVideoPath = videoPath;
                    MediaMetadataRetriever retr = new MediaMetadataRetriever();
                    retr.setDataSource(currentInputVideoPath);
                    String time = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);//获取视频时长
                    //7680
                    try {
                        videoLength = Double.parseDouble(time) / 1000.00;
                    } catch (Exception e) {
                        e.printStackTrace();
                        videoLength = 0.00;
                    }
                    Log.v(TAG, "videoLength = " + videoLength + "s");
                    refreshCurrentPath();
                }
            } else if (resultCode == RESULT_CODE_FOR_RECORD_VIDEO_FAILED) {
                //录制失败
                currentInputVideoPath = "";
            }
        }

    }

    private void refreshCurrentPath() {
        mBinding.tvVideoFilePath.setText(getString(R.string.path, currentInputVideoPath, getFileSize(currentInputVideoPath)));
        cmd = "-y -i " + currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
                "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 480x320 -aspect 16:9 " + currentOutputVideoPath;
        mBinding.etCommand.setText(cmd);
        mBinding.tvLog.setText("");
    }

    private void openFile(File file) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //获取文件file的MIME类型
            String type = getMIMEType(file);
            //设置intent的data和Type属性。
            intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
            //跳转
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(MediaActivity.this, R.string.dont_have_app_to_open_file, Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileSize(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return "0 MB";
        } else {
            long size = f.length();
            return (size / 1024f) / 1024f + "MB";
        }
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private String getProgress(String source) {
        //progress frame=   28 fps=0.0 q=24.0 size= 107kB time=00:00:00.91 bitrate= 956.4kbits/s
        Pattern p = Pattern.compile("00:\\d{2}:\\d{2}");
        Matcher m = p.matcher(source);
        if (m.find()) {
            //00:00:00
            String result = m.group(0);
            String temp[] = result.split(":");
            Double seconds = Double.parseDouble(temp[1]) * 60 + Double.parseDouble(temp[2]);
            Log.v(TAG, "current second = " + seconds);
            if (0 != videoLength) {
                return seconds / videoLength + "";
            }
            return "0";
        }
        return "";
    }

}