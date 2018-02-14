package cn.dujc.coreapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cn.dujc.core.widget.RadioMenu;

/**
 * Created by du on 2018/2/14.
 */
public class ItemView implements RadioMenu.Item {
    private TextView mTextView;
    private String text;
    private boolean isSelect = false;

    public ItemView(Context context, String text) {
        mTextView = new TextView(context);
        this.text = text;
    }

    @Override
    public ItemView initView() {
        mTextView.setText(text);
        mTextView.setGravity(Gravity.CENTER);
        return this;
    }

    @Override
    public ItemView onDefault(View itemView) {
        mTextView.setTextColor(ContextCompat.getColor(mTextView.getContext(), R.color.colorPrimary));
        return this;
    }

    @Override
    public ItemView onSelected(View itemView) {
        mTextView.setTextColor(ContextCompat.getColor(mTextView.getContext(), R.color.colorAccent));
        return this;
    }

    @Override
    public ItemView setSelected(boolean isSelected) {
        isSelect = isSelected;
        return this;
    }

    @Override
    public boolean isSelected() {
        return isSelect;
    }

    @Override
    public View getView() {
        return mTextView;
    }

}
