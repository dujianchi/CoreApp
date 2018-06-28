package cn.dujc.core.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public class ViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> mViews = new SparseArray<>();

    public ViewHolder(View itemView) {
        super(itemView);
    }

    public <T> T getView(int resId){
        View view = mViews.get(resId);
        if (view == null) {
            view = itemView.findViewById(resId);
            mViews.put(resId, view);
        }
        return (T) view;
    }
}
