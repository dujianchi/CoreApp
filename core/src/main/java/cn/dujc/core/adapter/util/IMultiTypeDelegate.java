package cn.dujc.core.adapter.util;

import java.util.List;

/**
 * 多种布局样式代理接口，原类{@link MultiTypeDelegate}，我自己的实现的id与type相同{@link TypeAsIdDelegate}
 */
public interface IMultiTypeDelegate<T> {
    public int getDefItemViewType(List<T> data, int position);

    public int getLayoutId(int viewType);
}
