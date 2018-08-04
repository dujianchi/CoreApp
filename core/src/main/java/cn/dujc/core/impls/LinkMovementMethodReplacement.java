package cn.dujc.core.impls;

import android.os.Handler;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * LinkMovementMethod.getInstance()的替换，可以解决LinkMovementMethod没有截断的bug
 * @author du
 * date 2018/7/27 上午10:03
 */
public class LinkMovementMethodReplacement implements View.OnTouchListener {

    public interface ITextView {
        TextView textView();
    }

    public static class TextViewImpl implements ITextView {
        private final TextView mTextView;

        public TextViewImpl(TextView textView) {
            mTextView = textView;
        }

        @Override
        public TextView textView() {
            return mTextView;
        }
    }

    public static LinkMovementMethodReplacement assistTextView(TextView textView) {
        return assistTextView(new TextViewImpl(textView));
    }

    public static LinkMovementMethodReplacement assistTextView(ITextView textView) {
        return new LinkMovementMethodReplacement(textView);
    }

    private final static long LONG_CLICK_INTERNAL = 2500;

    private final ITextView mTextView;
    private final AtomicBoolean mLongClicked = new AtomicBoolean(false);
    private final Handler mLongClickHandler = new Handler();
    private final Runnable mLongClickRunnable = new Runnable() {
        @Override
        public void run() {
            if (mTextView != null && mTextView.textView() != null) {
                mLongClicked.set(mTextView.textView().performLongClick());
            }
        }
    };

    private LinkMovementMethodReplacement(ITextView textView) {
        mTextView = textView;
        final TextView text = mTextView.textView();
        if (text != null) {
            text.setOnTouchListener(this);
        }
    }

    private void resetLongClick() {
        mLongClicked.set(false);
        mLongClickHandler.removeCallbacks(mLongClickRunnable);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            resetLongClick();
            mLongClickHandler.postDelayed(mLongClickRunnable, LONG_CLICK_INTERNAL);
        } else {
            final TextView tv = (TextView) v;
            final CharSequence text = tv.getText();
            if (action == MotionEvent.ACTION_UP) {
                if (!mLongClicked.get()) {
                    if (text instanceof Spanned) {
                        int x = (int) event.getX();
                        int y = (int) event.getY();

                        x -= tv.getTotalPaddingLeft();
                        y -= tv.getTotalPaddingTop();

                        x += tv.getScrollX();
                        y += tv.getScrollY();

                        Layout layout = tv.getLayout();
                        int line = layout.getLineForVertical(y);
                        int off = layout.getOffsetForHorizontal(line, x);

                        ClickableSpan[] link = ((Spanned) text).getSpans(off, off, ClickableSpan.class);
                        if (link.length != 0) {
                            link[0].onClick(tv);
                        } else {
                            resetLongClick();
                            return tv.performClick();
                        }
                    } else {
                        resetLongClick();
                        return tv.performClick();
                    }
                }
                resetLongClick();
            }
        }
        return true;
    }
}
