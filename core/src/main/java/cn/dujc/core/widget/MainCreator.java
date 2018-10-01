package cn.dujc.core.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.dujc.core.R;

/**
 * @author du
 * date 2018/9/22 下午9:27
 */
public class MainCreator extends RelativeLayout {

    public static interface INormalTab extends Cloneable {
        INormalTab setText(CharSequence text);

        INormalTab setDefaultColor(int defaultColor);

        INormalTab setSelectedColor(int selectedColor);

        INormalTab setTextSize(int textSize);

        INormalTab setIconDefaultId(int iconDefaultId);

        INormalTab setIconSelectedId(int iconSelectedId);

        INormalTab setBetweenSize(int betweenSize);

        INormalTab setTopSize(int topSize);

        INormalTab setBottomSize(int bottomSize);

        CharSequence getText();

        int getDefaultColor();

        int getSelectedColor();

        int getTextSize();

        int getIconDefaultId();

        int getIconSelectedId();

        int getBetweenSize();

        int getTopSize();

        int getBottomSize();
    }

    public static class NormalTabImpl implements INormalTab {

        private CharSequence mText;
        private int mDefaultColor, mSelectedColor, mTextSize, mIconDefaultId, mIconSelectedId, mBetweenSize, mTopSize, mBottomSize;

        public NormalTabImpl() { }

        public NormalTabImpl(CharSequence text, int defaultColor, int selectedColor, int textSize
                , int iconDefaultId, int iconSelectedId, int betweenSize, int bottomSize, int topSize) {
            mText = text;
            mDefaultColor = defaultColor;
            mSelectedColor = selectedColor;
            mTextSize = textSize;
            mIconDefaultId = iconDefaultId;
            mIconSelectedId = iconSelectedId;
            mBetweenSize = betweenSize;
            mBottomSize = bottomSize;
            mTopSize = topSize;
        }

        public INormalTab setText(CharSequence text) {
            mText = text;
            return this;
        }

        public INormalTab setDefaultColor(int defaultColor) {
            mDefaultColor = defaultColor;
            return this;
        }

        public INormalTab setSelectedColor(int selectedColor) {
            mSelectedColor = selectedColor;
            return this;
        }

        public INormalTab setTextSize(int textSize) {
            mTextSize = textSize;
            return this;
        }

        public INormalTab setIconDefaultId(int iconDefaultId) {
            mIconDefaultId = iconDefaultId;
            return this;
        }

        public INormalTab setIconSelectedId(int iconSelectedId) {
            mIconSelectedId = iconSelectedId;
            return this;
        }

        public INormalTab setBetweenSize(int betweenSize) {
            mBetweenSize = betweenSize;
            return this;
        }

        public INormalTab setTopSize(int topSize) {
            mTopSize = topSize;
            return this;
        }

        public INormalTab setBottomSize(int bottomSize) {
            mBottomSize = bottomSize;
            return this;
        }

        @Override
        public CharSequence getText() {
            return mText;
        }

        @Override
        public int getDefaultColor() {
            return mDefaultColor;
        }

        @Override
        public int getSelectedColor() {
            return mSelectedColor;
        }

        @Override
        public int getTextSize() {
            return mTextSize;
        }

        @Override
        public int getIconDefaultId() {
            return mIconDefaultId;
        }

        @Override
        public int getIconSelectedId() {
            return mIconSelectedId;
        }

        @Override
        public int getBetweenSize() {
            return mBetweenSize;
        }

        @Override
        public int getTopSize() {
            return mTopSize;
        }

        @Override
        public int getBottomSize() {
            return mBottomSize;
        }

        @Override
        public NormalTabImpl clone() {
            return new NormalTabImpl(mText, mDefaultColor, mSelectedColor, mTextSize
                    , mIconDefaultId, mIconSelectedId, mBetweenSize, mBottomSize, mTopSize);
        }
    }

    private static final float DEFAULT_BOTTOM_HEIGHT_DP = 52.0f;

    private final SparseArray<Fragment> mFragmentArray = new SparseArray<>();

    private int mDefaultIndex = 0;

