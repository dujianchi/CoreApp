package cn.dujc.coreapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author du
 * date 2018/8/29 下午5:58
 */
public class SB extends FrameLayout {

    public interface OnProgressCallback {
        void onProgress(float progress);
    }

    private final static int DEFAULT_SCALE = 0;//默认倍数，为0才能维持原值
    private float mY = 0;
    private float mSeekBarWidthScale;
    private float mSeekBarLeftMarginScale;
    private float mSeekBarTopMarginScale;
    private float mSeekBarRightMarginScale;
    private float mSeekBarBottomMarginScale;
    private int mSeekBarProgressColor;
    private Paint mPaint = new Paint();
    private RectF mRect;
    private OnProgressCallback mOnProgressCallback;
    private View mCursorView;

    public SB(Context context) {
        this(context, null, 0);
    }

    public SB(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SB(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SB);
            mSeekBarWidthScale = array.getFloat(R.styleable.SB_seekBarWidthScale, DEFAULT_SCALE);
            mSeekBarLeftMarginScale = array.getFloat(R.styleable.SB_seekBarLeftMarginScale, DEFAULT_SCALE);
            mSeekBarTopMarginScale = array.getFloat(R.styleable.SB_seekBarTopMarginScale, DEFAULT_SCALE);
            mSeekBarRightMarginScale = array.getFloat(R.styleable.SB_seekBarRightMarginScale, DEFAULT_SCALE);
            mSeekBarBottomMarginScale = array.getFloat(R.styleable.SB_seekBarBottomMarginScale, DEFAULT_SCALE);
            mSeekBarProgressColor = array.getColor(R.styleable.SB_seekBarProgressColor, Color.BLUE);
            array.recycle();
        }
        mPaint.setAntiAlias(true);
        mPaint.setColor(mSeekBarProgressColor);
        setClipChildren(false);
        //setClipToOutline(true);
        setClipToPadding(false);
    }

    public void setOnProgressCallback(OnProgressCallback onProgressCallback) {
        mOnProgressCallback = onProgressCallback;
    }

    public void setCursorView(View cursorView) {
        if (cursorView != null) {
            /*if (mCursorView != null) {
                removeView(mCursorView);
            }
            if (cursorView.getParent() != null) {
                ((ViewGroup) cursorView.getParent()).removeView(cursorView);
            }*/
            cursorView.setVisibility(INVISIBLE);
            //addView(cursorView);
            mCursorView = cursorView;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mRect != null) {
            canvas.drawRect(mRect, mPaint);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int height = getHeight();
        final int width = getWidth();
        final float bottom = height * mSeekBarBottomMarginScale;
        final float top = height * mSeekBarTopMarginScale;
        final float right = width * mSeekBarRightMarginScale;
        mY = event.getY();
        if (mY > height - bottom) {
            mY = height - bottom;
        } else {
            if (mY < top)
                mY = top;
        }
        mRect = new RectF(mSeekBarLeftMarginScale * width
                , mY
                , width - right
                , height - bottom);
        if (mOnProgressCallback != null) {
            mOnProgressCallback.onProgress((height - bottom - mY) / (height - top - bottom));
        }
        if (mCursorView != null) {
            if (mCursorView.getVisibility() != VISIBLE) {
                mCursorView.setVisibility(VISIBLE);
                //final LayoutParams params = (LayoutParams) mCursorView.getLayoutParams();
                //params.leftMargin = (int) (width - right);
                //mCursorView.setLayoutParams(params);
            }
            mCursorView.setTranslationY(mY);
        }
        postInvalidate();
        return true;
    }
}
