package cn.dujc.coreapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.ToastUtil;

/**
 * Created by du on 2018/2/14.
 */
public class Activity3 extends BaseActivity{

    @Override
    public View getViewV() {
        TextView view = new TextView(this);
        view.setText("activity 3");
        return view;
    }

    @Override
    public int getViewId() {
        return 0;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int go = starter().go(Activity4.class);
                System.out.println("3 go = " + go);
            }
        });
        String string = extras().get("123",null, String.class);
        if (string != null) ToastUtil.showToast(mActivity, string);
        setResult(RESULT_OK);
    }
}
