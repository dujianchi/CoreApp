package cn.dujc.coreapp;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import cn.dujc.core.permission.AppSettingsDialog;
import cn.dujc.core.ui.BaseFragment;
import cn.dujc.core.ui.StatusBarPlaceholder;
import cn.dujc.core.util.ToastUtil;

/**
 * @author du
 * date 2018/5/12 上午11:30
 */
public class MainFragment extends BaseFragment {

    private boolean tOn = false;
    private boolean fOn = false;
    private boolean lOn = true;
    private boolean pOn = true;

    @Override
    public int getViewId() {
        return R.layout.fragment_main;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        findViewById(R.id.translateSwitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTitleCompat().setTranslucentStatus(tOn = !tOn);
            }
        });
        findViewById(R.id.lightSwitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTitleCompat().setStatusBarMode(lOn = !lOn);
            }
        });
        findViewById(R.id.fitSwitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTitleCompat().setContentFits(fOn = !fOn);
            }
        });
        findViewById(R.id.placeholderSwitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StatusBarPlaceholder placeholder = (StatusBarPlaceholder)findViewById(R.id.sbp_placeholder);
                placeholder.placeholder(pOn = !pOn);
            }
        });
        findViewById(R.id.permissionSwitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionKeeper().requestPermissions(321, "权限设置2", "需要一些权限才能正常使用2", Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CONTACTS);
            }
        });
        findViewById(R.id.settingsSwitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AppSettingsDialog.Builder(MainFragment.this).setRationale("22222").build().show();
            }
        });
    }

    @Override
    public void onGranted(int requestCode, List<String> permissions) {
        super.onGranted(requestCode, permissions);
        ToastUtil.showToast(mActivity, "granted: ", permissions);
    }

    @Override
    public void onDenied(int requestCode, List<String> permissions) {
        super.onDenied(requestCode, permissions);
        ToastUtil.showToast(mActivity, "denied: ", permissions);
    }
}
