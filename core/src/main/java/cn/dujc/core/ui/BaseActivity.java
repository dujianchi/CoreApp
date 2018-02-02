package cn.dujc.core.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import cn.dujc.core.R;
import cn.dujc.core.bridge.ActivityStackUtil;
import cn.dujc.core.util.LogUtil;

/**
 * 基本的Activity。所有Activity必须继承于此类。“所有”！
 * Created by lucky on 2017/9/19.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Activity mActivity;
    protected Toolbar mToolbar = null;
    protected TitleCompat mTitleCompat = null;
    //private View mContentLayout = null;//内容的View，即不包含toolbar的布局

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mActivity = this;
        if (alwaysPortrait()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        }

        if (fullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        initAnimEnter();
        final int vid = getViewId();
        final View rootView = getRootView();

        super.onCreate(savedInstanceState);
        ActivityStackUtil.getInstance().addActivity(this);

        if (vid != 0 || rootView != null) {
            View contentView = vid == 0 ? rootView : getLayoutInflater().inflate(vid, null);
            setContentView(createRootView(contentView));
            mTitleCompat = initTransStatusBar();//这句一定要放在setcontent之后，initbasic之前，否则一些沉浸效果无法显现（改逻辑除外）
            initBasic(savedInstanceState);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayShowHomeEnabled(false);
                    actionBar.setDisplayShowTitleEnabled(false);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void finish() {
        super.finish();
        initAnimExit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackUtil.getInstance().removeActivity(this);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        ActivityStackUtil.getInstance().addFragment(this, fragment);
    }

    protected View createRootView(View contentView) {
        return linearRootView(contentView);
    }

    protected TitleCompat initTransStatusBar() {
        TitleCompat titleCompat = TitleCompat.setStatusBar(mActivity, true, true);
        return titleCompat;
    }

    protected Toolbar initToolbar(ViewGroup parent) {
        return null;
    }

    /**
     * 退出动画
     */
    protected void initAnimExit() {
        overridePendingTransition(R.anim.push_pic_right_in, R.anim.push_pic_right_out);
    }

    /**
     * 进入动画
     */
    protected void initAnimEnter() {
        overridePendingTransition(R.anim.push_pic_left_in, R.anim.push_pic_left_out);
    }

    /**
     * 是否全屏
     *
     * @return true则全屏
     */
    protected boolean fullScreen() {
        return false;
    }

    /**
     * 是否强制竖屏
     *
     * @return true则强制竖屏
     */
    protected boolean alwaysPortrait() {
        return true;
    }

    /**
     * 标题与界面线性排列
     */
    protected final View linearRootView(View contentView) {
        final LinearLayout layout = new LinearLayout(this);
        mToolbar = initToolbar(layout);
        if (mToolbar != null) {
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(mToolbar);
            layout.addView(contentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                    , LinearLayout.LayoutParams.MATCH_PARENT));
            return layout;
        } else {
            return contentView;
        }
    }

    /**
     * 标题与界面帧层叠
     */
    protected final View frameRootView(View contentView) {
        final FrameLayout layout = new FrameLayout(this);
        mToolbar = initToolbar(layout);
        if (mToolbar != null) {
            layout.addView(contentView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT
                    , FrameLayout.LayoutParams.MATCH_PARENT));
            layout.addView(mToolbar);
            return layout;
        } else {
            return contentView;
        }
    }

    ///**
    // * 设置内容（即不包含toolbar的布局）的布局，如果在修改了显示方式之后，需要调用此方法
    // */
    //protected final void setContentLayout(View contentLayout) {
    //    mContentLayout = contentLayout;
    //}

    ///**
    // * 设置内容的View是否可用，可用在进行一个接口请求过程中，把整个界面设置为不可用，来防止点击或操作
    // * 此方法与{@link #createRootView(int)}的实现有关，如果没有使用默认的onCreate或者以上适配的
    // * 两种{@link #linearRootView(int)}或{@link #frameRootView(int)}方法，需要另外再设置{@link #setContentLayout(View)}
    // *
    // * @param enable 是否可用
    // */
    //protected final void setContentLayoutEnable(boolean enable) {
    //    if (mContentLayout != null) mContentLayout.setEnabled(enable);
    //}

    /**
     * 自增的request code，每个跳转都是forresult的跳转，那么，今后只要记住跳转方法{@link #go(Class)}或{@link #go(Class, Bundle)}返回的int值
     * ，即为本次跳转产生的request code，从此不再管理request code，且不会再重复，因为不管在什么界面跳转，每次跳转都用了不同的request code（当然，崩溃重启的情况例外）
     */
    private static final int[] _INCREMENT_REQUEST_CODE = {1};

    public int go(Class<? extends Activity> activity) {
        return go(activity, null);
    }

    public int go(Class<? extends Activity> activity, Bundle args) {
        Intent intent = new Intent(this, activity);
        if (args != null) {
            intent.putExtras(args);
        }
        int requestCode = _INCREMENT_REQUEST_CODE[0]++;
        if (requestCode >= 0xffff) {
            requestCode = _INCREMENT_REQUEST_CODE[0] = 1;
        }
        LogUtil.d("------------ request code = " + requestCode);
        startActivityForResult(intent, requestCode);
        return requestCode;
    }

    /**
     * 关联主界面 **只有在使用自定义View时使用**
     */
    public View getRootView() {
        return null;
    }

    /**
     * 关联主界面
     */
    public abstract int getViewId();

    /**
     * 基础初始化
     */
    public abstract void initBasic(Bundle savedInstanceState);
}
