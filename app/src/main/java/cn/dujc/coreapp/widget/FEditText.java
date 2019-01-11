package cn.dujc.coreapp.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import cn.dujc.core.impls.TextWatcherImpl;
import cn.dujc.coreapp.R;

/**
 * f**k EditText, add 'clear' and 'showPassword'
 */
public class FEditText extends FrameLayout {

    private boolean mAllSettle = false;//是否设置完了
    private boolean mPasswordVisible = false;//是否设置完了
    private EditText mEditText;

    private ImageView mIvClear;
    private ImageView mIvShow;

    public FEditText(@NonNull Context context) {
        this(context, null, 0);
    }

    public FEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (mEditText == null) {
            final EditText editText = findEditText(child);
            if (editText != null) {
                mEditText = editText;
                setupAtFirst();
            }
        }
    }

    private void setupAtFirst() {
        if (!mAllSettle) {
            synchronized (FEditText.class) {
                if (!mAllSettle) {
                    mAllSettle = true;
                    if (mEditText != null) {
                        mEditText.addTextChangedListener(new TextWatcherImpl() {
                            @Override
                            public void afterTextChanged(Editable s) {
                                if (s.length() > 0 && mEditText.isFocused()) {
                                    mIvClear.setVisibility(VISIBLE);
                                } else {
                                    mIvClear.setVisibility(GONE);
                                }
                            }
                        });
                        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus && mEditText != null && mEditText.getText().length() > 0) {
                                    mIvClear.setVisibility(VISIBLE);
                                } else {
                                    mIvClear.setVisibility(GONE);
                                }
                            }
                        });

                        LayoutInflater.from(getContext()).inflate(R.layout.widget_f_edit_text, this);
                        mIvClear = findViewById(R.id.iv_clear);
                        mIvShow = findViewById(R.id.iv_show);

                        mIvClear.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mEditText != null) mEditText.setText("");
                            }
                        });

                        final int inputType = mEditText.getInputType();
                        if ((inputType & EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD) == EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD
                                || (inputType & EditorInfo.TYPE_TEXT_VARIATION_PASSWORD) == EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
                                || (inputType & EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) == EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                                || (inputType & EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD) == EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD) {
                            mIvShow.setVisibility(VISIBLE);
                            mIvShow.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mPasswordVisible = !mPasswordVisible) {
                                        mIvShow.setImageResource(R.mipmap.show_password);
                                        mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                        mEditText.setSelection(mEditText.getText().length());
                                    } else {
                                        mIvShow.setImageResource(R.mipmap.hidden_password);
                                        mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                        mEditText.setSelection(mEditText.getText().length());
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    private EditText findEditText(View view) {
        if (view instanceof EditText) {
            return (EditText) view;
        } else if (view instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) view;
            for (int index = 0, count = group.getChildCount(); index < count; index++) {
                final View child = group.getChildAt(index);
                return findEditText(child);
            }
        }
        return null;
    }
}
