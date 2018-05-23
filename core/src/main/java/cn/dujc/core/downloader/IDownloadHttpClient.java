package cn.dujc.core.downloader;

import android.os.Handler;

import java.io.File;

/**
 * @author du
 * date 2018/5/17 下午12:03
 */
public interface IDownloadHttpClient {

    void download(String url, File destination, boolean _continue, Handler mainThreadHandler, OnDownloadListener listener);

    void cancel();
}
