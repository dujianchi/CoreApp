package cn.dujc.coreapp.ui.toolbar;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.dujc.core.toolbar.IStatusColor;
import cn.dujc.core.toolbar.IToolbar;
import cn.dujc.core.ui.FragmentShellActivity;
import cn.dujc.coreapp.R;
import cn.dujc.coreapp.ui.CoordinatorActivity;

/**
 * @author du
 * date 2018/10/31 1:55 PM
 */
public class ToolbarHandler {

    @IToolbar(exclude = {CoordinatorActivity.class, FragmentShellActivity.class})
    public static ViewGroup normal(ViewGroup parent) {
        return (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.toolbar, parent, false);
    }

    @IStatusColor(darkOpera = IStatusColor.DarkOpera.LIGHT, exclude = {CoordinatorActivity.class, FragmentShellActivity.class})
    public static int color(Context context) {
        return ContextCompat.getColor(context, R.color.colorPrimary);
    }
}
