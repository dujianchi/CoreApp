/*
 * 版权所有 (c)2015, 福建楷霖网络科技有限公司
 * Create on 2016-2-3 下午1:46
 * 文件名：  MediaUtil.java
 * 版本         修改时间              作者    修改内容
 * V1.0.0      2016-2-3 下午1:46  Du JC  初始版本
 *
 * 最后修改时间：  2016-2-3 上午10:53
 * 注意：本内容仅限于福建楷霖网络科技有限公司内部传阅
 * 禁止外泄以及用于其他的商业目的,违者必究！
 * =========================================================
 * 作者： 杜坚持
 */

package cn.dujc.core.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Du JC on 2015/12/14.
 */
public class MediaUtil {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /**
     * 比较正常的使用方法
     *
     * @param subDirName
     * @return
     */
    public static File getOutputMediaDir(String subDirName) {
        if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), subDirName);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                LogUtil.e("failed to create directory");
                return null;
            }
        }

        return mediaStorageDir;
    }

    public static File getOutputMediaFile(String subDir, int type) {
        File mediaStorageDir = getOutputMediaDir(subDir);
        if (mediaStorageDir == null) {
            return null;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static void refreshGallery(Context context, File file) {
        new SingleMediaScanner(context.getApplicationContext(), file);
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        if (TextUtils.isEmpty(path)) {
            return 0;
        }
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            degree = 0;
        }
        return degree;
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap  原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bmp != null) {
            bitmap.recycle();
        } else {
            return bitmap;
        }
        return bmp;
    }

    /**
     * 将图片保存到SD卡中，并且更新缩略图
     * @param context
     * @param subDir
     * @param bitmap
     */
    public static void saveOneStep(Context context, String subDir, Bitmap bitmap){
        final File file = getOutputMediaFile(subDir, MEDIA_TYPE_IMAGE);
        if (file != null) {
            String path = saveImgToGallery(context, bitmap, subDir, file.getName());
            ToastUtil.showToast(context, StringUtil.concat("图片已保存到：", path));
        }
    }

    /**
     * 将图片保存到SD卡中，并且更新缩略图
     *
     * @param context
     * @param bitmap
     * @param fileName
     * @return path
     */
    public static String saveImgToGallery(Context context, Bitmap bitmap, String subDirName, String fileName) {
        File outFileDir = getCompatOutputMediaDir(context, subDirName);
        if (outFileDir == null) {
            return null;
        }
        if (bitmap == null) {
            return null;
        }

        File jpg = new File(outFileDir + File.separator + fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(jpg.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        refreshGallery(context, jpg);
        //context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
        //mediaScan(context, path);//Android4.4以上无系统权限不能发送Intent.ACTION_MEDIA_MOUNTED广播，所以采用此方法更新图库
        return jpg.getPath();
    }

    /**
     * 4.4比较坑爹的方法
     *
     * @param context
     * @return
     */
    public static File getKitkatOutputMediaDir(Context context) {
        if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        File mediaStorageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (mediaStorageDir != null && !mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                LogUtil.e("failed to create directory");
                return null;
            }
        }

        return mediaStorageDir;
    }

    public static File getCompatOutputMediaDir(Context context, String subDirName) {
        File atSdDir = getOutputMediaDir(subDirName);
        if (atSdDir == null) {
            atSdDir = getKitkatOutputMediaDir(context);
        }
        return atSdDir;
    }

    private static class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mMs;
        private File mFile;

        public SingleMediaScanner(Context context, File f) {
            mFile = f;
            mMs = new MediaScannerConnection(context.getApplicationContext(), this);
            mMs.connect();
        }

        @Override
        public void onMediaScannerConnected() {
            mMs.scanFile(mFile.getAbsolutePath(), null);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mMs.disconnect();
        }

    }
}
