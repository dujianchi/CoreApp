package cn.dujc.coreapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.dujc.core.ui.BaseFragment;
import cn.dujc.core.util.LogUtil;
import cn.dujc.core.util.ToastUtil;

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
        textView.setText("10000000");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (starter().getRequestCode(Activity3.class) == requestCode){
            ToastUtil.showToast(mActivity, "return fragment");
            LogUtil.d("return fragment");
        }
    }
}
