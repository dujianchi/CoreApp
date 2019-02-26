package cn.dujc.core.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.dujc.core.R;
import cn.dujc.core.initializer.toolbar.IToolbar;
import cn.dujc.core.initializer.toolbar.IToolbarHandler;

/**
 * 基本的Fragment。最好Fragment都要继承于此类
 * Created by du on 2017/9/19.
 */
public abstract class BaseFragment extends Fragment implements IBaseUI.WithToolbar, IBaseUI.IPermissionKeeperCallback {

    private IStarter mStarter = null;
    private IParams mParams = null;
    private IPermissionKeeper mPermissionKeeper = null;

    private boolean mLoaded = false;//是否已经载入
    protected View mToolbar;
    protected View mRootView;
    private TitleCompat mTitleCompat = null;
    protected Activity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        final int vid = getViewId();
        final View rootView = getViewV();
        Context context = container != null ? container.getContext() : null;

        if (mRootView == null && (vid != 0 || rootView != null) && context != null) {
            if (rootView == null) {
                mRootView = createRootView(inflater.inflate(vid, container, false));
            } else {
                mRootView = createRootView(rootView);
            }
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!mLoaded && mRootView != null) {
            mLoaded = true;
            initBasic(savedInstanceState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyRootViewAndToolbar();
    }

    @Override
    public View createRootView(View view) {
        final View[] viewAndToolbar = BaseToolbarHelper.createRootViewAndToolbar(toolbarStyle(), mActivity, this, view);
        mToolbar = viewAndToolbar[1];
        return viewAndToolbar[0];
    }

    @Override
    @Nullable
    public View initToolbar(ViewGroup parent) {
        return null;
    }

    @Override
    @Nullable
    public TitleCompat getTitleCompat() {
        if (mActivity instanceof BaseActivity) {
            mTitleCompat = ((BaseActivity) mActivity).getTitleCompat();
        }
        return mTitleCompat;
    }

    @Override
    public IStarter starter() {
        if (mStarter == null) mStarter = new IStarterImpl(this);
        else mStarter.clear();//为什么要clear呢？想了想，实际上我用的一直是同一个starter，那么，如果一直界面往不同界面都传了值，它就会一直累加……
        return mStarter;
    }

    @Override
    public IParams extras() {
        if (mParams == null) mParams = new FragmentParamsImpl(this);
        return mParams;
    }

    @Override
    public IPermissionKeeper permissionKeeper() {
        if (mPermissionKeeper == null) mPermissionKeeper = new IPermissionKeeperImpl(this, this);
        return mPermissionKeeper;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    @Nullable
    public final <T extends View> T findViewById(int resId) {
        return mRootView != null ? (T) mRootView.findViewById(resId) : null;
    }

    /**
     * 通知fragment改变了，需要这个的功能，在子类重写，然后其他地方调用这个子类的这个方法，就可以改动这个方法
     */
    public void notifyFragmentChanged() {/*在这重写需要更新fragment的动作*/}

    public void setTitle(CharSequence title) {
        if (mToolbar != null) {
            final View textMaybe = mToolbar.findViewById(R.id.core_toolbar_title_id);
            if (textMaybe instanceof TextView) ((TextView) textMaybe).setText(title);
        }
    }

    public void setTitle(int titleId) {
        setTitle(getText(titleId));
    }

    public void setTitleMenuText(CharSequence menuText, @Nullable View.OnClickListener onClickListener) {
        setTitleMenuText(menuText, 0, onClickListener);
    }

    /**
     * 设置菜单
     *
     * @param position 所在位置，最大支持4个，为-1时全部隐藏
     *                 ，position传入0到3之间的值时，如果找到的控件类型为TextView
     *                 ，且其所在父类的第index值大于等于position，则认为是要寻找的菜单控件
     */
    public void setTitleMenuText(CharSequence menuText, @IntRange(from = -1, to = 3) int position, @Nullable View.OnClickListener onClickListener) {
        if (mToolbar != null) {
            final View textMaybe = mToolbar.findViewById(R.id.core_toolbar_menu_id);
            if (textMaybe instanceof TextView) {
                textMaybe.setVisibility(View.VISIBLE);
                ((TextView) textMaybe).setText(menuText);
                if (onClickListener != null) textMaybe.setOnClickListener(onClickListener);
            } else if (textMaybe instanceof ViewGroup) {
                if (position == -1) {
                    textMaybe.setVisibility(View.GONE);
                } else {
                    final ViewGroup viewGroup = (ViewGroup) textMaybe;
                    for (int index = 0, count = viewGroup.getChildCount(); index < count; index++) {
                        final View childAt = viewGroup.getChildAt(index);
                        if (position <= index && childAt instanceof TextView) {
                            textMaybe.setVisibility(View.VISIBLE);
                            childAt.setVisibility(View.VISIBLE);
                            ((TextView) childAt).setText(menuText);
                            if (onClickListener != null)
                                textMaybe.setOnClickListener(onClickListener);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void setTitleMenuIcon(@DrawableRes int menuRes, @Nullable View.OnClickListener onClickListener) {
        setTitleMenuIcon(menuRes, 0, onClickListener);
    }

    /**
     * 设置菜单
     *
     * @param position 所在位置，最大支持4个，为-1时全部隐藏
     *                 ，position传入0到3之间的值时，如果找到的控件类型为ImageView
     *                 ，且其所在父类的第index值大于等于position，则认为是要寻找的菜单控件
     */
    public void setTitleMenuIcon(@DrawableRes int menuRes, @IntRange(from = -1, to = 3) int position, @Nullable View.OnClickListener onClickListener) {
        if (mToolbar != null) {
            final View imageMaybe = mToolbar.findViewById(R.id.core_toolbar_menu_id);
            if (imageMaybe instanceof ImageView) {
                imageMaybe.setVisibility(View.VISIBLE);
                ((ImageView) imageMaybe).setImageResource(menuRes);
                if (onClickListener != null) imageMaybe.setOnClickListener(onClickListener);
            } else if (imageMaybe instanceof ViewGroup) {
                if (position == -1) {
                    imageMaybe.setVisibility(View.GONE);
                } else {
                    final ViewGroup viewGroup = (ViewGroup) imageMaybe;
                    for (int index = 0, count = viewGroup.getChildCount(); index < count; index++) {
                        final View childAt = viewGroup.getChildAt(index);
                        if (position <= index && childAt instanceof ImageView) {
                            imageMaybe.setVisibility(View.VISIBLE);
                            childAt.setVisibility(View.VISIBLE);
                            ((ImageView) childAt).setImageResource(menuRes);
                            if (onClickListener != null)
                                imageMaybe.setOnClickListener(onClickListener);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 是否线性排列toolbar，否的话则toolbar在布局上方
     */
    protected int toolbarStyle() {
        final IToolbar iToolbar = IToolbarHandler.getToolbar(mActivity);
        if (iToolbar != null) return iToolbar.toolbarStyle();
        return IToolbar.LINEAR;
    }

    protected void destroyRootViewAndToolbar() {
        mLoaded = false;
        if (mToolbar != null) {
            final ViewParent parent = mToolbar.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mToolbar);
            }
            mToolbar = null;
        }
        if (mRootView != null) {
            final ViewParent parent = mRootView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mRootView);
            }
            mRootView = null;
        }
    }

}
