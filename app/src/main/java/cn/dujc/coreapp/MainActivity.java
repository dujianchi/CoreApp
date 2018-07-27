package cn.dujc.coreapp;

import android.Manifest;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.impls.LinkMovementMethodReplacement;
import cn.dujc.core.permission.AppSettingsDialog;
import cn.dujc.core.ui.BaseActivity;
import cn.dujc.core.ui.StatusBarPlaceholder;
import cn.dujc.core.util.GodDeserializer;
import cn.dujc.core.util.GsonUtil;
import cn.dujc.core.util.LogUtil;
import cn.dujc.core.util.TextColorBuilder;
import cn.dujc.core.util.ToastUtil;

public class MainActivity extends BaseActivity {

    @Override
    public int getViewId() {
        return R.layout.activity_main_dialog;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        setTitle("asdf");
        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MainFragment())
                .commit();*/
        //staggeredGrid();

//        final String text = "abcdaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbccccccccccccccccccc";
//        final Spannable string = new SpannableStringBuilder(text);
////        string.setSpan(new ImageSpan(ContextCompat.getDrawable(mActivity, R.mipmap.ic_launcher))
////                , 2, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        string.setSpan(new MyClickSpan("abcd"),0,4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        string.setSpan(new MyClickSpan("aaaa"),10,14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView textView = findViewById(R.id.textView);

        textView.setText(new TextColorBuilder()
                .addTextPart("没有颜色且没有点击")
                .addTextPart(",")
                .addTextPart(Color.RED, "红色没有点击")
                .addTextPart(",")
                .addTextPart("黑色有点击", Color.BLACK, new TextColorBuilder.OnClickListener() {
                    @Override
                    public void onClick(View widget, CharSequence clickedText) {
                        ToastUtil.showToast(mActivity, clickedText);
                    }
                })
                .addTextPart(",")
                .addTextPart("没有颜色有点击", 0, new TextColorBuilder.OnClickListener() {
                    @Override
                    public void onClick(View widget, CharSequence clickedText) {
                        ToastUtil.showToast(mActivity, clickedText);
                    }
                })
                .addTextPart(",")
                .addTextPart("有颜色有点击但没回调", Color.GREEN, null)
                .addTextPart(",")
                .addTextPart("没有颜色且没有点击")
                .build());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(mActivity, "textview clicked");
                LogUtil.d("textview clicked");
            }
        });
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToastUtil.showToast(mActivity, "textview long clicked");
                LogUtil.d("textview long clicked");
                return true;
            }
        });
        LinkMovementMethodReplacement.assistTextView(textView);
//        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private static class MyClickSpan extends ClickableSpan {

        private String tag;
        public MyClickSpan(String tag){
            this.tag = tag;
        }
        @Override
        public void onClick(View widget) {
            ToastUtil.showToast(widget.getContext(), tag+" is clicked");
            LogUtil.d("textview tag clicked");
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.RED);
        }
    }

    private void staggeredGrid() {
        final Random random = new Random();
        final List<String> list = new ArrayList<>();
        for (int index = 0; index < 100; index++) {
            StringBuilder text = new StringBuilder();
            for (int textIndex = 0, textLength = random.nextInt(4) + 1; textIndex < textLength; textIndex++) {
                text.append((char) ('a' + random.nextInt('z' - 'a')));
            }
            list.add(text.toString());
        }
        RecyclerView rv_list = findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rv_list.setAdapter(new BaseAdapter<String>(R.layout.item_staggered, list) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                final TextView itemView = (TextView) helper.itemView;
                itemView.setText(item);
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams();
                if (layoutParams == null) {
                    layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                }
                layoutParams.setFullSpan(helper.getAdapterPosition() % 7 == 0);
            }
        });
    }

    private DialogF mDialog;

    public void dialog(View v) {
        if (mDialog == null) {
            mDialog = new DialogF();
        }
        mDialog.showOnly(this);

        Bean2 bean2 = new Bean2();
        Bean bean = new Bean().init();
        bean2._bean = bean;
        bean2._beanArray = new Bean[]{bean, bean};
        bean2._beanList = Arrays.asList(bean);
        bean2._beanArrayList = new ArrayList<>(bean2._beanList);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Bean2.class, new GodDeserializer<Bean2>())
                .create();
        final String json = GsonUtil.toJsonString(bean2)
