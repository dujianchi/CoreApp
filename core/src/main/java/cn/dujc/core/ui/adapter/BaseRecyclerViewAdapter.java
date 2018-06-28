package cn.dujc.core.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * 只是随手写的recyclerview的适配器，基本思路是参照印象中的BRVAH。
 * <i>做了一些简化修改，比如，需要多种布局的情况，item view type即所设置的layout的id</i>
 * <p>需要注意的是：这个header和footer的处理，我随便写的，<b>还未测试，有使用到header、footer的时候要多测试一下</b></p>
 *
 * @param <T> 数据类型
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private static final int HEADER_TYPE = -1, FOOTER_TYPE = -2;

    private List<T> mDataList = null;
    private MultiTypeDelegate<T> mMultiTypeDelegate = null;
    private LinearLayout mHeaderView = null, mFooterView = null;
    private OnItemClickListener mOnItemClickListener = null;

    protected Context mContext = null;

    public BaseRecyclerViewAdapter() {
    }

    public BaseRecyclerViewAdapter(List<T> dataList) {
        mDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView;
        if (viewType == HEADER_TYPE) itemView = mHeaderView;
        else if (viewType == FOOTER_TYPE) itemView = mFooterView;
        else itemView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        final ViewHolder viewHolder = new ViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(BaseRecyclerViewAdapter.this
                            , v, viewHolder.getAdapterPosition() - getHeaderViewCount());
                }
            }
        });
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        int count = getDataCount();
        count += getHeaderViewCount();
        count += getFooterViewCount();
        return count;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        bindViewHolder(holder, getItemData(position));
    }

    @Override
    public int getItemViewType(int position) {
        final int headerViewCount = getHeaderViewCount();
        final int dataCount = getDataCount();
        if (headerViewCount != 0 && position == 0) {
            return HEADER_TYPE;
        } else if (getFooterViewCount() != 0 && position == dataCount + headerViewCount - 1) {
            return FOOTER_TYPE;
        } else if (mMultiTypeDelegate != null) {
            return mMultiTypeDelegate.getItemTypeLayout(getItemData(position));
        } else {
            return super.getItemViewType(position);
        }
    }

    public void addHeaderView(View headerView) {
        if (headerView == null) return;
        if (mHeaderView == null) {
            LinearLayout headerLayout = new LinearLayout(headerView.getContext());
            headerLayout.setOrientation(LinearLayout.VERTICAL);
            headerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT));
            mHeaderView = headerLayout;
        }
        final ViewGroup headerViewParent = (ViewGroup) headerView.getParent();
        if (headerViewParent != null) {
            headerViewParent.removeView(headerView);
        }
        mHeaderView.addView(headerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        notifyItemChanged(0);
    }

    public void removeHeaderView(View headerView) {
        if (headerView == null || mHeaderView == null) return;
        mHeaderView.removeView(headerView);
        notifyItemRemoved(0);
    }

    public void addFooterView(View footerView) {
        if (footerView == null) return;
        if (mFooterView == null) {
            LinearLayout footerLayout = new LinearLayout(footerView.getContext());
            footerLayout.setOrientation(LinearLayout.VERTICAL);
            footerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT));
            mFooterView = footerLayout;
        }
        final ViewGroup footerViewParent = (ViewGroup) footerView.getParent();
        if (footerViewParent != null) {
            footerViewParent.removeView(footerView);
        }
        mFooterView.addView(footerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        notifyItemChanged(getItemCount() - 1);
    }

    public void removeFooterView(View footerView) {
        if (footerView == null || mFooterView == null) return;
        mFooterView.removeView(footerView);
        notifyItemRemoved(getItemCount() - 1);
    }

    public void setDataList(List<T> dataList) {
        mDataList = dataList;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 这个方法是获取列表中数据的
     *
     * @param positionMayIncludeHeaderCount 传进来的position会被扣掉header的数量
     */
    public T getItemData(int positionMayIncludeHeaderCount) {
        return getItem(positionMayIncludeHeaderCount - getHeaderViewCount());
    }

    /**
     * 这个方法是获取列表中数据的
     *
     * @param positionExcludeHeaderCount 传进来多少就取第几个，默认当作已经扣去header数量
     */
    public T getItem(int positionExcludeHeaderCount) {
        final int dataSize = getDataCount();
        if (positionExcludeHeaderCount < 0 || positionExcludeHeaderCount >= dataSize) return null;
        return mDataList.get(positionExcludeHeaderCount);
    }

    public int getDataCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public int getHeaderViewCount() {
        return mHeaderView == null || mHeaderView.getChildCount() == 0 ? 0 : 1;
    }

    public int getFooterViewCount() {
        return mFooterView == null || mFooterView.getChildCount() == 0 ? 0 : 1;
    }

    public void setMultiTypeDelegate(MultiTypeDelegate<T> multiTypeDelegate) {
        mMultiTypeDelegate = multiTypeDelegate;
    }

    public MultiTypeDelegate<T> getMultiTypeDelegate() {
        return mMultiTypeDelegate;
    }

    public abstract void bindViewHolder(ViewHolder holder, T item);

}
