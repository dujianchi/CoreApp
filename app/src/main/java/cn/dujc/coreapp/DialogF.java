package cn.dujc.coreapp;

import android.os.Bundle;
import android.view.Gravity;

import cn.dujc.core.ui.BaseDialogFragment;

/**
 * @author du
 * date 2018/7/8 上午11:18
 */
public class DialogF extends BaseDialogFragment {

    boolean mBottom = false;

    @Override
    public int getViewId() {
        return R.layout.edit_dialog;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public int dialogGravity() {
        return (mBottom = !mBottom) ? Gravity.BOTTOM : Gravity.CENTER;
    }

//    @Override
//    public int dialogHeight() {
//        return ViewGroup.LayoutParams.MATCH_PARENT;
//    }
}
