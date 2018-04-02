package cn.dujc.coreapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.dujc.core.ui.BaseFragment;

/**
 * Created by du on 2018/2/14.
 */
public class Fragment1 extends BaseFragment implements View.OnClickListener {
    @Override
    public int getViewId() {
        return 0;
    }

    @Override
    public View getViewV() {
        TextView textView = new TextView(mActivity);
        textView.setText("1");
        textView.setOnClickListener(this);
        return textView;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
        starter().with("123","678").go(Activity3.class);
    }
}
