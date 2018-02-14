package cn.dujc.core.widget;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 常用的主页生成器
 * Created by du on 2018/1/28.
 */
public class NormalMainCreator implements RadioMenu.OnItemSelectedChangeListener {

    private final Map<RadioMenu.Item, Fragment> mFragments = new LinkedHashMap<>();

    private final int mFragmentsLayoutId, mMenuLayoutId;
    private AppCompatActivity mActivity;

    private NormalMainCreator(int fragmentsLayoutId, int menuLayoutId) {
        mFragmentsLayoutId = fragmentsLayoutId;
        mMenuLayoutId = menuLayoutId;
    }

    public static NormalMainCreator create(int fragmentsLayoutId, int menuLayoutId) {
        return new NormalMainCreator(fragmentsLayoutId, menuLayoutId);
    }

    @Override
    public void onItemSelected(RadioMenu.Item item) {
        final Fragment fragment = mFragments.get(item);
        if (fragment != null && mFragmentsLayoutId != 0 && mActivity != null) {
            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(mFragmentsLayoutId, fragment)
                    .commit();
        }
    }

    @Override
    public void onItemUnselected(RadioMenu.Item item) {}

    public NormalMainCreator add(RadioMenu.Item item, Fragment fragment) {
        mFragments.put(item, fragment);
        return this;
    }

    public void into(AppCompatActivity activity) {
        mActivity = activity;
        if (mMenuLayoutId != 0 && activity != null) {
            final RadioMenu radioMenu = activity.findViewById(mMenuLayoutId);
            radioMenu.setOnItemSelectedChangeListener(this);
            final Set<RadioMenu.Item> items = mFragments.keySet();
            for (RadioMenu.Item item : items) {
                radioMenu.addItem(item);
            }
        }
    }
}
