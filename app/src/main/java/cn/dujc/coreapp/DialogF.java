package cn.dujc.coreapp;

import android.os.Bundle;

import cn.dujc.core.ui.BaseDialogFragment;

/**
 * @author du
 * date 2018/7/8 上午11:18
 */
public class DialogF extends BaseDialogFragment {

    @Override
    public int getViewId() {
        return R.layout.edit_dialog;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);
    }
}
