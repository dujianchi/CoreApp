package cn.dujc.coreapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.dujc.core.toolbar.IStatusColor;
import cn.dujc.core.toolbar.IToolbar;

/**
 * @author du
 * date 2018/5/12 下午7:18
 */
public class ToolbarUtils {

    @IToolbar()
    public static Toolbar normal(ViewGroup viewGroup) {
        Toolbar toolbar = (Toolbar) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.toolbar, viewGroup, false);
        return toolbar;
    }

    @IStatusColor
    public int color(Context context){
        return ContextCompat.getColor(context, R.color.colorAccent);
    }
}
