package cn.dujc.core.permission;

import android.content.Context;

public abstract class AbstractOddsPermissionOperator implements IOddsPermissionOperator {

    @Override
    public boolean useOddsPermissionOperate(Context context) {
        return true;
    }

    @Override
    public boolean doneHere(String... permissions) {
        return false;
    }

    @Override
    public boolean showConfirmDialog(String... permissions) {
        return false;
    }
}
