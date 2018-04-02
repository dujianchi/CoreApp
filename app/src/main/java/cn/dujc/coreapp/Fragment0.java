package cn.dujc.coreapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import cn.dujc.core.downloader.DownloadNotification;
import cn.dujc.core.downloader.Downloader;
import cn.dujc.core.downloader.OnDownloadListener;
import cn.dujc.core.ui.BaseFragment;

/**
 * Created by du on 2018/2/14.
 */
public class Fragment0 extends BaseFragment {

    private TextView mTextView;

    @Override
    public int getViewId() {
        return 0;
    }

    @Override
    public View getViewV() {
        mTextView = new TextView(mActivity);
        mTextView.setText("0");
        return mTextView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        final Downloader downloader = new Downloader("http://cdn1.miaomubao.com/version/miaoke/miaoke-phone-debug-180307.apk"
                , new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "download.apk"));
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadNotification(mActivity, R.mipmap.ic_launcher) {
                    @Override
                    public void onDownloadSuccess(File saved) {
                        install(mActivity, saved);
                    }
                }.startDownload(downloader, false);
            }
        });
    }

    private static final String APK_MIME_TYPE = "application/vnd.android.package-archive";
    private void install(Context context, File apk){
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//不会安装一半跳掉
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "apk.fileProvider", apk);
            install.setDataAndType(contentUri, APK_MIME_TYPE);
        } else {
            install.setDataAndType(Uri.fromFile(apk), APK_MIME_TYPE);
        }
        try {
            context.startActivity(install);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
