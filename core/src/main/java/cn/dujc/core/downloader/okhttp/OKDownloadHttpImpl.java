package cn.dujc.core.downloader.okhttp;

import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.dujc.core.downloader.Downloader;
import cn.dujc.core.downloader.IDownloadHttpClient;
import cn.dujc.core.downloader.OnDownloadListener;
import cn.dujc.core.downloader.ssl.DefaultSSLSocketFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author du
 * date 2018/5/17 下午12:05
 */
public class OKDownloadHttpImpl implements IDownloadHttpClient {

    private Call call = null;

    @Override
    public void download(final String url, final File destination, boolean _continue, final Handler mainThreadHandler, final OnDownloadListener listener) {
        OkHttpClient httpClient = createOkByUrl(url, mainThreadHandler, listener);

        final Request.Builder request = new Request.Builder()
                .url(url);

        if (destination.exists()) {
            if (_continue) {
                request.header("RANGE", "bytes=" + destination.length() + "-");
            } else {
                destination.deleteOnExit();
            }
        } else if (!destination.getParentFile().exists() || !destination.getParentFile().isDirectory()) {
            destination.getParentFile().deleteOnExit();
            destination.getParentFile().mkdirs();
        }

        call = httpClient.newCall(request.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Downloader.removeDownloadQueue(url, destination);
                onError(e.getMessage(), listener, mainThreadHandler);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Downloader.removeDownloadQueue(url, destination);
                if (response.isSuccessful()) {
                    save(response, destination);
                    if (listener != null) {
                        mainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onDownloadSuccess(destination);
                            }
                        });
                    }
                } else {
                    final ResponseBody body = response.body();
                    onError(body != null ? body.string() : "unknown error", listener, mainThreadHandler);
                }
            }
        });
    }

    @Override
    public void cancel() {
        if (call != null) {
            try {
                call.cancel();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private static OkHttpClient createOkByUrl(String url, final Handler handler, final OnDownloadListener listener) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (Downloader.useSSL(url)) {
            DefaultSSLSocketFactory factory = DefaultSSLSocketFactory.create();
            if (factory.getSslSocketFactory() != null) {
                builder.sslSocketFactory(factory.getSslSocketFactory(), factory.getTrustManager());
            }
        }
        final Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(handler, originalResponse.body(), listener))
                        .build();
            }
        };
        return builder
                .addInterceptor(interceptor)
                .build();
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

    private static void save(Response response, File destination) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        byte[] buffer = new byte[1024];
        try {
            inputStream = response.body().byteStream();
            outputStream = new FileOutputStream(destination);
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
}
