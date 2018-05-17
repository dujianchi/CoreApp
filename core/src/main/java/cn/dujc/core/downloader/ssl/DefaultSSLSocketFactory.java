package cn.dujc.core.downloader.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author du
 * date 2018/5/17 下午12:43
 */
public class DefaultSSLSocketFactory {

    private final X509TrustManager trustManager;
    private SSLContext sslContext;
    private SSLSocketFactory sslSocketFactory;

    private DefaultSSLSocketFactory() {
        trustManager = new DefaultTrustManager();
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static DefaultSSLSocketFactory create() {
        return new DefaultSSLSocketFactory();
    }

    public X509TrustManager getTrustManager() {
        return trustManager;
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }
}
