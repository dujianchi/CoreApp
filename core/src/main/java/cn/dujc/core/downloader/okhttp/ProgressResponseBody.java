package cn.dujc.core.downloader.okhttp;

import android.os.Handler;

import java.io.IOException;

import cn.dujc.core.downloader.OnDownloadListener;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 带进度的ResponseBody
 * Created by du on 2018/3/8.
 */
public class ProgressResponseBody extends ResponseBody {

    private final Handler mHandler;
    private final ResponseBody mResponseBody;
    private final OnDownloadListener mOnDownloadListener;
    private BufferedSource mBufferedSource;

    public ProgressResponseBody(Handler handler, ResponseBody responseBody, OnDownloadListener onDownloadListener) {
        mHandler = handler;
        mResponseBody = responseBody;
        mOnDownloadListener = onDownloadListener;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long mDownloadedBytes = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                if (mOnDownloadListener != null) {
                    mDownloadedBytes += bytesRead != -1 ? bytesRead : 0;
                    final long contentLength = contentLength();
                    mOnDownloadListener.onThreadUpdateProgress(mDownloadedBytes, contentLength);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mOnDownloadListener.onUpdateProgress(mDownloadedBytes, contentLength);
                        }
                    });
                }
                return bytesRead;
            }
        };
    }
}
