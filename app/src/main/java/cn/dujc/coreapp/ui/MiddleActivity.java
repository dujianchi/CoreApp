package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.ui.FragmentShellActivity;

/**
 * @author du
 * date 2018/10/31 9:55 PM
 */
public class MiddleActivity extends BaseActivity {

    @Override
    public int getViewId() {
        return 0;
    }

    @Nullable
    @Override
    public View getViewV() {
        final LinearLayout layout = new LinearLayout(mActivity);
        final Button button = new Button(mActivity);
        button.setText("跳过，然后结束我自己");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starter().go(FragmentShellActivity.load(mActivity, SomeFragment.class), true);
            }
        });
        layout.addView(button);
        return layout;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {

    }
}
