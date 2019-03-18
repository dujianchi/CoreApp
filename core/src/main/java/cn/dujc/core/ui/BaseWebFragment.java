package cn.dujc.core.ui;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.lang.reflect.Field;

import cn.dujc.core.R;
import cn.dujc.core.util.LogUtil;

/**
 * Created by du on 2018/2/1.
 */
public class BaseWebFragment extends BaseFragment {

    public static BaseWebFragment newInstance(String title, String url) {
        return newInstance(title, url, null);
    }

    public static BaseWebFragment newInstance(String title, String url, String data) {
        Bundle args = new Bundle();
        args.putString(EXTRA_TITLE, title);
        args.putString(EXTRA_URL, url);
        args.putString(EXTRA_DATA, data);
        BaseWebFragment fragment = new BaseWebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_URL = "EXTRA_URL";
    public static final String EXTRA_DATA = "EXTRA_DATA";

    private ProgressBar mProgressBar;
    private WebView mWebView;
    private String mUrl;
    private String mData;
    private String mTitle;

    @Override
    public void initBasic(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 11 && hardwareAccelerated()) {
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        mUrl = extras().get(EXTRA_URL, "");
        mData = extras().get(EXTRA_DATA, "");
        mTitle = extras().get(EXTRA_TITLE, "");
        mActivity.setTitle("");//防止没有title时没有点击事件2017年3月21日 00:03:20
        init();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 11) {
            mWebView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 11) {
            mWebView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            //加载null内容
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);

            ViewParent parent = mWebView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mWebView);
            }

            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            //mWebView._clearHistory();
            mWebView.clearView();
            //mWebView.removeAllViews();

            mWebView.clearCache(true);
            mWebView.clearFormData();
            mWebView.clearMatches();
            mWebView.clearSslPreferences();
            mWebView.clearDisappearingChildren();
            mWebView.clearHistory();
            mWebView.clearAnimation();
            //mWebView.loadUrl("about:blank");
            mWebView.removeAllViews();
            //mWebView.freeMemory();

            try {
                mWebView.freeMemory();
                mWebView.destroy();
                mWebView = null;
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
            setConfigCallback(null);
        }
        super.onDestroy();
    }

    @Override
    public int getViewId() {
        return R.layout.core_layout_base_web;
    }

    //反射来清理webview的引用
    public void setConfigCallback(WindowManager windowManager) {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);
            if (null == configCallback) {
                return;
            }
            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, windowManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        if (!TextUtils.isEmpty(mTitle)) {
            mActivity.setTitle(mTitle);
        }

        mWebView = new WebView(mActivity.getApplicationContext());
        ((LinearLayout) findViewById(R.id.core_ll_webview_parent))
                .addView(mWebView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mProgressBar = (ProgressBar) findViewById(R.id.core_pb_progressbar);

        mProgressBar.setMax(100);
        mProgressBar.setProgressDrawable(ContextCompat.getDrawable(mActivity, R.drawable.core_progress_bar_states));
        mProgressBar.setProgress(5); //先加载5%，以使用户觉得界面没有卡死

        initWebViewSettings();

        mWebView.setWebViewClient(getWebViewClient());
        mWebView.setWebChromeClient(getWebChromeClient());

        loadAtFirst();
    }

    /**
     * 是否让Activity采用WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
     */
    protected boolean hardwareAccelerated() {
        return true;
    }

    protected boolean _shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }

    protected boolean _onPageStarted(WebView view, String url, Bitmap favicon) {
        return false;
    }

    protected boolean _onPageFinished(WebView view, String url) {
        return false;
    }

    protected boolean _onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        return false;
    }

    protected void _onReceivedTitle(WebView view, String title) {
        if (TextUtils.isEmpty(mTitle)) {//传到当前fragment的title为空时，使用webview获得的title
            mActivity.setTitle(title);
        }
    }

    protected void initWebViewSettings() {
        WebSettings settings = mWebView.getSettings();
        settings.setUserAgentString(settings.getUserAgentString() + "" + mActivity.getPackageName());

        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setAllowFileAccess(true);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            settings.setDisplayZoomControls(false);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);//提高渲染优先级
            settings.setSavePassword(false);
            settings.setPluginState(WebSettings.PluginState.ON);
        }
    }

    protected WebViewClient getWebViewClient() {
        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return _shouldOverrideUrlLoading(view, url) || super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!_onPageStarted(view, url, favicon)) super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!_onPageFinished(view, url)) super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (!_onReceivedError(view, errorCode, description, failingUrl))
                    super.onReceivedError(view, errorCode, description, failingUrl);
                mProgressBar.setVisibility(View.GONE);
            }
        };
    }

    protected WebChromeClient getWebChromeClient() {
        return new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                _onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 5) {
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        };
    }

    protected void loadAtFirst() {
        if (TextUtils.isEmpty(mUrl)) loadData(mData);
        else loadUrl(mUrl);
    }

    public final boolean onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    public final void loadUrl(String url) {
        loadUrl(url, true);
    }

    public final void loadUrl(String url, boolean clear) {
        LogUtil.d("load url = " + url);
        if (mWebView != null) {
            if (clear) _clearHistory();
            mWebView.loadUrl(mUrl = url);
        }
    }

    public final void _clearHistory() {
        if (mWebView != null) mWebView.clearHistory();
    }

    public final void loadData(String data) {
        loadData(data, "text/html; charset=utf-8", "utf-8");
    }

    public final void loadData(String data, String mimeType, String encoding) {
        LogUtil.d("load data = " + data);
        if (mWebView != null) {
            mWebView.loadData(data, mimeType, encoding);
        }
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public final WebView getWebView() {
        return mWebView;
    }
}
