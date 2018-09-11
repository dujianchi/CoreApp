package cn.dujc.core.permission;


import android.Manifest;

import cn.dujc.core.ui.IBaseUI;

/**
 * @author du
 * date 2018/9/5 上午10:15
 */
public class IOddsPermissionOperatorImpl implements IOddsPermissionOperator {

    @Override
    public boolean hasPermission(IBaseUI.IContextCompat context, String... permissions) {
        boolean result = true;
        if (permissions != null && permissions.length > 0) {
            for (String permission : permissions) {
                if (Manifest.permission.READ_CALENDAR.equals(permission)
                        || Manifest.permission.WRITE_CALENDAR.equals(permission)) {
                    result = result && hasCalendar(context);
                } else if (Manifest.permission.CAMERA.equals(permission)) {
                    result = result && hasCamera(context);
                } else if (Manifest.permission.READ_CONTACTS.equals(permission)
                        || Manifest.permission.WRITE_CONTACTS.equals(permission)
                        || Manifest.permission.GET_ACCOUNTS.equals(permission)) {
                    result = result && hasContacts(context);
                } else if (Manifest.permission.READ_CALENDAR.equals(permission)
                        || Manifest.permission.WRITE_CALENDAR.equals(permission)) {
                    result = result && hasCalendar(context);
                } else if (Manifest.permission.READ_CALENDAR.equals(permission)
                        || Manifest.permission.WRITE_CALENDAR.equals(permission)) {
                    result = result && hasCalendar(context);
                } else if (Manifest.permission.READ_CALENDAR.equals(permission)
                        || Manifest.permission.WRITE_CALENDAR.equals(permission)) {
                    result = result && hasCalendar(context);
                } else if (Manifest.permission.READ_CALENDAR.equals(permission)
                        || Manifest.permission.WRITE_CALENDAR.equals(permission)) {
                    result = result && hasCalendar(context);
                } else if (Manifest.permission.READ_CALENDAR.equals(permission)
                        || Manifest.permission.WRITE_CALENDAR.equals(permission)) {
                    result = result && hasCalendar(context);
                } else if (Manifest.permission.READ_CALENDAR.equals(permission)
                        || Manifest.permission.WRITE_CALENDAR.equals(permission)) {
                    result = result && hasCalendar(context);
                }
            }
        }
        return result;
    }

    @Override
    public boolean hasCalendar(IBaseUI.IContextCompat context) {
        return true;
    }

    @Override
    public boolean hasCamera(IBaseUI.IContextCompat context) {
        return true;
    }

    @Override
    public boolean hasContacts(IBaseUI.IContextCompat context) {
        return true;
    }

    @Override
    public boolean hasLocation(IBaseUI.IContextCompat context) {
        return true;
    }

    @Override
    public boolean hasMicrophone(IBaseUI.IContextCompat context) {
        return true;
    }

    @Override
    public boolean hasPhone(IBaseUI.IContextCompat context) {
        return true;
    }

    @Override
    public boolean hasSensors(IBaseUI.IContextCompat context) {
        return true;
    }

    @Override
    public boolean hasSMS(IBaseUI.IContextCompat context) {
        return true;
    }

    @Override
    public boolean hasStorage(IBaseUI.IContextCompat context) {
        return true;
    }


}
