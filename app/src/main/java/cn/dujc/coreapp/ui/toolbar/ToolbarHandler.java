package cn.dujc.coreapp.ui.toolbar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import cn.dujc.core.toolbar.IToolbar;
import cn.dujc.core.ui.FragmentShellActivity;
import cn.dujc.core.ui.IBaseUI;
import cn.dujc.coreapp.R;

/**
 * @author du
 * date 2018/10/31 1:55 PM
 */
public class ToolbarHandler implements IToolbar {

    private static final ToolbarHandler HANDLER = new ToolbarHandler();

    private ToolbarHandler() {}

    @Instance
    public static ToolbarHandler getHandler() {
        return HANDLER;
    }

    @Override
    public IToolbar get() {
        return this;
    }

    public View normal(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.toolbar, parent, false);
    }

    @Override
    public int statusBarColor(Context context) {
        return ContextCompat.getColor(context, R.color.colorPrimary);
    }

    @Override
    public StatusBarMode statusBarMode() {
        return StatusBarMode.NONE;
    }

    @Override
    public List<Class<? extends IBaseUI>> exclude() {
        return Arrays.asList(FragmentShellActivity.class);
    }

    @Override
    public List<Class<? extends IBaseUI>> include() {
        return null;
    }
}