    private FrameLayout mContainer;
    private SingleSelector mBottom;

    public MainCreator(@NonNull Context context) {
        this(context, null, 0);
    }

    public MainCreator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainCreator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int bottomHeight = (int) (getResources().getDisplayMetrics().density * DEFAULT_BOTTOM_HEIGHT_DP + 0.5f);
        int containerColor = -1, bottomColor = -1;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MainCreator);
            mDefaultIndex = array.getInteger(R.styleable.MainCreator_main_creator_default_index, mDefaultIndex);
            bottomHeight = array.getDimensionPixelOffset(R.styleable.MainCreator_main_creator_bottom_height, bottomHeight);
            bottomColor = array.getColor(R.styleable.MainCreator_main_creator_bottom_color, bottomColor);
            containerColor = array.getColor(R.styleable.MainCreator_main_creator_container_color, containerColor);
            array.recycle();
        }

        removeAllViews();

        mContainer = new FrameLayout(context);
        mContainer.setId(R.id.main_creator_container);
        LayoutParams containerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        containerParams.addRule(ABOVE, R.id.main_creator_bottom);
        if (containerColor != -1) mContainer.setBackgroundColor(containerColor);

        mBottom = new SingleSelector(context);
        mBottom.setId(R.id.main_creator_bottom);
        LayoutParams bottomParams = new LayoutParams(LayoutParams.MATCH_PARENT, bottomHeight);
        bottomParams.addRule(ALIGN_PARENT_BOTTOM);
        if (bottomColor != -1) mBottom.setBackgroundColor(bottomColor);

        addView(mBottom, bottomParams);
        addView(mContainer, containerParams);
    }

    public MainCreator add(int index, INormalTab normalTab, Fragment fragment) {
        final LinearLayout layout = new LinearLayout(getContext());
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);

        ImageView icon = new ImageView(getContext());
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_selected}, ContextCompat.getDrawable(getContext(), normalTab.getIconSelectedId()));
        drawable.addState(new int[0], ContextCompat.getDrawable(getContext(), normalTab.getIconDefaultId()));
        icon.setImageDrawable(drawable);
        icon.setAdjustViewBounds(true);
        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);

        TextView text = new TextView(getContext());
        text.setGravity(Gravity.CENTER);
        text.setText(normalTab.getText());
        text.setTextColor(new ColorStateList(new int[][]{new int[]{android.R.attr.state_selected}, new int[0]}
                , new int[]{normalTab.getSelectedColor(), normalTab.getDefaultColor()}));

        layout.addView(icon);
        layout.addView(text);

        final MarginLayoutParams textParams = (MarginLayoutParams) text.getLayoutParams();
        textParams.topMargin = normalTab.getBetweenSize();
        textParams.bottomMargin = normalTab.getBottomSize();
        text.setLayoutParams(textParams);

        final LinearLayout.LayoutParams iconParams = (LinearLayout.LayoutParams) icon.getLayoutParams();
        iconParams.weight = 1;
        iconParams.topMargin = normalTab.getTopSize();
        icon.setLayoutParams(iconParams);

        return add(index, layout, fragment);
    }

    public MainCreator add(int index, int resId, Fragment fragment) {
        return add(index, LayoutInflater.from(getContext()).inflate(resId, mBottom, false), fragment);
    }

    public MainCreator add(int index, View tabView, Fragment fragment) {
        mBottom.removeUnnecessary(index);
        mBottom.addView(tabView);
        mFragmentArray.put(index, fragment);
        return this;
    }

    public void show(final FragmentManager fragmentManager) {
        if (mBottom.getCurrentIndex() >= 0) {
            showIndex(fragmentManager, mBottom.getCurrentIndex());
        }
        mBottom.setOnSelectorChangedListener(new SingleSelector.OnSelectorChangedListener() {
            @Override
            public void onSelectorChanged(int index) {
                showIndex(fragmentManager, index);
            }
        });
    }

    private void showIndex(FragmentManager fragmentManager, int index) {
        final Fragment fragment = mFragmentArray.get(index);
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.main_creator_container, fragment).commit();
        }
    }
}
