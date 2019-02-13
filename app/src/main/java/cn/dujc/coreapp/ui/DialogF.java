package cn.dujc.coreapp.ui;

import android.content.Context;
import android.os.Bundle;

import cn.dujc.core.ui.BasePopupWindow;
import cn.dujc.coreapp.R;

public class DialogF extends BasePopupWindow {

    public DialogF(Context context) {
        super(context);
        setFocusable(true);
    }

    @Override
    public int getViewId() {
        return R.layout.dialog_f;
    }

   /* @Override
    public View getViewV() {
        NestedScrollView layout = new NestedScrollView(mContext.getApplicationContext());
        View view = LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.layout_empty_view, null);
        layout.addView(view);
        return layout;
    }*/

    @Override
    public void initBasic(Bundle savedInstanceState) {
        //setCanceledOnTouchOutside(false);
        /*TextView text = findViewById(R.id.tv_text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mContext, "aaa");
            }
        });*/
    }

   /* @Override
    public int _getWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int _getHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }*/
}
