package cn.dujc.coreapp;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.List;

/**
 * 广告切换
 *
 * @author du
 * date 2018/8/31 上午9:21
 */
public class ADSwitcher extends ViewSwitcher implements ViewSwitcher.ViewFactory {

    public interface OnSwitchListener {
        public void onSwitchListener(View child, int index);
    }

    private static final int TEXT_COUNT = 2;
    private static final int INTERNAL = 2000;
    private static final Handler HANDLER = new Handler();
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            showNext();
            delayToNext();
        }
    };
    private OnSwitchListener mOnSwitchListener;
    private List<?> mList;
    private int mCurrentIndex = 0;
    private boolean mPlaying = false;

    public ADSwitcher(Context context) {
        this(context, null);
    }

    public ADSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setFactory(this);
    }

    private void nextAnim() {
        setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_bottom_in));
        setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_bottom_out));
    }

    private void preAnim() {
        setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_top_in));
        setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.translate_top_out));
    }

    public final int getCount() {
        return mList == null ? 0 : mList.size();
    }

    public final String getItem(int index) {
        if (0 <= index && index < getCount()) return String.valueOf(mList.get(index));
        return "";
    }

    /**
     * 清除旧的延时任务
     */
    private void removeDelayTask() {
        HANDLER.removeCallbacks(mRunnable);
    }

    /**
     * 继续延迟任务
     */
    private void delayToNext() {
        removeDelayTask();
        HANDLER.postDelayed(mRunnable, INTERNAL);
    }

    private void showContent(boolean next) {
        final int count = getCount();
        final ViewHolder holder = (ViewHolder) (next ? getNextView() : getCurrentView()).getTag();
        if (count > 0) {
            if (mCurrentIndex >= count) {
                mCurrentIndex = 0;
            } else if (mCurrentIndex < 0) {
                mCurrentIndex = count - TEXT_COUNT;
            }
            holder.updateIndexListener(mCurrentIndex, mOnSwitchListener);
            holder.mTop.setText(getItem(mCurrentIndex));
            holder.mBottom.setText(getItem(mCurrentIndex + 1));
        }
    }

    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        mOnSwitchListener = onSwitchListener;
    }

    public void setList(List<?> list) {
        mList = list;
        mCurrentIndex = 0;
        showContent(false);
    }

    /**
     * 开始轮播
     */
    public void start() {
        mPlaying = true;
        removeDelayTask();
        delayToNext();
    }

    @Override
    public View makeView() {
        return ViewHolder.create(getContext()).mLayout;
    }

    @Override
    public void showNext() {
        mCurrentIndex += TEXT_COUNT;
        showContent(true);
        nextAnim();
        super.showNext();
    }

    @Override
    public void showPrevious() {
        mCurrentIndex -= TEXT_COUNT;
        showContent(true);
        preAnim();
        super.showPrevious();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mPlaying) delayToNext();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeDelayTask();
    }

    private static class ViewHolder implements OnClickListener {
        private OnSwitchListener mOnSwitchListener;
        private int mIndex = 0;
        private LinearLayout mLayout;
        private TextView mTop, mBottom;

        private ViewHolder(Context context) {
            mLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_index_ad_switcher, null);
            mTop = (TextView) mLayout.findViewById(R.id.tv_top);
            mBottom = (TextView) mLayout.findViewById(R.id.tv_bottom);
            mTop.setOnClickListener(this);
            mBottom.setOnClickListener(this);
            mLayout.setTag(this);
        }

        static ViewHolder create(Context context) {
            return new ViewHolder(context);
        }

        public void updateIndexListener(int index, OnSwitchListener listener) {
            mIndex = index;
            mOnSwitchListener = listener;
        }

        @Override
        public void onClick(View v) {
            if (mOnSwitchListener != null) {
                if (v == mTop && mTop.getText().length() > 0) {
                    mOnSwitchListener.onSwitchListener(mTop, mIndex);
                } else if (v == mBottom && mBottom.getText().length() > 0) {
                    mOnSwitchListener.onSwitchListener(mBottom, mIndex + 1);
                }
            }
        }
    }
}
