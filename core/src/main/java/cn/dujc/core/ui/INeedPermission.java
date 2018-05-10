package cn.dujc.core.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public interface INeedPermission extends EasyPermissions.PermissionCallbacks{

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);

    public void onPermissionsDenied(int requestCode, List<String> perms);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void requestMyPermissions(String rationale, int requestCode, String... permissions);

    public void onPermissionsGranted(int requestCode, List<String> perms);

    void onPermissionJustDenied(int requestCode, List<String> permissions);

    public static class NeedPermissionImpl implements INeedPermission {

        private final Activity activity;

        private List<String> lastRequestPermissions = null;
        private int lastPermissionRequestCode = -1;

        public NeedPermissionImpl(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            lastRequestPermissions = null;
            lastPermissionRequestCode = -1;
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }

        @Override
        public void onPermissionsDenied(int requestCode, List<String> perms) {
            if (EasyPermissions.somePermissionPermanentlyDenied(activity, perms)) {
                lastRequestPermissions = perms;
                lastPermissionRequestCode = requestCode;
                new AppSettingsDialog.Builder(activity).build().show();
            } else {
                onPermissionJustDenied(requestCode, perms);
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE
                    && lastRequestPermissions != null) {
                final String[] perms = lastRequestPermissions.toArray(new String[lastRequestPermissions.size()]);
                if (EasyPermissions.hasPermissions(activity, perms)) {
                    onPermissionsGranted(lastPermissionRequestCode, lastRequestPermissions);
                } else {
                    onPermissionJustDenied(lastPermissionRequestCode, lastRequestPermissions);
                }
            }
        }

        @Override
        public void requestMyPermissions(String rationale, int requestCode, String... permissions) {
            if (EasyPermissions.hasPermissions(activity, permissions)) {
                onPermissionsGranted(requestCode, Arrays.asList(permissions));
            } else {
                EasyPermissions.requestPermissions(activity, rationale, requestCode, permissions);
            }
        }

        @Override
        public void onPermissionsGranted(int requestCode, List<String> perms) {

        }

        @Override
        public void onPermissionJustDenied(int requestCode, List<String> permissions) {

        }
    }
}
