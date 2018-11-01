package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.coreapp.R;

/**
 * @author du
 * date 2018/11/1 11:36 AM
 */
public class CoordinatorActivity extends BaseActivity {

    @Override
    public int getViewId() {
        return R.layout.activity_coordinator;
    }

    @Nullable
    @Override
    public ViewGroup initToolbar(ViewGroup parent) {
        return (ViewGroup) LayoutInflater.from(mActivity).inflate(R.layout.toolbar_coordinator, parent, false);
    }

    @Override
    protected STYLE toolbarStyle() {
        return STYLE.COORDINATOR;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {

    }
}
