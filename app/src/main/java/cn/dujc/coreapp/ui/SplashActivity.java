package cn.dujc.coreapp.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.dujc.core.ui.BaseActivity;
import cn.dujc.coreapp.R;

public class SplashActivity extends BaseActivity {

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View initToolbar(ViewGroup parent) {
        return null;
    }

    @Override
    protected boolean fullScreen() {
        return true;
    }

    @Override
    public int getViewId() {
        return 0;
    }

    @Nullable
    @Override
    public View getViewV() {
        final ImageView imageView = new ImageView(mActivity);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.my_wallet);
        return imageView;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        sHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                starter().go(MainActivity.class, true);
            }
        }, 2000);
    }
}
