package com.dong.android.ui.media;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dong.android.R;
import com.dong.android.base.presenter.BasePresenter;
import com.dong.android.base.view.BaseActivity;
import com.dong.android.widget.MyProgressDialog;
import com.dong.utils.UIUtils;
import com.dong.utils.data.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author <dr_dong>
 * @time 2017/4/12 9:55
 */
public class MediaActivity extends BaseActivity {

    public static final String TAG = MediaActivity.class.getSimpleName();
    public static final int PERMISSIONS_REQUEST_CODE = 111;
    public static final int CHOOSE_CODE = 222;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
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
    @BindView(R.id.command0)
    EditText command0;
    @BindView(R.id.command1)
    EditText command1;
    private MyProgressDialog progressDialog;
    private Compressor mCompressor;
    private String cmd0 = "-y" +
            " -i ";
    private String cmd1 = " -threads:1 4" +
//            " -vcodec libx264" +
            " -vcodec mpeg4" +
            " -x264opts bitrate=300:vbv-maxrate=500" +
            " -preset ultrafast" +
            " -codec:a aac" +
            " -ac 1" +
            " -ar 44100" +
            " -b:a 48000" +
            " -b:v 150k" +
            " -r 24" +
            " -crf 28" +
//            " -tbr 30" +
//            " -tbn 90k" +
//            " -tbc 180k" +
//            " -g 25" +
            " -aspect 16:9" +
            " -s ";
    private Double videoLength = 0.00;//视频时长 s
    private long startTime;
    private long endTime;
    private String fileO;
    private String filePath;
    private String fileNameIn;
    private String fileNameOut;
    private String fileCover;

    @OnClick({R.id.btnRun})
    public void onClick(View v) {
        //防止快速点击
        switch (v.getId()) {
            case R.id.btnRun:
                if (TextUtils.isEmpty(fileO)) {
                    UIUtils.showToast("请选择文件");
                    choose();
                } else {
                    execCommand(fileO);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_media;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        command0.setText(cmd0);
        command1.setText(cmd1);
        checkPermission(PERMISSIONS);
        mCompressor = new Compressor(this);
        mCompressor.loadBinary(new InitListener() {
            @Override
            public void onLoadSuccess() {
                Log.d(TAG, "===ffmpeg load library succeed===" + getString(R.string.compress_load_library_succeed));
            }

            @Override
            public void onLoadFail(String reason) {
                Log.d(TAG, "load library fail:" + reason);
                Log.d(TAG, "===ffmpeg load library fail===" + getString(R.string.compress_load_library_failed, reason));
            }
        });
    }

    private void checkPermission(String[] permissions) {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_CODE);
        } else {
            choose();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                UIUtils.showToast("相关权限获取成功！");
                choose();
            } else {
                // Permission Denied
                UIUtils.showToast("未获取相关权限！");
                finish();
            }
        }
    }

    public void choose() {
        Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        it.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
        startActivityForResult(it, CHOOSE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_CODE) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri uri = data.getData();
                String[] proj = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.MIME_TYPE};
                Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    String _data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String mime_type = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
                    if (!TextUtils.isEmpty(mime_type) && mime_type.contains("video") && !TextUtils.isEmpty(_data)) {
                        fileO = _data;
                    } else {
                        UIUtils.showToast("视频获取失败");
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
    }

    private void execCommand(String fileO) {
        Log.d(TAG, "===fileO===" + fileO);
        filePath = fileO.substring(0, fileO.lastIndexOf('/') + 1);
        fileNameIn = fileO.substring(fileO.lastIndexOf('/') + 1);
        fileNameOut = fileO.substring(fileO.lastIndexOf('/') + 1, fileO.lastIndexOf('.')) + "_out" +
                fileO.substring(fileO.lastIndexOf('.'));
        fileCover = fileO.substring(fileO.lastIndexOf('/') + 1, fileO.lastIndexOf('.')) + "_cover.jpeg";

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String path = filePath + fileNameIn;
        mmr.setDataSource(path);
        int rotation = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
        int widthO = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        int heightO = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        videoLength = Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) * 0.001;
        int width = 480;
        int height = 480;
        if (widthO > heightO) {
            if (widthO > width) {
                width = 480;
                height = heightO * width / widthO;
            } else {
                width = widthO;
                height = heightO;
            }
        } else {
            if (heightO > height) {
                height = 480;
                width = widthO * height / heightO;
            } else {
                width = widthO;
                height = heightO;
            }
        }
        String w_h = width + "x" + height;
        if (rotation == 90 || rotation == 270) {
            w_h = height + "x" + width;
        }
        String temp = command0.getText().toString() + filePath + fileNameIn +
                command1.getText().toString() + w_h + " " +
                filePath + fileNameOut;

        Log.e(TAG, "===ffmpeg cmd===" + temp);
//        String[] cmd = temp.split(" ");
        String[] cmd = new String[]{"-h", "full"};

        File mFile = new File(filePath + fileNameOut);
        if (mFile.exists()) {
            mFile.delete();
        }
        startTime = System.currentTimeMillis();
        progressDialog = new MyProgressDialog(this);
        progressDialog.setTextInfo("0%");
        progressDialog.show();
        mCompressor.execCommand(cmd, new CompressListener() {
            @Override
            public void onExecSuccess(String message) {
                endTime = System.currentTimeMillis();
                Log.d(TAG, "===ffmpeg压缩成功===" + message);
                try {
                    FileUtils.saveFile(message, filePath, "log.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String result = getString(R.string.compress_result_input_output,
                        filePath + fileNameIn,
                        getFileSize(filePath + fileNameIn),
                        filePath + fileNameOut,
                        getFileSize(filePath + fileNameOut));
                result = result + "---" + new DecimalFormat("#0.000").format((endTime - startTime) / 1000.0) + "s";
                Log.e(TAG, "===ffmpeg out===" + result);
                progressDialog.dismiss();
                new AlertDialog.Builder(MediaActivity.this)
                        .setTitle(getString(R.string.compress_succeed))
                        .setMessage(result)
                        .setPositiveButton(getString(R.string.open_video), (dialog, which) -> {
                            openFile(new File(filePath + fileNameOut));
                            dialog.dismiss();
                        })
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            }

            @Override
            public void onExecFail(String reason) {
                Log.e(TAG, "===ffmpeg===" + getString(R.string.compress_failed, reason));
                progressDialog.dismiss();
                UIUtils.showToast("压缩失败");
            }

            @Override
            public void onExecProgress(String message) {
                Log.d(TAG, getString(R.string.compress_progress, getProgress(message)));
                progressDialog.setTextInfo(getString(R.string.compress_progress, getProgress(message)));
            }
        });
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
        Pattern p = Pattern.compile("00:\\d{2}:\\d{2}");
        Matcher m = p.matcher(source);
        if (m.find()) {
            //00:00:00
            String result = m.group(0);
            String temp[] = result.split(":");
            Double seconds = Double.parseDouble(temp[1]) * 60 + Double.parseDouble(temp[2]);
            Log.d(TAG, "current second = " + seconds);
            if (0 != videoLength) {
                return new DecimalFormat("#0.0").format(seconds / videoLength * 100) + "%";
            }
            return "0";
        }
        return "";
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

}
