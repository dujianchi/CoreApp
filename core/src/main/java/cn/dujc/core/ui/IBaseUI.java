package cn.dujc.core.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.Toolbar;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import cn.dujc.core.util.LogUtil;

/**
 * 基本的UI类方法，包括activity和fragment
 * Created by du on 2018/2/14.
 */
public interface IBaseUI {
    /**
     * 自增的request code，每个跳转都是forresult的跳转，那么，今后只要记住跳转方法{@link #starter().go(Class)}返回的int值
     * ，即为本次跳转产生的request code，从此不再管理request code，且不会再重复，因为不管在什么界面跳转，每次跳转都用了不同的request code（当然，崩溃重启的情况例外）
     */
    int[] _INCREMENT_REQUEST_CODE = {1};

    Toolbar initToolbar(ViewGroup parent);

    TitleCompat getTitleCompat();

    View createRootView(View contentView);

    IStarter starter();

    IParams extras();

    View getViewV();

    int getViewId();

    void initBasic(Bundle savedInstanceState);

    interface IContextCompat {
        void startActivityForResult(Intent intent, int requestCode);

        Context context();

        void finish();
    }

    public interface IStarter {

        int getRequestCode(Class<? extends Activity> activityForward);

        int go(Class<? extends Activity> activity);

        IStarter finishThen();

        IStarter clear();

        IStarter putAll(Bundle bundle);

        IStarter with(String key, String param);

        IStarter with(String key, byte param);

        IStarter with(String key, char param);

        IStarter with(String key, short param);

        IStarter with(String key, float param);

        IStarter with(String key, CharSequence param);

        IStarter with(String key, Parcelable param);

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        IStarter with(String key, Size param);

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        IStarter with(String key, SizeF param);

        IStarter with(String key, Parcelable[] param);

        IStarter withParcelableArrayList(String key, ArrayList<? extends Parcelable> param);

        IStarter with(String key, SparseArray<? extends Parcelable> param);

        IStarter withIntegerArrayList(String key, ArrayList<Integer> param);

        IStarter withStringArrayList(String key, ArrayList<String> param);

        IStarter withCharSequenceArrayList(String key, ArrayList<CharSequence> param);

        IStarter with(String key, Serializable param);

        IStarter with(String key, byte[] param);

        IStarter with(String key, short[] param);

        IStarter with(String key, char[] param);

        IStarter with(String key, float[] param);

        IStarter with(String key, CharSequence[] param);

        IStarter with(String key, Bundle param);
    }

    public interface IParams {
        public <T> T get(String key, T defaultValues, Class<T> clazz);

        public <T> T get(String key, Class<T> clazz);

        public <T> T get(String key);
    }

    static class IContextCompatActivityImpl implements IContextCompat {
        private final Activity mActivity;

        public IContextCompatActivityImpl(Activity activity) {
            mActivity = activity;
        }

        @Override
        public void startActivityForResult(Intent intent, int requestCode) {
            mActivity.startActivityForResult(intent, requestCode);
        }

        @Override
        public Context context() {
            return mActivity;
        }

        @Override
        public void finish() {
            mActivity.finish();
        }
    }

    static class IContextCompatFragmentImpl implements IContextCompat {
        private final Fragment mFragment;

        public IContextCompatFragmentImpl(Fragment fragment) {
            mFragment = fragment;
        }

        @Override
        public void startActivityForResult(Intent intent, int requestCode) {
            mFragment.startActivityForResult(intent, requestCode);
        }

        @Override
        public Context context() {
            return mFragment.getContext();
        }

        @Override
        public void finish() {
            final FragmentActivity activity = mFragment.getActivity();
            if (activity != null && !activity.isFinishing())activity.finish();
        }
    }

    public static class IStarterImpl implements IStarter {

        Bundle mBundle = new Bundle();
        Map<Class<? extends Activity>, Integer> mRequestCodes = new ArrayMap<>();
        private IContextCompat mContext;
        private boolean mFinishThenGo = false;//跳转完是否关闭，默认false。跳转完或获取starter后恢复默认

        public IStarterImpl(Activity activity) {
            mContext = new IContextCompatActivityImpl(activity);
        }

