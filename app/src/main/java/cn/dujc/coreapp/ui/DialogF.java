package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.view.View;

import cn.dujc.core.ui.BaseDialogFragment;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;

public class DialogF extends BaseDialogFragment {

    @Override
    public int getViewId() {
        return R.layout.dialog_f;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        View app = findViewById(R.id.btn_app);
        app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mActivity, "click");
            }
        });app.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToastUtil.showToast(mActivity, "long click");
                return true;
            }
        });
    }

}
