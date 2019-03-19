package cn.dujc.core.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public final class SerializableTransfer {

    private final File mFile;

    public SerializableTransfer(File file) {
        mFile = file;
    }

    public SerializableTransfer(@NonNull Context context, String filename) {
        this(new File(context.getCacheDir(), filename));
    }

    public void save(Serializable serializable) {
        if (serializable == null || mFile == null) return;
        synchronized (mFile.getAbsolutePath()) {
            mFile.deleteOnExit();
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            try {
                fos = new FileOutputStream(mFile);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(serializable);
                oos.flush();
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (oos != null) try {
                    oos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (fos != null) try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Nullable
    public Object read() {
        if (mFile == null || !mFile.exists()) return null;
        synchronized (mFile.getAbsolutePath()) {
            Object result = null;
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                fis = new FileInputStream(mFile);
                ois = new ObjectInputStream(fis);
                result = ois.readObject();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ois != null) try {
                    ois.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (fis != null) try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }
}
