package cn.dujc.core.ui;

import android.app.Activity;
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

/**
 * 基本的Activity。所有Activity必须继承于此类。“所有”！
 * Created by lucky on 2017/9/19.
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseUI {

    private IStarter mStarter = null;

    protected Activity mActivity;
    protected Toolbar mToolbar = null;
    protected TitleCompat mTitleCompat = null;
    protected View mRootView = null;

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
        mRootView = getViewV();

        super.onCreate(savedInstanceState);
        ActivityStackUtil.getInstance().addActivity(this);

        if (vid != 0 || mRootView != null) {
            mRootView = vid == 0 ? mRootView : getLayoutInflater().inflate(vid, null);
            setContentView(createRootView(mRootView));
            mTitleCompat = initTransStatusBar();//这句一定要放在setcontent之后，initbasic之前，否则一些沉浸效果无法显现（改逻辑除外）
            initBasic(savedInstanceState);
            setupToolbar();
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

    @Override
    public View createRootView(View contentView) {
        return linearRootView(contentView);
    }

    @Override
    public TitleCompat initTransStatusBar() {
        TitleCompat titleCompat = TitleCompat.setStatusBar(mActivity, true, true);
        return titleCompat;
    }

    @Override
    public Toolbar initToolbar(ViewGroup parent) {
        return null;
    }

    @Override
    public IStarter starter() {
        if (mStarter == null) mStarter = new IStarterImpl(this);
        return mStarter;
    }

    /**
     * 关联主界面 **只有在使用自定义View时使用**
     */
    @Override
    public View getViewV() {
        return null;
    }

    protected void setupToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
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

}
