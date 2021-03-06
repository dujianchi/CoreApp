package cn.dujc.coreapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import cn.dujc.core.bridge.ActivityStackUtil;
import cn.dujc.core.ui.BaseActivity;
import cn.dujc.coreapp.R;

/**
 * @author du
 * date 2018/10/31 9:46 PM
 */
public class ReturnActivity extends BaseActivity {

    public static final String KEY = "KEY";

    private EditText mData;

    @Override
    public int getViewId() {
        return R.layout.activity_return;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        mData = findViewById(R.id.et_data);
        System.out.println("-- 开始接受值  " + System.currentTimeMillis());
        String text = extras().getLargeData(KEY, "");
        System.out.println("-- 接受到值  " + System.currentTimeMillis() + "  " + text);
        mData.setText(text);
        System.out.println("-- 显示到文本框  " + System.currentTimeMillis());
        findViewById(R.id.btn_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent data = new Intent();
                data.putExtra(KEY, mData.getText().toString());
                setResult(RESULT_OK, data);
                finish();
            }
        });
        findViewById(R.id.btn_finish_other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityStackUtil.getInstance().closeAllExcept(ReturnActivity.class);
            }
        });
    }
}
