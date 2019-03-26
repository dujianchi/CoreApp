package cn.dujc.core.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.view.View;

/**
 * @author du
 * date 2018/7/27 下午1:42
 */
public class TextColorBuilder {

    public interface OnClickListener {
        void onClick(View widget, CharSequence clickedText);
    }

    /**
     * 点击事件，需要与{@link android.text.method.LinkMovementMethod}
     * 或{@link cn.dujc.core.impls.LinkMovementMethodReplacement}
     * 配合使用
     */
    public static class TextClickableSpan extends ClickableSpan {

        private final CharSequence mText;
        private final int mColor;
        private final OnClickListener mOnClickListener;

        public TextClickableSpan(CharSequence text, int color, OnClickListener onClickListener) {
            mText = text;
            mColor = color;
            mOnClickListener = onClickListener;
        }

        @Override
        public void onClick(View widget) {
            if (mOnClickListener != null) mOnClickListener.onClick(widget, mText);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            //super.updateDrawState(ds);
            if (mColor != 0) ds.setColor(mColor);
        }
    }

    private final SpannableStringBuilder mStringBuilder = new SpannableStringBuilder();

    /**
     * 返回spannable
     */
    public Spannable build() {
        return mStringBuilder;
    }

    /**
     * 添加一个char字符
     */
    public TextColorBuilder addTextPart(char text) {
        mStringBuilder.append(text);
        return this;
    }

    /**
     * 添加一个纯文本
     */
    public TextColorBuilder addTextPart(CharSequence text) {
        if (!TextUtils.isEmpty(text)) mStringBuilder.append(text);
        return this;
    }

    /**
     * 添加一个带颜色的文字
     */
    public TextColorBuilder addTextPart(Context context, int colorId, CharSequence text) {
        if (context == null || colorId == 0) return addTextPart(text);
        return addPart(text, new ForegroundColorSpan(ContextCompat.getColor(context, colorId)));
    }

    /**
     * 添加一个带颜色的文字
     */
    public TextColorBuilder addTextPart(int color, CharSequence text) {
        return addPart(text, new ForegroundColorSpan(color));
    }

    /**
     * 添加一个带文本样式的文字，比如删除线{@link android.text.style.StrikethroughSpan}
     * 、下划线{@link android.text.style.UnderlineSpan}
     * 、下标{@link android.text.style.SubscriptSpan}
     * 、上标{@link android.text.style.SuperscriptSpan}
     */
    public TextColorBuilder addTextPart(CharSequence text, CharacterStyle characterStyle) {
        return addPart(text, characterStyle);
    }

    /**
     * 添加一个带颜色和点击事件的文字
     */
    public TextColorBuilder addTextPart(CharSequence text, Context context, int colorId, OnClickListener listener) {
        return addPart(text, new TextClickableSpan(text, ContextCompat.getColor(context, colorId), listener));
    }

    /**
     * 添加一个带颜色和点击事件的文字
     */
    public TextColorBuilder addTextPart(CharSequence text, int color, OnClickListener listener) {
        return addPart(text, new TextClickableSpan(text, color, listener));
    }

    /**
     * 添加一个指定多少px大小的文字
     */
    public TextColorBuilder addTextPartPx(CharSequence text, int sizeInPx) {
        return addPart(text, new AbsoluteSizeSpan(sizeInPx));
    }

    /**
     * 添加一个指定多少dp大小的文字
     */
    public TextColorBuilder addTextPartDp(CharSequence text, int sizeInDp) {
        return addPart(text, new AbsoluteSizeSpan(sizeInDp, true));
    }

    /**
     * 添加一个指定倍数的文字，比如0.5即为设置的字体的一半大
     */
    public TextColorBuilder addTextPartScale(CharSequence text, float scale) {
        return addPart(text, new RelativeSizeSpan(scale));
    }

    /**
     * 添加一个指定宽度倍数的文字，比如2。0即为设置的字体的2倍宽
     */
    public TextColorBuilder addTextPartScaleX(CharSequence text, float scaleX) {
        return addPart(text, new ScaleXSpan(scaleX));
    }

    /**
     * 添加一个图片
     */
    public TextColorBuilder addImage(Context context, int drawableId) {
        if (drawableId == 0) return this;
        return addImage(ContextCompat.getDrawable(context, drawableId));
    }

    /**
     * 添加一个图片
     */
    public TextColorBuilder addImage(Drawable drawable) {
        if (drawable == null) return this;
        return addImage(drawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), DynamicDrawableSpan.ALIGN_BASELINE);
    }

    /**
     * 添加一个图片
     *
     * @param verticalAlignment 选择{@link android.text.style.DynamicDrawableSpan#ALIGN_BASELINE}
     *                          或{@link android.text.style.DynamicDrawableSpan#ALIGN_BOTTOM}
     */
    public TextColorBuilder addImage(Drawable drawable, int verticalAlignment) {
        if (drawable == null) return this;
        return addImage(drawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), verticalAlignment);
    }

    /**
     * 添加一个图片
     *
     * @param verticalAlignment 选择{@link android.text.style.DynamicDrawableSpan#ALIGN_BASELINE}
     *                          或{@link android.text.style.DynamicDrawableSpan#ALIGN_BOTTOM}
     */
    public TextColorBuilder addImage(Drawable drawable, int width, int height, int verticalAlignment) {
        if (drawable == null) return this;
        drawable.setBounds(0, 0, width, height);
        return addPart(" ", new ImageSpan(drawable, verticalAlignment));
    }

    /**
     * 添加一个文字，并指定span
     */
    public TextColorBuilder addPart(CharSequence text, Object span) {
        if (!TextUtils.isEmpty(text) && span != null) {
            final int start = mStringBuilder.length();
            final int end = start + text.length();
            mStringBuilder.append(text);
            mStringBuilder.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

}
