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
        Bundle args = new Bundle();
        args.putString(TITLE_INTENT, title);
        args.putString(URL_INTENT, url);
        BaseWebFragment fragment = new BaseWebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static final String TITLE_INTENT = "TITLE_INTENT";
    private static final String URL_INTENT = "URL_INTENT";

    private ProgressBar mProgressBar;
    private WebView mWebView;
    private String mUrl;
    private String mTitle;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (args != null) {
            mUrl = args.getString(URL_INTENT);
            mTitle = args.getString(TITLE_INTENT);
        }
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= 11) {
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
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
            //mWebView.clearHistory();
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
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        if (!TextUtils.isEmpty(mTitle)) {
            mActivity.setTitle(mTitle);
        }

        mWebView = new WebView(mActivity.getApplicationContext());
        ((LinearLayout)findViewById(R.id.core_ll_webview_parent))
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
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
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
                if (TextUtils.isEmpty(mTitle)) {
                    mActivity.setTitle(title);
                }
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
        loadUrl(mUrl);
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
            if (clear) mWebView.clearHistory();
            mWebView.loadUrl(mUrl = url);
        }
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public final WebView getWebView() {
        return mWebView;
    }
}
