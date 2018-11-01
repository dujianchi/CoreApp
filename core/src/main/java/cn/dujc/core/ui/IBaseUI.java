package cn.dujc.core.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.dujc.core.permission.AppSettingsDialog;
import cn.dujc.core.permission.IOddsPermissionOperator;
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

    IStarter starter();

    IParams extras();

    IPermissionKeeper permissionKeeper();

    View getViewV();

    int getViewId();

    void initBasic(Bundle savedInstanceState);

    public interface WithToolbar extends IBaseUI {
        ViewGroup initToolbar(ViewGroup parent);

        TitleCompat getTitleCompat();

        View createRootView(View contentView);

        enum STYLE {
            LINEAR, FRAME, COORDINATOR
        }
    }

    public interface IContextCompat {
        void startActivityForResult(Intent intent, int requestCode);

        Context context();

        void finish();

        int checkSelfPermission(String permission);

        boolean shouldShowRequestPermissionRationale(String permission);

        void requestPermissions(String[] permissions, int requestCode);
    }

    public interface IStarter {

        int getRequestCode(Class<?> activityForward);

        int newRequestCode(Class<?> activity);

        int go(Class<? extends Activity> activity);

        int go(Class<? extends Activity> activity, boolean finishThen);

        int go(Intent intent);

        int go(Intent intent, boolean finishThen);

        IStarter clear();

        IStarter putAll(Bundle bundle);

        IStarter with(String key, String param);

        IStarter with(String key, byte param);

        IStarter with(String key, char param);

        IStarter with(String key, short param);

        IStarter with(String key, int param);

        IStarter with(String key, float param);

        IStarter with(String key, double param);

        IStarter with(String key, long param);

        IStarter with(String key, boolean param);

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

        IStarter with(String key, int[] param);

        IStarter with(String key, float[] param);

        IStarter with(String key, double[] param);

        IStarter with(String key, long[] param);

        IStarter with(String key, boolean[] param);

        IStarter with(String key, CharSequence[] param);

        IStarter with(String key, Bundle param);
    }

    public interface IParams {
        public <T> T get(String key, T defaultValues, Class<T> clazz);

        public <T> T get(String key, Class<T> clazz);

        public <T> T get(String key);
    }

    public interface IPermissionKeeper {

        void requestPermissions(int requestCode, @StringRes int title, @StringRes int message, String... permission);

        void requestPermissions(int requestCode, String title, String message, String... permission);

        boolean hasPermission(String... permissions);

        void handOnActivityResult(int requestCode);

        void handOnRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

        void setSettingsDialog(IPermissionSettingsDialog settingsDialog);

        void registerOddsPermissionOperator(IOddsPermissionOperator... permissionOperators);

        void unregisterOddsPermissionOperator(IOddsPermissionOperator permissionOperator);
    }

    public interface IPermissionSettingsDialog {
        void showSettingsDialog(IContextCompat context, String title, String message);
    }

    public interface IPermissionKeeperCallback {
        void onGranted(int requestCode, List<String> permissions);

        void onDenied(int requestCode, List<String> permissions);
    }

    public static class IContextCompatActivityImpl implements IContextCompat {
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

        @Override
        public int checkSelfPermission(String permission) {
            return ActivityCompat.checkSelfPermission(context(), permission);
        }

        @Override
        public boolean shouldShowRequestPermissionRationale(String permission) {
            return ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission);
        }

        @Override
        public void requestPermissions(String[] permissions, int requestCode) {
            ActivityCompat.requestPermissions(mActivity, permissions, requestCode);
        }
    }

    public static class IContextCompatFragmentImpl implements IContextCompat {
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
            if (activity != null && !activity.isFinishing()) activity.finish();
        }

        @Override
        public int checkSelfPermission(String permission) {
            return ActivityCompat.checkSelfPermission(context(), permission);
        }

        @Override
        public boolean shouldShowRequestPermissionRationale(String permission) {
            return mFragment.shouldShowRequestPermissionRationale(permission);
        }

        @Override
        public void requestPermissions(String[] permissions, int requestCode) {
            mFragment.requestPermissions(permissions, requestCode);
        }
    }

    public static class IStarterImpl implements IStarter {

        private final Bundle mBundle = new Bundle();
        private final Map<Class<?>, Integer> mRequestCodes = new ArrayMap<>();
        private final IContextCompat mContext;

        public IStarterImpl(Activity activity) {
            this(new IContextCompatActivityImpl(activity));
        }

        public IStarterImpl(Fragment fragment) {
            this(new IContextCompatFragmentImpl(fragment));
        }

        public IStarterImpl(IContextCompat context) {
            mContext = context;
        }

        //跳转
        private static int go(IContextCompat context, Intent intent, int requestCode, boolean finishThen) {
            context.startActivityForResult(intent, requestCode);
            if (finishThen) {
                context.finish();
            }
            return requestCode;
        }

        @Override
        public int getRequestCode(Class<?> activityForward) {
            final Integer integer = mRequestCodes.get(activityForward);
            return integer == null ? -1 : integer;
        }

        @Override
        public int newRequestCode(Class<?> activity) {
            int requestCode = _INCREMENT_REQUEST_CODE[0]++;
            if (requestCode >= 0xffff) {
                requestCode = _INCREMENT_REQUEST_CODE[0] = 1;
            }
            LogUtil.d("------------ request code = " + requestCode);
            mRequestCodes.put(activity, requestCode);
            return requestCode;
        }

        @Override
        public int go(Class<? extends Activity> activity) {
            return go(activity, false);
        }

        @Override
        public int go(Class<? extends Activity> activity, boolean finishThen) {
            Intent intent = new Intent(mContext.context(), activity);
            if (mBundle.size() > 0) {
                intent.putExtras(mBundle);
            }
            int requestCode = newRequestCode(activity);
            return IStarterImpl.go(mContext, intent, requestCode, finishThen);
        }

        @Override
        public int go(Intent intent) {
            return go(intent, false);
        }

        @Override
        public int go(Intent intent, boolean finishThen) {
            final ComponentName component = intent.getComponent();
            int requestCode = 0;
            if (component != null) {
                final Class<?> activity;
                try {
                    activity = Class.forName(component.getClassName());
                    requestCode = newRequestCode(activity);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
            return IStarterImpl.go(mContext, intent, requestCode, finishThen);
        }

        @Override
        public IStarter clear() {
            mBundle.clear();
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
        public IStarter with(String key, int param) {
            mBundle.putInt(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, float param) {
            mBundle.putFloat(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, double param) {
            mBundle.putDouble(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, long param) {
            mBundle.putLong(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, boolean param) {
            mBundle.putBoolean(key, param);
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
        public IStarter with(String key, int[] param) {
            mBundle.putIntArray(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, float[] param) {
            mBundle.putFloatArray(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, double[] param) {
            mBundle.putDoubleArray(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, long[] param) {
            mBundle.putLongArray(key, param);
            return this;
        }

        @Override
        public IStarter with(String key, boolean[] param) {
            mBundle.putBooleanArray(key, param);
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

    public static class IPermissionKeeperImpl implements IPermissionKeeper {

        private final List<IOddsPermissionOperator> mPermissionOperators = new ArrayList<>();
        private final IContextCompat mContext;
        private final IPermissionKeeperCallback mCallback;
        private IPermissionSettingsDialog mSettingsDialog;
        private String[] mLastRequestedPermissions = null;

        public IPermissionKeeperImpl(Activity activity, IPermissionKeeperCallback callback) {
            this(new IContextCompatActivityImpl(activity), callback);
        }

        public IPermissionKeeperImpl(Fragment fragment, IPermissionKeeperCallback callback) {
            this(new IContextCompatFragmentImpl(fragment), callback);
        }

        public IPermissionKeeperImpl(IContextCompat context, IPermissionKeeperCallback callback) {
            mContext = context;
            mCallback = callback;
            mSettingsDialog = new IPermissionSettingsDialogImpl();
        }

        @Override
        public void requestPermissions(int requestCode, @StringRes int title, @StringRes int message, String... permission) {
            final String titleStr = title != 0 ? mContext.context().getString(title) : "";
            final String messageStr = message != 0 ? mContext.context().getString(message) : "";
            requestPermissions(requestCode, titleStr, messageStr, permission);
        }

        @Override
        public void requestPermissions(int requestCode, String title, String message, String... permissions) {
            mLastRequestedPermissions = null;
            if (permissions == null) return;
            mLastRequestedPermissions = permissions;
            if (hasPermission(permissions)) {
                if (mCallback != null) mCallback.onGranted(requestCode, Arrays.asList(permissions));
            } else {
                boolean showHint = false;
                for (String permission : permissions) {
                    showHint = showHint || mContext.shouldShowRequestPermissionRationale(permission);
                }
                if (showHint && mSettingsDialog != null) {
                    mSettingsDialog.showSettingsDialog(mContext, title, message);
                } else {
                    mContext.requestPermissions(permissions, requestCode);
                }
            }
        }

        @Override
        public boolean hasPermission(String... permissions) {
            return IPermissionKeeperImpl.hasPermission(mContext.context(), permissions);
        }

        @Override
        public void handOnActivityResult(int requestCode) {
            if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE
                    && mLastRequestedPermissions != null) {
                handleGrantedOrDenied(mContext.context(), mCallback, mLastRequestedPermissions, null, requestCode);
            }
        }

        @Override
        public void handOnRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            handleGrantedOrDenied(mContext.context(), mCallback, permissions, grantResults, requestCode);
        }

        @Override
        public void setSettingsDialog(IPermissionSettingsDialog settingsDialog) {
            mSettingsDialog = settingsDialog;
        }

        @Override
        public void registerOddsPermissionOperator(IOddsPermissionOperator... permissionOperators) {
            if (permissionOperators != null && permissionOperators.length > 0) {
                mPermissionOperators.addAll(Arrays.asList(permissionOperators));
            }
        }

        @Override
        public void unregisterOddsPermissionOperator(IOddsPermissionOperator permissionOperator) {
            if (permissionOperator != null) {
                mPermissionOperators.remove(permissionOperator);
            }
        }

        private static boolean hasPermission(Context context, String... permissions) {
            boolean has = permissions != null && permissions.length > 0;
            if (has) {
                for (String permission : permissions) {
                    has = has && ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
                }
            }
            return has;
        }

        private static void handleGrantedOrDenied(Context context, IPermissionKeeperCallback callback, String[] permissions, int[] grantResults, int requestCode) {
            if (context == null || callback == null || permissions == null) return;

            final List<String> granted = new ArrayList<>();
            final List<String> denied = new ArrayList<>();

            if (grantResults == null) {
                for (String perm : permissions) {
                    if (hasPermission(context, perm)) {
                        granted.add(perm);
                    } else {
                        denied.add(perm);
                    }
                }
            } else {
                for (int index = 0, length = permissions.length; index < length; index++) {
                    String perm = permissions[index];
                    if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                        granted.add(perm);
                    } else {
                        denied.add(perm);
                    }
                }
            }

            /*if (!granted.isEmpty()) {
                callback.onGranted(requestCode, granted);
            }

            if (!denied.isEmpty()) {
                callback.onDenied(requestCode, denied);
            }*/
            if (denied.isEmpty() && !granted.isEmpty()) {
                callback.onGranted(requestCode, granted);
            } else {
                callback.onDenied(requestCode, denied);
            }
        }

    }

    public static class IPermissionSettingsDialogImpl implements IPermissionSettingsDialog {

        @Override
        public void showSettingsDialog(IContextCompat context, String title, String message) {
            final AppSettingsDialog.Builder builder = new AppSettingsDialog.Builder(context);
            if (!TextUtils.isEmpty(title)) builder.setTitle(title);
            if (!TextUtils.isEmpty(message)) builder.setRationale(message);
            builder.build().show();
        }
    }
}
