package cn.dujc.core.downloader;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 下载器
 * Created by lucky on 2018/3/8.
 */
public class Downloader {

    private static final ConcurrentLinkedQueue<String> DOWNLOAD_QUEUE = new ConcurrentLinkedQueue<String>();
    private Handler mHandler;
    private OkHttpClient mHttpClient;

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

    public void download(boolean _continue) {
        if (!canDownload(mUrl, mDestination)|| DOWNLOAD_QUEUE.contains(bindUrlAndFile(mUrl, mDestination))) return;

        DOWNLOAD_QUEUE.add(bindUrlAndFile(mUrl, mDestination));

        mHttpClient = createByUrl(mUrl, mOnDownloadListener);

        final Request.Builder request = new Request.Builder()
                .url(mUrl);

        if (mDestination.exists()) {
            if (_continue) {
                request.header("RANGE", "bytes=" + mDestination.length() + "-");
            } else {
                mDestination.deleteOnExit();
            }
        } else if (!mDestination.getParentFile().exists() || !mDestination.getParentFile().isDirectory()) {
            mDestination.getParentFile().deleteOnExit();
            mDestination.getParentFile().mkdirs();
        }

        mHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                DOWNLOAD_QUEUE.remove(bindUrlAndFile(mUrl, mDestination));
                onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                DOWNLOAD_QUEUE.remove(bindUrlAndFile(mUrl, mDestination));
                if (response.isSuccessful()) {
                    save(response);
                    if (mOnDownloadListener != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mOnDownloadListener.onDownloadSuccess(mDestination);
                            }
                        });
                    }
                } else {
                    final ResponseBody body = response.body();
                    onError(body != null ? body.string() : "unknown error");
                }
            }
        });
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

    private void save(Response response) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        byte[] buffer = new byte[1024];
        try {
            inputStream = response.body().byteStream();
            outputStream = new FileOutputStream(mDestination);
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
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

    /**
     * 判断是否可以下载
     */
    private boolean canDownload(String url, File destination) {
        return !TextUtils.isEmpty(url) && destination != null;
    }

    /**
     * 连接url和文件路径，用于判断是否已经添加过任务
     */
    private String bindUrlAndFile(@NotNull String url, @NotNull File destination) {
        return url + destination.getAbsolutePath();
    }

    private void onError(final String message) {
        if (mOnDownloadListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mOnDownloadListener.onDownloadFailure(message);
                }
            });
        }
    }

    private OkHttpClient createByUrl(String url, final OnDownloadListener listener) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (url != null && url.startsWith("https://")) {
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{trustManager}, null);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            if (sslContext != null) {
                builder.sslSocketFactory(sslContext.getSocketFactory(), trustManager);
            }
        }
        final Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(mHandler, originalResponse.body(), listener))
                        .build();
            }
        };
        return builder
                .addInterceptor(interceptor)
                .build();
    }
}
