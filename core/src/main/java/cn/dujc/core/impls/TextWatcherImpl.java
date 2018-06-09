package cn.dujc.core.impls;

import android.text.TextWatcher;

/**
 * @author du
 * date 2018/6/9 下午1:36
 */
public abstract class TextWatcherImpl implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

}
