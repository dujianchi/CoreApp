package cn.dujc.core.adapter;

import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author du
 * date 2018/5/11 上午12:51
 */
public abstract class BaseAdapterL<T> extends BaseAdapter {

    private List<T> mList;

    public BaseAdapterL() {}

    public BaseAdapterL(List<T> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public T getItem(int position) {
        if (0 <= position && position < getCount()) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = createView(parent);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convert(getItem(position), position, holder);
        return convertView;
    }

    public void addData(T data) {
        if (data != null) {
            if (mList != null) {
                mList.add(data);
            } else {
                mList = new ArrayList<>();
                mList.add(data);
            }
        }
    }

    public void addAllData(List<T> list) {
        if (list != null && list.size() > 0) {
            if (mList != null) {
                mList.addAll(list);
            } else {
                mList = new ArrayList<>();
                mList.addAll(list);
            }
        }
    }

    public void clearData() {
        if (mList != null) {
            mList.clear();
        }
    }

    public void resetData(List<T> list) {
        resetData(list, false);
    }

    public void resetData(List<T> list, boolean clearBefore) {
        if (clearBefore) clearData();
        mList = list;
    }

    public abstract View createView(ViewGroup parent);

    public abstract void convert(T item, int position, ViewHolder holder);

    public static class ViewHolder {
        private final View convertView;
        private final SparseArray<View> views = new SparseArray<>();

        public ViewHolder(View convertView) {
            this.convertView = convertView;
        }

        public View getConvertView() {
            return convertView;
        }

        public <T> T getView(@IdRes int id) {
            View view = views.get(id);
            if (view == null) {
                view = convertView.findViewById(id);
                views.put(id, view);
            }
            return (T) view;
        }
    }
}
