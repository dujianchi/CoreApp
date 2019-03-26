package cn.dujc.coreapp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.style.DynamicDrawableSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import cn.dujc.core.impls.LinkMovementMethodReplacement;
import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.util.TextColorBuilder;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;

/**
 * @author du
 * date: 2019/3/25 6:56 PM
 */
public class TextActivity extends BaseActivity {

    @Override
    public int getViewId() {
        return R.layout.activity_text;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        TextView text = findViewById(R.id.tv_text);
        LinkMovementMethodReplacement.assistTextView(text);
        Spannable spannable = new TextColorBuilder()
                .addTextPart("默认颜色后面换行 ")
                .addTextPart('\n')
                .addTextPart(mActivity, R.color.colorAccent, "colorAccent ")
                .addTextPart(Color.RED, "红色 ")
                .addTextPart("删除线 ", new StrikethroughSpan())
                .addTextPart("下划线 ", new UnderlineSpan())
                .addTextPart("下标 ", new SubscriptSpan())
                .addTextPart("上标 ", new SuperscriptSpan())
                .addTextPart("colorAccent带点击 ", mActivity, R.color.colorAccent, new TextColorBuilder.OnClickListener() {
                    @Override
                    public void onClick(View widget, CharSequence clickedText) {
                        ToastUtil.showToast(mActivity, "点中了" + clickedText);
                    }
                })
                .addTextPart("蓝色带点击 ", Color.BLUE, new TextColorBuilder.OnClickListener() {
                    @Override
                    public void onClick(View widget, CharSequence clickedText) {
                        ToastUtil.showToast(mActivity, "点中了" + clickedText);
                    }
                })
                .addTextPartPx("20px ", 20)
                .addTextPartDp("20dp ", 20)
                .addTextPartScale("一半 ", 0.5f)
                .addTextPartScaleX("2倍宽 ", 2f)
                .addTextPart("后面会有4个图片")
                .addImage(mActivity, R.mipmap.ic_launcher)
                .addImage(ContextCompat.getDrawable(mActivity, R.mipmap.ic_launcher))
                .addImage(ContextCompat.getDrawable(mActivity, R.mipmap.ic_launcher), DynamicDrawableSpan.ALIGN_BOTTOM)
                .addImage(ContextCompat.getDrawable(mActivity, R.mipmap.ic_launcher), 20, 20, DynamicDrawableSpan.ALIGN_BASELINE)
                .addTextPart("后面很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长")
                .build();
        text.setText(spannable);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mActivity, "整个点中了");
            }
        });
    }
}
