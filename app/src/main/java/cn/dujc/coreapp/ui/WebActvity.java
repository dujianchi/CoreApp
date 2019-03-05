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
//        mFragment = BaseWebFragment.newInstance("baidu", "http://192.168.61.61/tomedclinic2/interface1/honey3.6/tomedclinic2/bloodtest_shop/manage_page.php?type=0&blood_module=2&token=501b182e9717ff4002f571f4d8833273&orders_code=155168855720260&apply_src_phone=blood_apply/aplly_src/b97ff48645_1551684310.html&bloodsample_id=21402&pname=%E5%8D%A1%E5%8D%A1%E5%8D%A1%E7%BB%86%E8%83%9Eggg%E9%9A%8F%E6%97%B6%20.."/*"https://www.baidu.com/s?wd=js%20%E5%92%8C%20Android%20%E4%BA%A4%E4%BA%92"*/);
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
