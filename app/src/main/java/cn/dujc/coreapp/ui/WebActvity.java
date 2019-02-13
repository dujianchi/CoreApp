package cn.dujc.coreapp.ui;

import android.os.Bundle;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.ui.BaseWebFragment;
import cn.dujc.coreapp.R;

public class WebActvity extends BaseActivity {

    private BaseWebFragment mFragment;

    @Override
    public int getViewId() {
        return R.layout.activity_web;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mFragment = BaseWebFragment.newInstance("baidu", "https://www.baidu.com/s?wd=js%20%E5%92%8C%20Android%20%E4%BA%A4%E4%BA%92");
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, mFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null && mFragment.onBackPressed()) return;
        super.onBackPressed();
    }
}
