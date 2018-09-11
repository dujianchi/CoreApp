package cn.dujc.coreapp;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * @author du
 * date 2018/9/7 下午5:36
 */
public class Line extends View {

    private List<?> mData;

    public Line(Context context) {
        this(context, null, 0);
    }

    public Line(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Line(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(List<?> data) {
        mData = data;
    }

    public final int getDataCount() {
        return mData == null ? 0 : mData.size();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        final int count = getDataCount();
        if (count > 0){
            final int width = getWidth();
            final int height = getHeight();
        }
        super.onDraw(canvas);
    }
}
