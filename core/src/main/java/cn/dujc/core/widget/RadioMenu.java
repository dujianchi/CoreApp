package cn.dujc.core.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 单选菜单
 * Created by du on 2018/1/28.
 */
public class RadioMenu extends LinearLayout {

    public interface Item {
        Item initView();

        Item onDefault(View itemView);

        Item onSelected(View itemView);

        Item setSelected(boolean isSelected);

        boolean isSelected();

        View getView();
    }

    public interface OnItemSelectedChangeListener {
        void onItemSelected(Item item);

        void onItemUnselected(Item item);
    }

    private final LayoutParams mDefaultLayoutParams
            = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
    private final List<Item> mItems = new ArrayList<>();
    private Item mLastItem = null;
    private OnItemSelectedChangeListener mOnItemSelectedChangeListener = null;

    public RadioMenu(Context context) {
        this(context, null, 0);
    }

    public RadioMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadioMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final float rawX = ev.getRawX();
        final float rawY = ev.getRawY();
        for (Item item : mItems) {
            final int[] lIS = new int[2];//location in screen，在屏幕中的位置
            final View view = item.getView();
            view.getLocationOnScreen(lIS);
            if (rawX >= lIS[0] && rawX <= lIS[0] + view.getWidth() && rawY >= lIS[1] && rawY <= lIS[1] + view.getHeight()) {
                selectItem(item);
                break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOnItemSelectedChangeListener(OnItemSelectedChangeListener onItemSelectedChangeListener) {
        mOnItemSelectedChangeListener = onItemSelectedChangeListener;
    }

    private void addItemChild(Item item, boolean isSelected) {
        item.initView();
        final View itemView = item.getView();
        if (isSelected) {
            selectItem(item);
        } else {
            item.onDefault(itemView);
        }
        addView(itemView, mDefaultLayoutParams);
    }

    private void selectItem(Item item) {
        if (item == mLastItem) return;
        if (mLastItem != null) {
            mLastItem.setSelected(false);
            mLastItem.onDefault(item.getView());
            if (mOnItemSelectedChangeListener != null) {
                mOnItemSelectedChangeListener.onItemUnselected(mLastItem);
            }
        }
        item.setSelected(true);
        item.onSelected(item.getView());
        if (mOnItemSelectedChangeListener != null) {
            mOnItemSelectedChangeListener.onItemSelected(item);
        }
        mLastItem = item;
    }

    public RadioMenu addItem(Item item) {
        mItems.add(item);
        addItemChild(item, item.isSelected());
        return this;
    }

    public RadioMenu removeItem(Item item) {
        mItems.remove(item);
        removeView(item.getView());
        return this;
    }

    public RadioMenu removeItem(int index) {
        mItems.remove(index);
        removeViewAt(index);
        return this;
    }

    public RadioMenu clearItems() {
        mItems.clear();
        removeAllViews();
        return this;
    }

}
