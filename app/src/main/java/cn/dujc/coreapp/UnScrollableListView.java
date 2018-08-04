package cn.dujc.coreapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author du
 * date 2018/7/29 下午10:27
 */
public class UnScrollableListView extends LinearLayout {

    public interface OnItemClickListener {
        void onItemClick(int index, View itemView);
    }

    public static abstract class Adapter {

        private UnScrollableListView observer;

        private void setObserver(UnScrollableListView observer) {
            this.observer = observer;
        }

        public void notifyDataSetChanged() {
            if (this.observer != null) {
                this.observer.refreshAll();
            }
        }

        protected Object getItem(int position) {
            return null;
        }

        protected abstract int getCount();

        /**
         * 获取子控件
         *
         * @param position  位置
         * @param existView 已存在的控件，这个参数的存在，是为了减少创建子控件，如子控件从3->4，只需要创建第4个，前3个可以继续使用不必remove
         * @return 相应位置的子控件
         */
        protected abstract View getView(int position, View existView, ViewGroup parent);

    }

    private Adapter mAdapter;
    private OnItemClickListener mOnItemClickListener;

    public UnScrollableListView(Context context) {
        this(context, null, 0);
    }

    public UnScrollableListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnScrollableListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * @param l
     * @deprecated 请不要为这个表格设置点击事件，请使用{@link #setOnItemClickListener(OnItemClickListener)}
     */
    @Deprecated
    @Override
    public final void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mOnItemClickListener != null && ev.getAction() == MotionEvent.ACTION_UP) {
            final float x = ev.getRawX(), y = ev.getRawY();
            final int childCount = getChildCount();
            for (int index = 0; index < childCount; index++) {
                View child = getChildAt(index);
                if (child != null) {
                    final int[] location = new int[2];
                    child.getLocationOnScreen(location);
                    if (location[0] <= x
                            && x <= location[0] + child.getWidth()
                            && location[1] <= y
                            && y <= location[1] + child.getHeight()) {
                        mOnItemClickListener.onItemClick(index, child);
                        break;
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        if (mAdapter != null) {//防止设置的adapter为null
            mAdapter.setObserver(this);
        }
        refreshAll();
    }

    private void refreshAll() {
        final int count = mAdapter == null ? 0 : mAdapter.getCount();
        while (getChildCount() > count) {
            removeViewAt(getChildCount() - 1);
        }
        for (int i = 0; i < count; i++) {
            View existView = getChildAt(i);//因为只移除了多余的View。所以这边要先获取是否存在这个child
            View view = mAdapter.getView(i, existView, this);//adapter中判断，是new View还是使用existView
            if (existView != view) {//只有当两个不相等的时候才addView，否则只是在adapter中更新而已
                removeView(existView);//防止没有使用重用时，多添加了View
                addView(view, i);
            }
        }
    }
}
