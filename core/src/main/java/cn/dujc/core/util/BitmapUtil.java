package cn.dujc.core.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {

    public static byte[] toBytes(Bitmap bitmap, Bitmap.CompressFormat compressFormat) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(compressFormat, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bitmap.recycle();
            return byteArray;
        }
        return new byte[0];
    }

    public static Bitmap fromBytes(byte[] bytes) {
        if (bytes.length != 0) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    /**
     * 获取bitmap的真正大小
     *
     * @param bitmap 要测量的bitmap
     * @return 真正大小 in byte
     */
    public static int getBitmapSizeInByte(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) return 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    /**
     * 收缩bitmap大小
     *
     * @param src      原bitmap
     * @param sizeInKb 微信限制32k
     * @return 新的bitmap
     */
    @Nullable
    public static Bitmap shrinkImage(Bitmap src, int sizeInKb, boolean recycle) {
        if (src == null || src.isRecycled()) return null;

        final int sizeInByte = getBitmapSizeInByte(src);
        final int byteForKb = sizeInKb * 1024;
        LogUtil.d("------   zoom before is " + sizeInByte);
        if (sizeInByte > byteForKb) {//若图片大小大于目的大小，则压缩大小97%，压缩质量85%
            float zoom = (float) Math.sqrt(byteForKb * 1.0 / sizeInByte);
            if (zoom >= 0.97) zoom = 0.97F;
            LogUtil.d("------   zoom level is " + zoom);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            src.compress(Bitmap.CompressFormat.JPEG, 85, out);
            Matrix matrix = new Matrix();
            matrix.setScale(zoom, zoom);
            Bitmap result = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);

            if (recycle) src.recycle();//我自己加的，源图片好像已经没用了，可以回收

            out.reset();
            matrix.reset();
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return shrinkImage(result, sizeInKb, true);//中间产生的过渡图片都可以回收
        }
        return src;
    }

    /**
     * 保存bitmap到指定路径
     */
    public static String saveBitmapToFile(Bitmap bitmap, File file) {
        return saveBitmapToFile(bitmap, file, Bitmap.CompressFormat.JPEG, 100, true);
    }

    /**
     * 保存bitmap到指定路径
     */
    public static String saveBitmapToFile(Bitmap bitmap, String file, Bitmap.CompressFormat format, int quality, boolean recycle) {
        return saveBitmapToFile(bitmap, new File(file), format, quality, recycle);
    }

    /**
     * 保存bitmap到指定路径
     */
    public static String saveBitmapToFile(Bitmap bitmap, File file, Bitmap.CompressFormat format, int quality, boolean recycle) {
        if (file.exists())
            file.delete();
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            if (bitmap != null)
                bitmap.compress(format, quality, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (recycle && bitmap != null)//此处不能回收bitmap，否则后面会不能用
            bitmap.recycle();
        return file.getPath();
    }

    /**
     * 图片处理
     * 从路径中解析bitmap并压缩图片大小，优先判断短边，当图片最短边大于shortEdge则缩小图片，否则当最长边大于longEdge则压缩图片，再否则就不压大小
     */
    public static Bitmap decodeSmallerFromFile(String path, int shortEdge, int longEdge) {
        return decodeSmallerFromFile(new File(path), shortEdge, longEdge);
    }

    /**
     * 图片处理
     * 从路径中解析bitmap并压缩图片大小，优先判断短边，当图片最短边大于shortEdge则缩小图片，否则当最长边大于longEdge则压缩图片，再否则就不压大小
     */
    public static Bitmap decodeSmallerFromFile(File file, int shortEdge, int longEdge) {
        LogUtil.d("---------------   path =     " + file);
        if (file == null) {
            LogUtil.e("decode bitmap error, course path is null");
            return null;
        }
        //------------- decode now --------------
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        FileInputStream is = null;
        Bitmap bitmap = null;
        try {
            is = new FileInputStream(file);
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, newOpts);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            newOpts.inPurgeable = true;
            newOpts.inInputShareable = true;
            newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//4.4以下手机降低bitmap色彩，降低oom发生概率 by djc 2015年11月30日 10:41:19
        }
        newOpts.inDither = false;
        newOpts.inJustDecodeBounds = false;

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (shortEdge > 0) {
            int edge = Math.min(newOpts.outWidth, newOpts.outHeight);
            if (edge > shortEdge) {
                newOpts.inSampleSize = (int) (edge * 1f / shortEdge + 0.5f);
            }
        } else if (longEdge > 0) {
            int edge = Math.max(newOpts.outWidth, newOpts.outHeight);
            if (edge > longEdge) {
                newOpts.inSampleSize = (int) (edge * 1f / longEdge + 0.5f);
            }
        }
        if (bitmap != null)
            bitmap.recycle();
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        try {
            bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, newOpts);  //bitmap = BitmapFactory.decodeFile(path, newOpts);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        newOpts = null;
        return bitmap;
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
        } catch (Exception e) {
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
        matrix.postRotate(degrees);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bmp != null) {
            bitmap.recycle();
        } else {
            return bitmap;
        }
        return bmp;
    }


}
