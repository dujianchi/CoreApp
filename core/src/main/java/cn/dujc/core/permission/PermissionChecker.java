package cn.dujc.core.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

/**
 * Created by lucky on 2018/2/22.
 */
public class PermissionChecker implements IPermissionChecker {

    public static PermissionChecker crate(Activity activity) {
        return new PermissionChecker(activity);
    }

    private Activity mActivity;

    private PermissionChecker(Activity activity) {
        mActivity = activity;
    }

    @Override
    public int checkPermission(String[] permissions, String rationale) {
        return checkPermission(permissions, null, null);
    }

    @Override
    public int checkPermission(String[] permissions, String rationale, OnPermissionGranted callback) {
        return checkPermission(permissions, rationale, callback, null);
    }

    @Override
    public int checkPermission(String[] permissions, String rationale, OnPermissionGranted callback1, OnPermissionDenied callback2) {
        final int permission = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            if (callback1 != null) {
                callback1.onPermissionGranted(permissions);
            }
        } else {
            if (callback2 != null) {
                callback2.onPermissionDenied(permissions);
            }
            if (!TextUtils.isEmpty(rationale)) {
                ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissions[0]);
            } else {
                ActivityCompat.requestPermissions(mActivity, permissions, 123);
            }
        }
        return permission;
    }

}
