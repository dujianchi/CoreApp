package cn.dujc.core.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import cn.dujc.core.R;

public class FragmentShellActivity extends BaseActivity {

    private static final String KEY_FRAGMENT_CLASS = "KEY_FRAGMENT_CLASS",
            KEY_FRAGMENT_CONTAINER_ID = "KEY_FRAGMENT_CONTAINER_ID";

    public static int start(IStarter starter, Class<? extends Fragment> fragment) {
        return start(starter, fragment, 0);
    }

    public static int start(IStarter starter, Class<? extends Fragment> fragment, int containerId) {
        if (fragment != null) {
            starter.with(KEY_FRAGMENT_CLASS, fragment.getName());
        }
        if (containerId != 0) {
            starter.with(KEY_FRAGMENT_CONTAINER_ID, containerId);
        }
        return starter != null ? starter.go(FragmentShellActivity.class) : 0;
    }

    @Override
    public int getViewId() {
        return R.layout.fragment_shell_activity;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        try {
            final int containerId = extras().get(KEY_FRAGMENT_CONTAINER_ID);
            final Fragment fragment = (Fragment) Class.forName(extras().get(KEY_FRAGMENT_CLASS, String.class))
                    .newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(containerId, fragment)
                    .commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
