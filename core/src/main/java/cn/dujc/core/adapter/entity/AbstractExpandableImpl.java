package cn.dujc.core.adapter.entity;

import java.io.Serializable;

public abstract class AbstractExpandableImpl<T> implements IExpandable<T>, Serializable {

    /**
     * 是否展开，由于这个字段仅供本地列表使用，所以声明为transient以使gson不序列化或反序列化这个字段。
     * 同时这个奇葩命名，是为了避免真的有的bean类真的有expanded这个字段，这个奇葩命名，肯定不会有人用。
     */
    private transient boolean _mExpanded_ = false;

    @Override
    public boolean isExpanded() {
        return _mExpanded_;
    }

    @Override
    public void setExpanded(boolean expanded) {
        _mExpanded_ = expanded;
    }
}
