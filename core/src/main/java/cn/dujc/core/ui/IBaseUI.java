package cn.dujc.core.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;

import cn.dujc.core.util.LogUtil;

/**
 * 基本的UI类方法，包括activity和fragment
 * Created by du on 2018/2/14.
 */
public interface IBaseUI {
    /**
     * 自增的request code，每个跳转都是forresult的跳转，那么，今后只要记住跳转方法{@link #go(Class)}或{@link #go(Class, Bundle)}返回的int值
     * ，即为本次跳转产生的request code，从此不再管理request code，且不会再重复，因为不管在什么界面跳转，每次跳转都用了不同的request code（当然，崩溃重启的情况例外）
     */
    int[] _INCREMENT_REQUEST_CODE = {1};

    Toolbar initToolbar(ViewGroup parent);

    TitleCompat initTransStatusBar();

    View createRootView(View contentView);

    Starter starter();

    View getViewV();

    int getViewId();

    void initBasic(Bundle savedInstanceState);

    public interface Starter {
        int go(Class<? extends Activity> activity);

        Starter clear();

        Starter putAll(Bundle bundle);

        Starter with(String key, String param);

        Starter with(String key, byte param);

        Starter with(String key, char param);

        Starter with(String key, short param);

        Starter with(String key, float param);

        Starter with(String key, CharSequence param);

        Starter with(String key, Parcelable param);

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        Starter with(String key, Size param);

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        Starter with(String key, SizeF param);

        Starter with(String key, Parcelable[] param);

        Starter withParcelableArrayList(String key, ArrayList<? extends Parcelable> param);

        Starter with(String key, SparseArray<? extends Parcelable> param);

        Starter withIntegerArrayList(String key, ArrayList<Integer> param);

        Starter withStringArrayList(String key, ArrayList<String> param);

        Starter withCharSequenceArrayList(String key, ArrayList<CharSequence> param);

        Starter with(String key, Serializable param);

        Starter with(String key, byte[] param);

        Starter with(String key, short[] param);

        Starter with(String key, char[] param);

        Starter with(String key, float[] param);

        Starter with(String key, CharSequence[] param);

        Starter with(String key, Bundle param);
    }

    public static class StarterImpl implements Starter {

        private final Activity mActivity;
        private Bundle mBundle = new Bundle();

        public StarterImpl(Activity activity) {
            mActivity = activity;
        }

        @Override
        public int go(Class<? extends Activity> activity) {
            Intent intent = new Intent(mActivity, activity);
            if (mBundle != null && mBundle.size() > 0) {
                intent.putExtras(mBundle);
            }
            int requestCode = _INCREMENT_REQUEST_CODE[0]++;
            if (requestCode >= 0xffff) {
                requestCode = _INCREMENT_REQUEST_CODE[0] = 1;
            }
            LogUtil.d("------------ request code = " + requestCode);
            mActivity.startActivityForResult(intent, requestCode);
            return requestCode;
        }

        @Override
        public Starter clear() {
            mBundle.clear();
            return this;
        }

        @Override
        public Starter putAll(Bundle bundle) {
            bundle.putAll(bundle);
            return this;
        }

        @Override
        public Starter with(String key, String param) {
            mBundle.putString(key, param);
            return this;
        }

        @Override
        public Starter with(String key, byte param) {
            mBundle.putByte(key, param);
            return this;
        }

        @Override
        public Starter with(String key, char param) {
            mBundle.putChar(key, param);
            return this;
        }

        @Override
        public Starter with(String key, short param) {
            mBundle.putShort(key, param);
            return this;
        }

        @Override
        public Starter with(String key, float param) {
            mBundle.putFloat(key, param);
            return this;
        }

        @Override
        public Starter with(String key, CharSequence param) {
            mBundle.putCharSequence(key, param);
            return this;
        }

        @Override
        public Starter with(String key, Parcelable param) {
            mBundle.putParcelable(key, param);
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public Starter with(String key, Size param) {
            mBundle.putSize(key, param);
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public Starter with(String key, SizeF param) {
            mBundle.putSizeF(key, param);
            return this;
        }

        @Override
        public Starter with(String key, Parcelable[] param) {
            mBundle.putParcelableArray(key, param);
            return this;
        }

        @Override
        public Starter withParcelableArrayList(String key, ArrayList<? extends Parcelable> param) {
            mBundle.putParcelableArrayList(key, param);
            return this;
        }

        @Override
        public Starter with(String key, SparseArray<? extends Parcelable> param) {
            mBundle.putSparseParcelableArray(key, param);
            return this;
        }

        @Override
        public Starter withIntegerArrayList(String key, ArrayList<Integer> param) {
            mBundle.putIntegerArrayList(key, param);
            return this;
        }

        @Override
        public Starter withStringArrayList(String key, ArrayList<String> param) {
            mBundle.putStringArrayList(key, param);
            return this;
        }

        @Override
        public Starter withCharSequenceArrayList(String key, ArrayList<CharSequence> param) {
            mBundle.putCharSequenceArrayList(key, param);
            return this;
        }

        @Override
        public Starter with(String key, Serializable param) {
            mBundle.putSerializable(key, param);
            return this;
        }

        @Override
        public Starter with(String key, byte[] param) {
            mBundle.putByteArray(key, param);
            return this;
        }

        @Override
        public Starter with(String key, short[] param) {
            mBundle.putShortArray(key, param);
            return this;
        }

        @Override
        public Starter with(String key, char[] param) {
            mBundle.putCharArray(key, param);
            return this;
        }

        @Override
        public Starter with(String key, float[] param) {
            mBundle.putFloatArray(key, param);
            return this;
        }

        @Override
        public Starter with(String key, CharSequence[] param) {
            mBundle.putCharSequenceArray(key, param);
            return this;
        }

        @Override
        public Starter with(String key, Bundle param) {
            mBundle.putBundle(key, param);
            return this;
        }
    }
}
