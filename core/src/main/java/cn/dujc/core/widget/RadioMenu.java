package cn.dujc.core.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 单选菜单
 * Created by du on 2018/1/28.
 */
public class RadioMenu extends LinearLayout {

    public interface Item {
        public View getView();

        public void onDefault();

        public void onSelected();

        public boolean isSelected();
    }

    private final List<Item> mItems = new ArrayList<>();
    private Item mLastItem = null;

    public RadioMenu(Context context) {
        this(context, null, 0);
    }

    public RadioMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadioMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RadioMenu addItem(Item item) {
        return addItem(item, false);
    }

    public RadioMenu addItem(Item item, boolean isSelected) {
        mItems.add(item);
        addItemChild(item, isSelected);
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

    private void addItemChild(Item item, boolean isSelected) {
        addView(item.getView());
        if (isSelected) {
            selectItem(item);
        }
    }

    private void selectItem(Item item) {
        item.getView().setSelected(true);
        item.onSelected();
        if (mLastItem != null) {
            mLastItem.getView().setSelected(false);
            mLastItem.onDefault();
        }
        mLastItem = item;
    }
}