        public IStarterImpl(Fragment fragment) {
            mContext = new IContextCompatFragmentImpl(fragment);
        }

        @Override
        public int getRequestCode(Class<? extends Activity> activityForward) {
            final Integer integer = mRequestCodes.get(activityForward);
            return integer == null ? -1 : integer;
        }

        @Override
        public int go(Class<? extends Activity> activity) {
            Intent intent = new Intent(mContext.context(), activity);
            if (mBundle != null && mBundle.size() > 0) {
                intent.putExtras(mBundle);
            }
            int requestCode = _INCREMENT_REQUEST_CODE[0]++;
            if (requestCode >= 0xffff) {
                requestCode = _INCREMENT_REQUEST_CODE[0] = 1;
            }
            LogUtil.d("------------ request code = " + requestCode);
            mRequestCodes.put(activity, requestCode);
            mContext.startActivityForResult(intent, requestCode);
            if (mFinishThenGo) {
                mContext.finish();
                mFinishThenGo = false;
            }
            return requestCode;
        }

        @Override
        public IStarter finishThen() {
            mFinishThenGo = true;
            return this;
        }

        @Override
        public IStarter clear() {
            mBundle.clear();
            mFinishThenGo = false;
            return this;
        }

        @Override
        public IStarter putAll(Bundle bundle) {
            bundle.putAll(bundle);
            return this;
        }

        @Override
        public IStarter with(String key, String param) {
            mBundle.putString(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, byte param) {
            mBundle.putByte(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, char param) {
            mBundle.putChar(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, short param) {
            mBundle.putShort(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, float param) {
            mBundle.putFloat(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, CharSequence param) {
            mBundle.putCharSequence(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, Parcelable param) {
            mBundle.putParcelable(key, param);
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public IStarter with(String key, Size param) {
            mBundle.putSize(key, param);
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public IStarter with(String key, SizeF param) {
            mBundle.putSizeF(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, Parcelable[] param) {
            mBundle.putParcelableArray(key, param);
            return this;
        }

        @Override
        public IStarter withParcelableArrayList(String key, ArrayList<? extends Parcelable> param) {
            mBundle.putParcelableArrayList(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, SparseArray<? extends Parcelable> param) {
            mBundle.putSparseParcelableArray(key, param);
            return this;
        }

        @Override
        public IStarter withIntegerArrayList(String key, ArrayList<Integer> param) {
            mBundle.putIntegerArrayList(key, param);
            return this;
        }

        @Override
        public IStarter withStringArrayList(String key, ArrayList<String> param) {
            mBundle.putStringArrayList(key, param);
            return this;
        }

        @Override
        public IStarter withCharSequenceArrayList(String key, ArrayList<CharSequence> param) {
            mBundle.putCharSequenceArrayList(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, Serializable param) {
            mBundle.putSerializable(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, byte[] param) {
            mBundle.putByteArray(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, short[] param) {
            mBundle.putShortArray(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, char[] param) {
            mBundle.putCharArray(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, float[] param) {
            mBundle.putFloatArray(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, CharSequence[] param) {
            mBundle.putCharSequenceArray(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, Bundle param) {
            mBundle.putBundle(key, param);
            return this;
        }
    }

    public static class IParamsImpl implements IParams {

        private final Bundle mBundle;

        public IParamsImpl(Activity activity) {
            mBundle = activity != null && activity.getIntent() != null ? activity.getIntent().getExtras() : null;
        }

        @Override
        @Nullable
        public <T> T get(String key, T defaultValues, Class<T> clazz) {
            if (mBundle != null && key != null) {
                final Object obj = mBundle.get(key);
                if (clazz != null && clazz.isInstance(obj)) {
                    return clazz.cast(obj);
                } else if (obj != null) {
                    try {
                        return (T) obj;
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }
                }
            }
            return defaultValues;
        }

        @Override
        @Nullable
        public <T> T get(String key, Class<T> clazz) {
            return get(key, null, clazz);
        }

        @Override
        @Nullable
        public <T> T get(String key) {
            return get(key, null, null);
        }
    }
}
