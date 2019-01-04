package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.view.View;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.ui.StatusBarPlaceholder;
import cn.dujc.coreapp.R;

/**
 * @author du
 * date 2018/10/31 2:02 PM
 */
public class ToolbarCtrlActivity extends BaseActivity {

    private boolean mTransfer = true, mFit0 = true, mFit1 = false, mDark = false;

    @Override
    public int getViewId() {
        return R.layout.activity_toolbar_ctrl;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        setTitle("Toolbar控制");
    }

    public void transferSwitch(View v) {
        getTitleCompat().setTranslucentStatus(mTransfer = !mTransfer);
    }

    public void fitSwitch0(View v) {
        getTitleCompat().setContentFits(mFit0 = !mFit0);
    }

    public void fitSwitch1(View v) {
        final StatusBarPlaceholder placeholder = findViewById(R.id.toolbar_status_bar_placeholder);
        if (placeholder != null) {
            placeholder.setOverrideSystemOpen(true);
            placeholder.placeholder(mFit1 = !mFit1);
        }
    }

    public void darkSwitch(View v) {
        getTitleCompat().setStatusBarMode(mDark = !mDark);
    }
}
