package cn.dujc.core.adapter.util;

import android.support.annotation.LayoutRes;

import java.util.List;

import cn.dujc.core.R;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;

/**
 * 以id作为type的delegate
 */
public abstract class TypeAsIdDelegate<T> implements IMultiTypeDelegate<T> {

    @Override
    public final int getDefItemViewType(List<T> data, int position) {
        T item = data.get(position);
        return item != null ? getItemLayoutId(item) : R.layout.core_adapter_just_in_case;
    }

    @Override
    public final int getLayoutId(int viewType) {
        return viewType;
    }

    /**
     * 获取当前item对应的layoutId
     */
    @LayoutRes
    protected abstract int getItemLayoutId(T data);

    public final void setup(BaseQuickAdapter<T, ? extends BaseViewHolder> adapter) {
        if (adapter != null) adapter.setMultiTypeDelegate(this);
    }
}
