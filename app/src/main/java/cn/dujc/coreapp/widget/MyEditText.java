package cn.dujc.coreapp.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import cn.dujc.coreapp.ui.EditTextActivity;

/**
 * @author du
 * date: 2019/3/13 6:27 PM
 */
public class MyEditText extends AppCompatEditText implements EditTextActivity.StatusChange {

    public MyEditText(Context context) {
        this(context, null, 0);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EditTextActivity.UnitStatus.getInstance().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EditTextActivity.UnitStatus.getInstance().unregister(this);
    }

    @Override
    public void enable(boolean yes) {
        setEnabled(yes);
    }
}
