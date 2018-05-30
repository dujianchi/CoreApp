package cn.dujc.core.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * @author du
 * date 2018/5/30 下午7:35
 */
public class Installer {

    public static final String APK_MIME_TYPE = "application/vnd.android.package-archive";

    private Installer() { }

    /**
     * 安装apk
     *
     * @param authorityIfLargeN 如果版本大于N，api24，则需要传FileProvider的authority，否则传null即可
     */
    public static void install(Context context, File apk, String authorityIfLargeN) {
        if (apk == null || !apk.exists()) {
            ToastUtil.showToast(context, "安装包不存在");
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || ContextCompat.checkSelfPermission(context, Manifest.permission.REQUEST_INSTALL_PACKAGES)
                == PackageManager.PERMISSION_GRANTED) {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//不会安装一半跳掉
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, authorityIfLargeN, apk);
                install.setDataAndType(contentUri, APK_MIME_TYPE);
            } else {
                install.setDataAndType(Uri.fromFile(apk), APK_MIME_TYPE);
            }
            try {
                context.startActivity(install);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            ToastUtil.showToast(context, "没有安装权限");
        }
    }
}
