package cn.dujc.coreapp;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import cn.dujc.core.permission.AppSettingsDialog;
import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.ui.StatusBarPlaceholder;
import cn.dujc.core.util.ToastUtil;

public class MainActivity extends BaseActivity {

    @Override
    public int getViewId() {
        return R.layout.activity_main_dialog;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        setTitle("asdf");
        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MainFragment())
                .commit();*/
    }

    private DialogF mDialog;
    public void dialog(View v) {
        if (mDialog == null){
            mDialog = new DialogF();
        }
        mDialog.showOnly(this);
    }

    private boolean tOn = true;
    private boolean fOn = false;
    private boolean lOn = true;
    private boolean pOn = true;

    public void translateSwitch(View v) {
        tOn = !tOn;
        getTitleCompat().setTranslucentStatus(tOn);
    }

    public void lightSwitch(View v) {
        lOn = !lOn;
        ToastUtil.showToast(mActivity, lOn);
        getTitleCompat().setStatusBarMode(lOn);
    }

    public void fitSwitch(View v) {
        fOn = !fOn;
        getTitleCompat().setContentFits(fOn);
    }

    public void placeholderSwitch(View v) {
        pOn = !pOn;
        ((StatusBarPlaceholder) findViewById(R.id.sbp_placeholder)).placeholder(pOn);
    }

    public void permissionSwitch(View v) {
        permissionKeeper().requestPermissions(123
                , "权限设置"
                , "需要一些权限才能正常使用"
                , Manifest.permission.WRITE_EXTERNAL_STORAGE);
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

    public void settingsSwitch(View v) {
        new AppSettingsDialog.Builder(this)
                .setRationale("123321")
                .build()
                .show();
    }
}
