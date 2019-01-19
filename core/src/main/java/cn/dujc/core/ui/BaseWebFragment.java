package cn.dujc.core.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.List;

import cn.dujc.core.R;
import cn.dujc.core.util.LogUtil;

/**
 * Created by du on 2018/2/1.
 */
public class BaseWebFragment extends BaseRefreshableFragment {

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

    private final static String[] DB_NAME_LIST = {"webview.db", "webviewCache.db", "webviewCookiesChromium.db", "webviewCookiesChromiumPrivate.db"};

    private ProgressBar pb_progressbar;
    private WebView web_simple_view;
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
        setupRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 11) {
            web_simple_view.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 11) {
            web_simple_view.onResume();
        }
    }

    @Override
    public void onDestroy() {
        if (web_simple_view != null) {
            //加载null内容
            web_simple_view.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);

            ViewParent parent = web_simple_view.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(web_simple_view);
            }

            web_simple_view.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            web_simple_view.getSettings().setJavaScriptEnabled(false);
            //web_simple_view.clearHistory();
            web_simple_view.clearView();
            //web_simple_view.removeAllViews();

            web_simple_view.clearCache(true);
            web_simple_view.clearFormData();
            web_simple_view.clearMatches();
            web_simple_view.clearSslPreferences();
            web_simple_view.clearDisappearingChildren();
            web_simple_view.clearHistory();
            web_simple_view.clearAnimation();
            //web_simple_view.loadUrl("about:blank");
            web_simple_view.removeAllViews();
            //web_simple_view.freeMemory();

            try {
                web_simple_view.freeMemory();
                web_simple_view.destroy();
                web_simple_view = null;
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
            setConfigCallback(null);
        }
        super.onDestroy();
    }

    @Override
    public int getViewId() {
        return R.layout.dujc_core_layout_base_web;
    }

    @Override
    public void onRefresh() {
        if (web_simple_view != null) web_simple_view.reload();
        refreshDone();
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

        web_simple_view = new WebView(mActivity.getApplicationContext());
        ((LinearLayout)findViewById(R.id.ll_webview_parent))
                .addView(web_simple_view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        pb_progressbar = (ProgressBar) findViewById(R.id.pb_progressbar);

        pb_progressbar.setMax(100);
        pb_progressbar.setProgressDrawable(ContextCompat.getDrawable(mActivity, R.drawable.dujc_core_progress_bar_states));
        pb_progressbar.setProgress(5); //先加载5%，以使用户觉得界面没有卡死

        initWebViewSettings();

        web_simple_view.setWebViewClient(getWebViewClient());
        web_simple_view.setWebChromeClient(getWebChromeClient());

        loadAtFirst();
    }

    protected void setupRefresh() {
        getSwipeRefreshLayout().setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                return web_simple_view != null && web_simple_view.getScrollY() > 0;
            }
        });
    }

    protected void initWebViewSettings() {
        WebSettings settings = web_simple_view.getSettings();
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
                pb_progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb_progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                pb_progressbar.setVisibility(View.GONE);
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
                    pb_progressbar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        };
    }

    protected void loadAtFirst() {
        loadUrl(mUrl);
    }

    /**
     * 能否处理intent
     *
     * @param context
     * @param intent
     * @return
     */
    public boolean hasIntentHandler(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public final boolean onBackPressed() {
        if (web_simple_view.canGoBack()) {
            web_simple_view.goBack();
            return true;
        }
        return false;
    }

    public final void loadUrl(String url) {
        loadUrl(url, true);
    }

    public final void loadUrl(String url, boolean clear) {
        LogUtil.d("load url = " + url);
        if (web_simple_view != null) {
            if (clear) web_simple_view.clearHistory();
            web_simple_view.loadUrl(mUrl = url);
        }
    }

    public ProgressBar getProgressBar() {
        return pb_progressbar;
    }

    public final WebView getWebView() {
        return web_simple_view;
    }
}
