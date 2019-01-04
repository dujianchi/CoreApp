package cn.dujc.core.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import cn.dujc.core.util.ContextUtil;

/**
 * 状态栏占位,提供一个状态栏的高度
 *
 * @author du
 * date 2018/5/11 上午11:10
 */
public class StatusBarPlaceholder extends View {

    private boolean mOpen = true;
    private boolean mOverrideSystemOpen = false;
    private final int mStatusBarHeight;

    public StatusBarPlaceholder(Context context) {
        this(context, null, 0);
    }

    public StatusBarPlaceholder(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusBarPlaceholder(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mStatusBarHeight = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? TitleCompat.getStatusBarHeight(context.getResources()) : 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        if (isSystemOpen() && mOpen) {
            height = mStatusBarHeight;
        }
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    public boolean isSystemOpen() {
        boolean open = mOverrideSystemOpen;
        final Activity activity = ContextUtil.getActivity(getContext());
        if (activity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                open = open || (activity.getWindow().getDecorView().getSystemUiVisibility() & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) == View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                open = open || (activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) == WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            }
        }
        return open;
    }

    /**
     * 由于1.2.0-alpha1以后，toolbar改成持有application的context，无法根据context获取activity，然后判断当前activity是否开启了沉浸效果，
     * 所以定义这个方法，用于重写系统方法，但此方法即使传入true，也需要当前运行环境大于4.4才有效。
     * 1.2.0-alpha2又改成持有自身activity的context，因为太麻烦……
     *
     * @param overrideSystemOpen 是否重写系统判断
     */
    public void setOverrideSystemOpen(boolean overrideSystemOpen) {
        mOverrideSystemOpen = overrideSystemOpen && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public void placeholder(boolean open) {
        mOpen = open;
        requestLayout();
    }

}
