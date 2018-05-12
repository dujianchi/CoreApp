package cn.dujc.core.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.util.List;

import cn.dujc.core.R;
import cn.dujc.core.bridge.ActivityStackUtil;

/**
 * 基本的Activity。所有Activity必须继承于此类。“所有”！
 * Created by lucky on 2017/9/19.
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseUI, IBaseUI.IPermissionKeeperCallback {

    private IStarter mStarter = null;
    private IParams mParams = null;
    private IPermissionKeeper mPermissionKeeper = null;

    protected Activity mActivity;
    protected Toolbar mToolbar = null;
    private TitleCompat mTitleCompat = null;
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
    @Nullable
    public View createRootView(View contentView) {
        return linearRootView(contentView);
    }

    @Nullable
    public TitleCompat initTransStatusBar() {
        if (mTitleCompat == null) mTitleCompat = TitleCompat.setStatusBar(mActivity, true, true);
        return mTitleCompat;
    }

    @Override
    public TitleCompat getTitleCompat() {
        return mTitleCompat;
    }

    @Override
    @Nullable
    public Toolbar initToolbar(ViewGroup parent) {
        return null;
    }

    @Override
    public IStarter starter() {
        if (mStarter == null) mStarter = new IStarterImpl(this);
        else mStarter.clear();//为什么要clear呢？想了想，实际上我用的一直是同一个starter，那么，如果一直界面往不同界面都传了值，它就会一直累加……
        return mStarter;
    }

    @Override
    public IParams extras() {
        if (mParams == null) mParams = new IParamsImpl(this);
        return mParams;
    }

    @Override
    public IPermissionKeeper permissionKeeper() {
        if (mPermissionKeeper == null) mPermissionKeeper = new IPermissionKeeperImpl(this, this);
        return mPermissionKeeper;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPermissionKeeper != null) {
            mPermissionKeeper.handOnActivityResult(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionKeeper != null) {
            mPermissionKeeper.handOnRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onGranted(int requestCode, List<String> permissions) { }

    @Override
    public void onDenied(int requestCode, List<String> permissions) { }

    /**
     * 关联主界面 **只有在使用自定义View时使用**
     */
    @Override
    @Nullable
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