//        // normal
//        "{\"_bean\":{\"_Byte\":1,\"_Character\":\"3\",\"_Double\":6.0,\"_Float\":5.0,\"_Integer\":4,\"_Short\":2,\"_String\":\"7\",\"_byte\":1,\"_char\":\"3\",\"_double\":6.0,\"_float\":5.0,\"_int\":4,\"_short\":2},\"_beanArray\":[{\"_Byte\":1,\"_Character\":\"3\",\"_Double\":6.0,\"_Float\":5.0,\"_Integer\":4,\"_Short\":2,\"_String\":\"7\",\"_byte\":1,\"_char\":\"3\",\"_double\":6.0,\"_float\":5.0,\"_int\":4,\"_short\":2}],\"_beanArrayList\":[{\"_Byte\":1,\"_Character\":\"3\",\"_Double\":6.0,\"_Float\":5.0,\"_Integer\":4,\"_Short\":2,\"_String\":\"7\",\"_byte\":1,\"_char\":\"3\",\"_double\":6.0,\"_float\":5.0,\"_int\":4,\"_short\":2}],\"_beanList\":[{\"_Byte\":1,\"_Character\":\"3\",\"_Double\":6.0,\"_Float\":5.0,\"_Integer\":4,\"_Short\":2,\"_String\":\"7\",\"_byte\":1,\"_char\":\"3\",\"_double\":6.0,\"_float\":5.0,\"_int\":4,\"_short\":2}]}"
//        // array error
//        "{\"_bean\":{\"_Byte\":1,\"_Character\":\"3\",\"_Double\":6.0,\"_Float\":5.0,\"_Integer\":4,\"_Short\":2,\"_String\":\"7\",\"_byte\":1,\"_char\":\"3\",\"_double\":6.0,\"_float\":5.0,\"_int\":4,\"_short\":2},\"_beanArray\":\"\",\"_beanArrayList\":[{\"_Byte\":1,\"_Character\":\"3\",\"_Double\":6.0,\"_Float\":5.0,\"_Integer\":4,\"_Short\":2,\"_String\":\"7\",\"_byte\":1,\"_char\":\"3\",\"_double\":6.0,\"_float\":5.0,\"_int\":4,\"_short\":2}],\"_beanList\":[{\"_Byte\":1,\"_Character\":\"3\",\"_Double\":6.0,\"_Float\":5.0,\"_Integer\":4,\"_Short\":2,\"_String\":\"7\",\"_byte\":1,\"_char\":\"3\",\"_double\":6.0,\"_float\":5.0,\"_int\":4,\"_short\":2}]}"
//        // list error
//        "{\"_bean\":{\"_Byte\":1,\"_Character\":\"3\",\"_Double\":6.0,\"_Float\":5.0,\"_Integer\":4,\"_Short\":2,\"_String\":\"7\",\"_byte\":1,\"_char\":\"3\",\"_double\":6.0,\"_float\":5.0,\"_int\":4,\"_short\":2},\"_beanArray\":[{\"_Byte\":1,\"_Character\":\"3\",\"_Double\":6.0,\"_Float\":5.0,\"_Integer\":4,\"_Short\":2,\"_String\":\"7\",\"_byte\":1,\"_char\":\"3\",\"_double\":6.0,\"_float\":5.0,\"_int\":4,\"_short\":2}],\"_beanArrayList\":[{\"_Byte\":1,\"_Character\":\"3\",\"_Double\":6.0,\"_Float\":5.0,\"_Integer\":4,\"_Short\":2,\"_String\":\"7\",\"_byte\":1,\"_char\":\"3\",\"_double\":6.0,\"_float\":5.0,\"_int\":4,\"_short\":2}],\"_beanList\":\"\"}"
                //GsonUtil.toJsonString(bean2)
                ;
        System.err.println(json);
        final Bean2 fromJson = gson.fromJson(json, Bean2.class);
        System.out.println(GsonUtil.toJsonString(fromJson));
    }

    private boolean tOn = true;
    private boolean fOn = false;
    private boolean lOn = true;
    private boolean pOn = true;

    public void translateSwitch(View v) {
        tOn = !tOn;
        getTitleCompat().setTranslucentStatus(tOn);
    }

    public void lightSwitch(View v) {
        lOn = !lOn;
        ToastUtil.showToast(mActivity, lOn);
        getTitleCompat().setStatusBarMode(lOn);
    }

    public void fitSwitch(View v) {
        fOn = !fOn;
        getTitleCompat().setContentFits(fOn);
    }

    public void placeholderSwitch(View v) {
        pOn = !pOn;
        ((StatusBarPlaceholder) findViewById(R.id.sbp_placeholder)).placeholder(pOn);
    }

    public void permissionSwitch(View v) {
        permissionKeeper().requestPermissions(123
                , "权限设置"
                , "需要一些权限才能正常使用"
                , Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onGranted(int requestCode, List<String> permissions) {
        super.onGranted(requestCode, permissions);
        ToastUtil.showToast(mActivity, "granted: ", permissions);
    }

    @Override
    public void onDenied(int requestCode, List<String> permissions) {
        super.onDenied(requestCode, permissions);
        ToastUtil.showToast(mActivity, "denied: ", permissions);
    }

    public void settingsSwitch(View v) {
        new AppSettingsDialog.Builder(this)
                .setRationale("123321")
                .build()
                .show();
    }

    public static final class VerticalImageSpan extends ImageSpan {

        public VerticalImageSpan(Drawable drawable) {
            super(drawable);
        }

        public int getSize(Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fontMetricsInt) {
            Drawable drawable = getDrawable();
            Rect rect = drawable.getBounds();
            if (fontMetricsInt != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;
                int drHeight = rect.bottom - rect.top;

                //对于这里我表示,我不知道为啥是这样。不应该是fontHeight/2?但是只有fontHeight/4才能对齐
                //难道是因为TextView的draw的时候top和bottom是大于实际的？具体请看下图
                //所以fontHeight/4是去除偏差?
                int top = drHeight / 2 - fontHeight / 4;
                int bottom = drHeight / 2 + fontHeight / 4;

                fontMetricsInt.ascent = -bottom;
                fontMetricsInt.top = -bottom;
                fontMetricsInt.bottom = top;
                fontMetricsInt.descent = top;
            }
            return rect.right;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end,
                         float x, int top, int y, int bottom, Paint paint) {
            Drawable drawable = getDrawable();
            canvas.save();
            int transY = 0;
            //获得将要显示的文本高度-图片高度除2等居中位置+top(换行情况)
            transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
            canvas.translate(x, transY);
            drawable.draw(canvas);
            canvas.restore();
        }
    }
}
