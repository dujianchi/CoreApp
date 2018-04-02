package cn.dujc.core.downloader;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import cn.dujc.core.BuildConfig;
import cn.dujc.core.util.ToastUtil;

/**
 * 下载通知栏
 * Created by lucky on 2018/3/8.
 */
public abstract class DownloadNotification implements OnDownloadListener {

    public static final String ON_NOTIFICATION_CLICK_BROADCAST = BuildConfig.APPLICATION_ID+".UPDATE_DOWNLOADER";

    private static final String CHANNEL_ID = "downloader";
    private final int mId = 123;
    private Context mContext;
    private NotificationManager mNotificationManager;
    //private static final String APK_MIME_TYPE = "application/vnd.android.package-archive";
    private NotificationCompat.Builder mNotification;
    private long mLastUpdate = 0;

    public DownloadNotification(Context context, int notificationIcon) {
        mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "更新通知", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("更新通知");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        try {
            mNotification = new NotificationCompat.Builder(mContext, CHANNEL_ID);
        } catch (NoSuchMethodError e) {
            mNotification = new NotificationCompat.Builder(mContext);
        }
        mNotification
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(notificationIcon)
                .setContentTitle("更新")
                .setContentText("下载更新");

        Intent intent = new Intent(ON_NOTIFICATION_CLICK_BROADCAST);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.setContentIntent(pendingIntent);
    }

    @Override
    public void onDownloadFailure(String message) {
        ToastUtil.showToast(mContext, "下载失败：" + message);
    }

    @Override
    public void onUpdateProgress(long downloaded, long total) {
        if (downloaded >= total) {
            mNotification.setContentText("下载完成")
                    .setProgress(0, 0, false);
            mNotificationManager.notify(mId, mNotification.build());
        }
    }

    @Override
    public void onThreadUpdateProgress(long downloaded, long total) {
        final long current = System.currentTimeMillis();
        if (current - mLastUpdate < 1000) return;
        mLastUpdate = current;
        final float scale = total / 100f;
        mNotification.setProgress(100, (int) (downloaded / scale), false);
        final Notification build = mNotification.build();
        mNotificationManager.notify(mId, build);
    }

    public void startDownload(Downloader downloader, boolean _confinue) {
        downloader.setOnDownloadListener(this);
        downloader.download(_confinue);
    }

    /*private void install(Context context, File apk){
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//不会安装一半跳掉
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "apk.fileProvider", apk);
            install.setDataAndType(contentUri, APK_MIME_TYPE);
        } else {
            install.setDataAndType(Uri.fromFile(apk), APK_MIME_TYPE);
        }
        try {
            context.startActivity(install);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }*/
}
