package cn.dujc.coreapp;

import java.util.Arrays;
import java.util.List;

import cn.dujc.core.adapter.entity.IExpandable;

/**
 * 数据实体实现IExpandable，范型即展开后内部的类型
 * @author du
 * date 2018/9/7 下午3:46
 */
public class Main2Entity implements IExpandable<Main2Entity.InnerEntity> {

    //这个即展开后的数据，即二级列表，同样需要实现IExpandable
    public static class InnerEntity implements IExpandable<String>{

        public InnerEntity(String child) {
            this.child = child;
        }

        private final String child;

        public String getChild() {
            return child;
        }

        @Override
        public boolean isExpanded() {//没有下一级，这个返回false
            return false;
        }

        @Override
        public void setExpanded(boolean expanded) { }

        @Override
        public List<String> getSubItems() {
            return null;
        }

        @Override
        public int getLevel() {
            return 1;//level要与顶级不同
        }
    }

    public Main2Entity(String title, InnerEntity... child) {
        this.title = title;
        this.child = Arrays.asList(child);
    }

    private boolean mExpanded;//因为这个是顶级，有展开与否的状态，所以需要存放状态

    private String title;
    private List<InnerEntity> child;

    public String getTitle() {
        return title;
    }

    public List<InnerEntity> getChild() {
        return child;
    }

    @Override
    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    @Override
    public List<InnerEntity> getSubItems() {
        return child;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
