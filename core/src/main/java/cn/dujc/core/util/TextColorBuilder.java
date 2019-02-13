package cn.dujc.core.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

/**
 * @author du
 * date 2018/7/27 下午1:42
 */
public class TextColorBuilder {

    public interface OnClickListener {
        void onClick(View widget, CharSequence clickedText);
    }

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

    public Spannable build() {
        return mStringBuilder;
    }

    public TextColorBuilder addTextPart(char text) {
        mStringBuilder.append(text);
        return this;
    }

    public TextColorBuilder addTextPart(CharSequence text) {
        if (!TextUtils.isEmpty(text)) mStringBuilder.append(text);
        return this;
    }

    public TextColorBuilder addTextPart(Context context, int colorId, CharSequence text) {
        if (context == null || colorId == 0) return addTextPart(text);
        return addTextPart(ContextCompat.getColor(context, colorId), text);
    }

    public TextColorBuilder addTextPart(int color, CharSequence text) {
        if (!TextUtils.isEmpty(text)) {
            final int start = mStringBuilder.length();
            final int end = start + text.length();
            mStringBuilder.append(text);
            mStringBuilder.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    public TextColorBuilder addTextPart(CharSequence text, CharacterStyle characterStyle) {
        if (!TextUtils.isEmpty(text)) {
            final int start = mStringBuilder.length();
            final int end = start + text.length();
            mStringBuilder.append(text);
            mStringBuilder.setSpan(characterStyle, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }

    public TextColorBuilder addTextPart(CharSequence text, Context context, int colorId, OnClickListener listener) {
        return addTextPart(text, ContextCompat.getColor(context, colorId), listener);
    }

    public TextColorBuilder addTextPart(CharSequence text, int color, OnClickListener listener) {
        if (!TextUtils.isEmpty(text)) {
            final int start = mStringBuilder.length();
            final int end = start + text.length();
            mStringBuilder.append(text);
            mStringBuilder.setSpan(new TextClickableSpan(text, color, listener)
                    , start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return this;
    }
}
