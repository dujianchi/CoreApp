package cn.dujc.coreapp;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.adapter.entity.IExpandable;
import cn.dujc.core.adapter.util.MultiTypeDelegate;

/**
 * 这个adapter即最外层的adapter，必须实现BaseAdapter，然后数据类型是IExpandable
 * @author du
 * date 2018/9/7 下午3:46
 */
public class Main2Adapter extends BaseAdapter<IExpandable> {

    public Main2Adapter(@Nullable List<IExpandable> data) {
        super(data);
        //多种类型的数据，必须实现以下全部代码
        final MultiTypeDelegate<IExpandable> delegate = new MultiTypeDelegate<IExpandable>() {
            @Override
            protected int getItemType(IExpandable o) {
                return o.getLevel();//因为实体设置了不同的level，所以这边返回level就行
            }
        };
        delegate.registerItemType(0, R.layout.item_level0);//即为前面实体类的那个level
        delegate.registerItemType(1, R.layout.item_level1);//即为前面实体类的那个level
        setMultiTypeDelegate(delegate);
    }

    @Override
    protected void convert(BaseViewHolder helper, IExpandable item) {
        if (helper.getItemViewType() == 0) {//即为上面的0或1
            ((TextView) helper.itemView).setText(((Main2Entity)item).getTitle());
            helper.itemView.setOnClickListener(new View.OnClickListener() {//最外层要实现展开与关闭，所以需要设置点击
                @Override
                public void onClick(View v) {
                    if (item.isExpanded()) collapse(helper.getAdapterPosition());
                    else expand(helper.getAdapterPosition());
                }
            });
        } else {//二级的文字设置
            ((TextView) helper.itemView).setText(((Main2Entity.InnerEntity)item).getChild());
        }
    }
}
