package cn.dujc.coreapp.ui.toolbar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.dujc.core.toolbar.IToolbar;
import cn.dujc.core.ui.IBaseUI;
import cn.dujc.coreapp.R;

/**
 * @author du
 * date 2018/10/31 1:55 PM
 */
public class ToolbarHandler implements IToolbar {

    private static final ToolbarHandler HANDLER = new ToolbarHandler();

    private ToolbarHandler() {}

    public static ToolbarHandler getHandler() {
        return HANDLER;
    }

//    @Override
//    public IToolbar get() {
//        return this;
//    }

    public View normal(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.toolbar, parent, false);
    }

    @Override
    public int statusBarColor(Context context) {
        return Color.TRANSPARENT;
    }

    @Override
    public StatusBarMode statusBarMode() {
        return StatusBarMode.DARK;
    }

    @Override
    public IBaseUI.WithToolbar.Style toolbarStyle() {
        return IBaseUI.WithToolbar.Style.LINEAR;
    }

    @Override
    public List<Class<? extends IBaseUI>> exclude() {
        return null;
    }

}
