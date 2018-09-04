package cn.dujc.core.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import cn.dujc.core.R;

public class FragmentShellActivity extends BaseActivity {

    private static final String KEY_FRAGMENT_CLASS = "KEY_FRAGMENT_CLASS";

    public static int start(IStarter starter, Class<? extends Fragment> fragment) {
        if (fragment != null) {
            starter.with(KEY_FRAGMENT_CLASS, fragment.getName());
        }
        return starter != null ? starter.go(FragmentShellActivity.class) : 0;
    }

    public static Intent load(Context context, Class<? extends Fragment> fragment) {
        final Intent intent = new Intent(context, FragmentShellActivity.class);
        final Bundle bundle = new Bundle();
        bundle.putString(KEY_FRAGMENT_CLASS, fragment.getName());
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public int getViewId() {
        return R.layout.fragment_shell_activity;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        try {
            final Fragment fragment = (Fragment) Class.forName(extras().get(KEY_FRAGMENT_CLASS, String.class))
                    .newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_fragment_container, fragment)
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
