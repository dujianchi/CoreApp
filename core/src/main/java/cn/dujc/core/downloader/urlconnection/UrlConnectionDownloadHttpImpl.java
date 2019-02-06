package cn.dujc.core.downloader.urlconnection;

import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import cn.dujc.core.downloader.Downloader;
import cn.dujc.core.downloader.IDownloadHttpClient;
import cn.dujc.core.downloader.OnDownloadListener;
import cn.dujc.core.downloader.ssl.DefaultSSLSocketFactory;

/**
 * @author du
 * date 2018/5/17 下午12:06
 */
public class UrlConnectionDownloadHttpImpl implements IDownloadHttpClient {

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2);

    private Future<Object> future = null;

    @Override
    public void download(final String url, final File destination, final boolean _continue, final Handler mainThreadHandler, final OnDownloadListener listener) {
        future = EXECUTOR.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                final HttpURLConnection downloader = createByUrl(url);
                if (downloader != null) {
                    if (destination.exists()) {
                        if (_continue) {
                            downloader.setRequestProperty("RANGE", "bytes=" + destination.length() + "-");
                        } else {
                            destination.deleteOnExit();
                        }
                    } else if (!destination.getParentFile().exists() || !destination.getParentFile().isDirectory()) {
                        destination.getParentFile().deleteOnExit();
                        destination.getParentFile().mkdirs();
                    }
                    downloader.setDoInput(true);
                    //downloader.setDoOutput(true);
                    downloader.setUseCaches(false);
                    try {
                        downloader.setRequestMethod("GET");
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                    try {
                        downloader.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        final InputStream inputStream = downloader.getInputStream();
                        final int contentLength = downloader.getContentLength();
                        if (!_continue || contentLength > 0)
                            save(inputStream, destination, contentLength, listener, mainThreadHandler);
                        if (listener != null) {
                            mainThreadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onDownloadSuccess(destination);
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        onError(e.getLocalizedMessage(), listener, mainThreadHandler);
                    }
                    downloader.disconnect();
                }
                Downloader.removeDownloadQueue(url, destination);
                return null;
            }
        });
    }

    @Override
    public void cancel() {
        if (future != null) {
            try {
                future.cancel(true);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private static HttpURLConnection createByUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URLConnection connection = url.openConnection();
            if (Downloader.useSSL(urlStr)) {
                final SSLSocketFactory socketFactory = DefaultSSLSocketFactory.create().getSslSocketFactory();
                if (socketFactory != null) {
                    final HttpsURLConnection httpsURLConnection = (HttpsURLConnection) connection;
                    httpsURLConnection.setSSLSocketFactory(socketFactory);
                    return httpsURLConnection;
                }
            } else {
                return (HttpURLConnection) connection;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void onError(final String message, final OnDownloadListener listener, Handler mainThreadHandler) {
        if (listener != null) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onDownloadFailure(message);
                }
            });
        }
    }

    private static void save(InputStream inputStream, File destination, final long max, final OnDownloadListener listener, Handler handler) {
        FileOutputStream outputStream = null;
        byte[] buffer = new byte[1024];
        try {
            outputStream = new FileOutputStream(destination);
            int length, downloaded = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
                if (listener != null) {
                    listener.onThreadUpdateProgress(downloaded += length, max);
                    final int downloadedF = downloaded;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onUpdateProgress(downloadedF, max);
                        }
                    });
                }
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
