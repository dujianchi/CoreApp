package cn.dujc.coreapp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.dujc.coreapp.R;

/**
 * @author du
 * date: 2018/11/29 1:16 PM
 */
public class BulgeLineView extends View {

    private int mLineWide = 1, mBulgeHeight = 0, mBulgeSize = 0, mBulgePosition = 0;//Dimension
    private int mPositionGravity = 0;//Enum
    private int mLineColor = Color.TRANSPARENT, mBulgeBackground = Color.TRANSPARENT;//Color
    private final Path mBulgePath = new Path();
    private final Paint mLinePaint, mBulgePaint;
    private final float[] mXys = new float[16];

    public BulgeLineView(Context context) {
        this(context, null, 0);
    }

    public BulgeLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BulgeLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BulgeLineView);
            mLineWide = typedArray.getDimensionPixelOffset(R.styleable.BulgeLineView_blvLineWide, mLineWide);
            mBulgeHeight = typedArray.getDimensionPixelOffset(R.styleable.BulgeLineView_blvBulgeHeight, mBulgeHeight);
            mBulgeSize = typedArray.getDimensionPixelOffset(R.styleable.BulgeLineView_blvBulgeSize, mBulgeSize);
            mBulgePosition = typedArray.getDimensionPixelOffset(R.styleable.BulgeLineView_blvBulgePosition, mBulgePosition);

            mPositionGravity = typedArray.getInt(R.styleable.BulgeLineView_blvPositionGravity, mPositionGravity);
            mLineColor = typedArray.getColor(R.styleable.BulgeLineView_blvLineColor, mLineColor);
            mBulgeBackground = typedArray.getColor(R.styleable.BulgeLineView_blvBulgeBackground, mBulgeBackground);
            typedArray.recycle();
        }
        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(mLineWide);
        mLinePaint.setAntiAlias(true);

        mBulgePaint = new Paint();
        mBulgePaint.setColor(mBulgeBackground);
        mBulgePaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec
                , MeasureSpec.makeMeasureSpec(mLineWide + mBulgeHeight + getPaddingTop() + getPaddingBottom()
                        , MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int measuredWidth = getMeasuredWidth();
        mXys[0] = getPaddingLeft();
        mXys[1] = getPaddingTop() + mBulgeHeight;
        if (mPositionGravity == 0) {
            mXys[2] = mXys[0] + mBulgePosition;
            mXys[3] = mXys[1];
        } else {
            mXys[2] = measuredWidth - getPaddingRight() - mBulgeSize - mBulgePosition;
            mXys[3] = mXys[1];
        }
        mXys[4] = mXys[2];
        mXys[5] = mXys[3];
        mXys[6] = mXys[4] + mBulgeSize / 2.0f;
        mXys[7] = getPaddingTop();

        mXys[8] = mXys[6];
        mXys[9] = mXys[7];
        mXys[10] = mXys[8] + mBulgeSize / 2.0f;
        mXys[11] = mXys[5];

        mXys[12] = mXys[10];
        mXys[13] = mXys[11];
        mXys[14] = measuredWidth - getPaddingRight();
        mXys[15] = mXys[13];

        canvas.drawLines(mXys, mLinePaint);

        if (mBulgeBackground != Color.TRANSPARENT) {
            mBulgePath.moveTo(mXys[2], mXys[3] + mLineWide);
            mBulgePath.lineTo(mXys[6], mXys[7] + mLineWide);
            mBulgePath.lineTo(mXys[10], mXys[11] + mLineWide);
            mBulgePath.close();
            canvas.drawPath(mBulgePath, mBulgePaint);
            mBulgePath.reset();
        }
        super.onDraw(canvas);
    }

    public int getLineWide() {
        return mLineWide;
    }

    public void setLineWide(int lineWide) {
        mLineWide = lineWide;
    }

    public int getBulgeHeight() {
        return mBulgeHeight;
    }

    public void setBulgeHeight(int bulgeHeight) {
        mBulgeHeight = bulgeHeight;
    }

    public int getBulgeSize() {
        return mBulgeSize;
    }

    public void setBulgeSize(int bulgeSize) {
        mBulgeSize = bulgeSize;
    }

    public int getBulgePosition() {
        return mBulgePosition;
    }

    public void setBulgePosition(int bulgePosition) {
        mBulgePosition = bulgePosition;
    }

    public int getPositionGravity() {
        return mPositionGravity;
    }

    public void setPositionGravity(int positionGravity) {
        mPositionGravity = positionGravity;
    }

    public int getLineColor() {
        return mLineColor;
    }

    public void setLineColor(int lineColor) {
        mLineColor = lineColor;
    }

    public Paint getLinePaint() {
        return mLinePaint;
    }
}
