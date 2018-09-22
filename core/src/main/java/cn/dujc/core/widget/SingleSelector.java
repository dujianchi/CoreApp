package cn.dujc.core.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.dujc.core.R;
import cn.dujc.core.util.LogUtil;

/**
 * @author du
 * date 2018/9/22 下午9:30
 */
public class SingleSelector extends LinearLayout {

    public interface OnSelectorChangedListener {
        void onSelectorChanged(int index);
    }

    private int mCurrentIndex = 0;
    private View mLastSelected = null;
    private OnSelectorChangedListener mOnSelectorChangedListener;

    public SingleSelector(@NonNull Context context) {
        this(context, null, 0);
    }

    public SingleSelector(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleSelector(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SingleSelector);
            mCurrentIndex = array.getInteger(R.styleable.SingleSelector_single_selector_default_index, mCurrentIndex);
            array.recycle();
        }
    }

    private LayoutParams generateParams(){
        return new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
    }

//    @Override//理论上只需要重写下面的方法这个也会影响到
//    public void addView(View child) {
//        super.addView(child, generateParams());
//    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index, generateParams());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mCurrentIndex >= 0 && getChildCount() > 0) {
            setCurrentIndex(mCurrentIndex);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            final float x = event.getRawX(), y = event.getRawY();
            final int count = getChildCount();
            for (int index = 0; index < count; index++) {
                final View child = getChildAt(index);
                final int[] location = new int[2];
                child.getLocationInWindow(location);
                final int l = location[0], r = l + child.getWidth(), t = location[1], b = t + child.getHeight();
                LogUtil.d("-------- index = " + index);
                if (mLastSelected != child && child.getVisibility() == VISIBLE && l <= x && x <= r && t <= y && y <= b) {
                    setCurrentIndex(index);
                    LogUtil.d("-------- index = " + index);
                    break;
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void setOnSelectorChangedListener(OnSelectorChangedListener onSelectorChangedListener) {
        mOnSelectorChangedListener = onSelectorChangedListener;
    }

    /**
     * 移除大于x到无用的控件
     *
     * @param greaterThan x
     */
    public void removeUnnecessary(int greaterThan) {
        if (getChildCount() > greaterThan) {
            removeViews(greaterThan, getChildCount() - greaterThan);
        }
    }

    public void setCurrentIndex(int index) {
        final View child = getChildAt(index);
        if (mLastSelected != child) {
            if (mLastSelected != null) mLastSelected.setSelected(false);
            (mLastSelected = child).setSelected(true);
            mCurrentIndex = index;
            if (mOnSelectorChangedListener != null) {
                mOnSelectorChangedListener.onSelectorChanged(index);
            }
        }
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }
}
