package cn.dujc.coreapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import cn.dujc.coreapp.R;

public class GoodsNumber extends FrameLayout {

    private Integer mLeftTextColor, mMiddleTextColor, mRightTextColor;
    private Integer mLeftTextSize, mMiddleTextSize, mRightTextSize;
    private Integer mLeftBackground, mMiddleBackground, mRightBackground;

    public GoodsNumber(Context context) {
        this(context, null, 0);
    }

    public GoodsNumber(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodsNumber(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_goods_number, this, true);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GoodsNumber);
        array.getResourceId(R.styleable.GoodsNumber_leftBackground,0);
        array.recycle();
    }
}
