package cn.dujc.coreapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.dujc.core.ui.BaseActivity;

/**
 * Created by du on 2018/2/14.
 */
public class Activity2 extends BaseActivity {

    @Override
    public View getViewV() {
        TextView view = new TextView(this);
        view.setText("activity 2");
        return view;
    }

    @Override
    public int getViewId() {
        return 0;
    }

    boolean on = true;

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final int go = starter().with("123", "123").go(Activity3.class);
                //System.out.println("2 go = " + go);
                mTitleCompat.setContentFits(false).setFakeStatusBarColorId(R.color.colorAccent).setTranslucentStatus(on = !on);
            }
        });
    }
}
