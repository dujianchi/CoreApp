package cn.dujc.coreapp.ui;

import android.content.Context;
import android.os.Bundle;

import cn.dujc.core.ui.BaseDialog;
import cn.dujc.coreapp.R;

public class Dialog2 extends BaseDialog {

    public Dialog2(Context context) {
        super(context);
    }

    @Override
    public int getViewId() {
        return R.layout.dialog_f;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {

    }
}
