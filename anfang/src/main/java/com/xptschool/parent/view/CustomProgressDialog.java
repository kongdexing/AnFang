package com.xptschool.parent.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.widget.roundcornerprogressbar.RoundCornerProgressBar;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;

import java.io.File;
import java.text.NumberFormat;

/**
 * Created by Administrator on 2018/05/08.
 */
public class CustomProgressDialog {

    private Context mContext;
    private AlertDialog alertDialog;
    private String TAG = "DownloadFile";
    RoundCornerProgressBar downloadBar;

    public CustomProgressDialog(Context context) {
        mContext = context;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(XPTApplication.getInstance().getWindowWidth() * 4 / 5,
                XPTApplication.getInstance().getWindowHeight() / 3);
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.popup_dialog_progress);

        downloadBar = (RoundCornerProgressBar) window.findViewById(R.id.downloadBar);
        downloadBar.setMax(100);
        downloadBar.setProgress(0);
    }

    public void downloadFile(String netFile) {
        Log.i(TAG, "downloadFile: " + netFile);
        FileDownloader.getImpl().create(netFile)
                .setListener(createListener())
                .setTag(netFile)
                .start();
    }

    public FileDownloadListener createListener() {
        return new FileDownloadListener() {

            @Override
            protected boolean isInvalid() {
                return super.isInvalid();
            }

            @Override
            protected void pending(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
//                updateDisplay(String.format("[pending] id[%d] %d/%d", task.getId(), soFarBytes, totalBytes));
            }

            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                super.connected(task, etag, isContinue, soFarBytes, totalBytes);
//                updateDisplay(String.format("[connected] id[%d] %s %B %d/%d", task.getId(), etag, isContinue, soFarBytes, totalBytes));
            }

            @Override
            protected void progress(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
                downloadBar.setMax(totalBytes);
                downloadBar.setProgress(soFarBytes);
                Log.i(TAG, "progress: " + String.format("[progress] id[%d] %d/%d", task.getId(), soFarBytes, totalBytes));
            }

            @Override
            protected void blockComplete(final BaseDownloadTask task) {
            }

            @Override
            protected void retry(BaseDownloadTask task, Throwable ex, int retryingTimes, int soFarBytes) {
                super.retry(task, ex, retryingTimes, soFarBytes);
//                updateDisplay(String.format("[retry] id[%d] %s %d %d",
//                        task.getId(), ex, retryingTimes, soFarBytes));
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                Log.i(TAG, "completed: " + task.getPath() + " " + task.getTargetFilePath());
                File file = new File(task.getPath());
                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri =
                        FileProvider.getUriForFile(mContext, "com.xptschool.parent.fileProvider", file);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                // 由于没有在Activity环境下启动Activity,设置下面的标签
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                mContext.startActivity(intent);

                //apk文件的本地路径
//                File apkfile = new File(task.getPath());
//                //会根据用户的数据类型打开android系统相应的Activity。
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                //设置intent的数据类型是应用程序application
//                intent.setDataAndType(Uri.parse("file:/" + apkfile.toString()), "application/vnd.android.package-archive");
//                //为这个新apk开启一个新的activity栈
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                //开始安装
//                mContext.startActivity(intent);
                //关闭旧版本的应用程序的进程
                android.os.Process.killProcess(android.os.Process.myPid());
            }

            @Override
            protected void paused(final BaseDownloadTask task, final int soFarBytes, final int totalBytes) {
//                updateDisplay(String.format("[paused] id[%d] %d/%d", task.getId(), soFarBytes, totalBytes));
//                updateDisplay(String.format("############################## %d", (Integer) task.getTag()));
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
//                updateDisplay(Html.fromHtml(String.format("[error] id[%d] %s %s",
//                        task.getId(),
//                        e,
//                        FileDownloadUtils.getStack(e.getStackTrace(), false))));
//
//                updateDisplay(String.format("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! %d", (Integer) task.getTag()));
            }

            @Override
            protected void warn(BaseDownloadTask task) {
//                updateDisplay(String.format("[warn] id[%d]", task.getId()));
//                updateDisplay(String.format("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ %d", (Integer) task.getTag()));
            }
        };
    }

    public void dismiss() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }


}
