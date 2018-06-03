package cn.dujc.core.downloader;

import java.io.File;

/**
 * 下载监听
 * Created by du on 2018/3/8.
 */
public interface OnDownloadListener {

    void onDownloadFailure(String message);

    void onUpdateProgress(long downloaded, long total);

    void onDownloadSuccess(File saved);

    void onThreadUpdateProgress(long downloaded, long total);

    public abstract class OnDownloadListenerImpl implements OnDownloadListener {
        @Override
        public void onThreadUpdateProgress(long downloaded, long total) {}
    }
}
