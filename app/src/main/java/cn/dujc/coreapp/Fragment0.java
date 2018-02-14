package cn.dujc.coreapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.dujc.core.ui.BaseFragment;

/**
 * Created by du on 2018/2/14.
 */
public class Fragment0 extends BaseFragment {
    @Override
    public int getViewId() {
        return 0;
    }

    @Override
    public View getViewV() {
        TextView textView = new TextView(mActivity);
        textView.setText("0");
        return textView;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {

    }
}
