package cn.dujc.core.downloader;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.dujc.core.downloader.okhttp.OKDownloadHttpImpl;

/**
 * 下载器
 * Created by du on 2018/3/8.
 */
public class Downloader {

    private static final ConcurrentLinkedQueue<String> DOWNLOAD_QUEUE = new ConcurrentLinkedQueue<String>();
    private final Handler mHandler;

    private IDownloadHttpClient mHttpClient = new OKDownloadHttpImpl();

    private String mUrl;
    private File mDestination;
    private OnDownloadListener mOnDownloadListener;

    public Downloader(String url, File destination) {
        this(url, destination, null);
    }

    public Downloader(String url, File destination, OnDownloadListener listener) {
        mUrl = url;
        mDestination = destination;
        mOnDownloadListener = listener;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public void download() {
        download(false);
    }

    public void download(boolean _continue) {
        if (!canDownload(mUrl, mDestination) || DOWNLOAD_QUEUE.contains(bindUrlAndFile(mUrl, mDestination)))
            return;

        DOWNLOAD_QUEUE.add(bindUrlAndFile(mUrl, mDestination));

        if (mHttpClient != null) {
            mHttpClient.download(mUrl, mDestination, _continue, mHandler, mOnDownloadListener);
        }
    }

    private IDownloadHttpClient createHttpClient() {
        return new OKDownloadHttpImpl();
    }

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        mOnDownloadListener = onDownloadListener;
    }

    public String getUrl() {
        return mUrl;
    }

    public File getDestination() {
        return mDestination;
    }

    public void cancel() {
        if (mHttpClient != null) mHttpClient.cancel();
    }

    public Downloader setDownloadHttpClient(IDownloadHttpClient httpClient) {
        if (httpClient != null) {
            mHttpClient = httpClient;
        }
        return this;
    }

    public static void removeDownloadQueue(String url, File destination) {
        Downloader.DOWNLOAD_QUEUE.remove(Downloader.bindUrlAndFile(url, destination));
    }

    /**
     * 判断是否可以下载
     */
    private static boolean canDownload(String url, File destination) {
        return !TextUtils.isEmpty(url) && destination != null;
    }

    /**
     * 连接url和文件路径，用于判断是否已经添加过任务
     */
    private static String bindUrlAndFile(@NonNull String url, @NonNull File destination) {
        return url + destination.getAbsolutePath();
    }

    public static boolean useSSL(String url) {
        return url != null && url.startsWith("https://");
    }

    public static Downloader createWithNotification(String url, File destination, DownloadNotification downloadNotification) {
        Downloader downloader = new Downloader(url, destination, downloadNotification);
        return downloader;
    }
}
