package cn.dujc.coreapp.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.ui.BaseWebFragment;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;

public class WebActvity extends BaseActivity {

    private BaseWebFragment mFragment;
    private boolean mLoaded = false;

    @Override
    public int getViewId() {
        return R.layout.activity_web;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mFragment = BaseWebFragment.newInstance("baidu", "file:////android_asset/index.html"/*"https://www.baidu.com/s?wd=js%20%E5%92%8C%20Android%20%E4%BA%A4%E4%BA%92"*/);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, mFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mLoaded){
            mFragment.getWebView().addJavascriptInterface(this, "android");
            mLoaded = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null && mFragment.onBackPressed()) return;
        super.onBackPressed();
    }

    public void toJs(View v){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mFragment.loadUrl("javascript:receiveToken('123')", false);
        } else {
            mFragment.getWebView().evaluateJavascript("receiveToken('123')", null);
        }
    }

    @JavascriptInterface//微信支付
    public void sendToken() {
        ToastUtil.showToast(mActivity, "js 调用了 原生");
    }
}
