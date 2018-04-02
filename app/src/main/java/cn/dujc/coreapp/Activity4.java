package cn.dujc.coreapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.dujc.core.ui.BaseFragment;
import cn.dujc.core.ui.BaseRefreshableActivity;

/**
 * Created by du on 2018/2/14.
 */
public class Activity4 extends BaseRefreshableActivity {

    @Override
    public int getViewId() {
        return R.layout.activity_4;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_container, new Fragment4())
                .commit();
    }

    @Override
    public void onRefresh() {
        refreshDone();
    }

    public static final class Fragment4 extends BaseFragment {

        @Override
        public View getViewV() {
            TextView view = new TextView(mActivity);
            view.setText("activity 4.5");
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
                    final int go = starter().go(Activity2.class);
                    System.out.println("4 go = " + go);
                }
            });
        }
    }
}
