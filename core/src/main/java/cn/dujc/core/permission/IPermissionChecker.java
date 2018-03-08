package cn.dujc.core.permission;

import android.content.Context;

public interface IPermissionChecker {

    /**
     * 权限通过回调
     */
    public interface OnPermissionGranted {
        void onPermissionGranted(String[] permission);
    }

    /**
     * 权限拒绝回调
     */
    public interface OnPermissionDenied {
        void onPermissionDenied(String[] permission);
    }

    public int checkPermission(String[] permissions, String rationale);
    public int checkPermission(String[] permissions, String rationale, OnPermissionGranted callback);
    public int checkPermission(String[] permissions, String rationale, OnPermissionGranted callback1, OnPermissionDenied callback2);

}