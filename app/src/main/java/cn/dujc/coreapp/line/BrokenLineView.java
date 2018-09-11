package cn.dujc.coreapp.line;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xk on 2016/11/9 22:14.
 */

public class BrokenLineView extends RecyclerView {

    private int maxValue;
    private int minValue;
    private List<Integer> data = new ArrayList<>();
    private Adapter adapter;

    public BrokenLineView(Context context) {
        this(context, null);
    }

    public BrokenLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new Adapter();
        setAdapter(adapter);
    }

    public void setData(List<Integer> d) {
        if (data != null) {
            data.clear();
            data.addAll(d);
            Collections.sort(d);
            minValue = d.get(0);
            maxValue = d.get(d.size() - 1);
            adapter.notifyDataSetChanged();
        }
    }

    class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Item item = new Item(getContext());
            item.setMinValue(minValue);
            item.setMaxValue(maxValue);
            LayoutParams layoutParams = new LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT);//这个数字表示每一个item的宽度
            item.setLayoutParams(layoutParams);
            return new ViewHolder(item);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (position == 0) {
                holder.item.setDrawLeftLine(false);
            } else {
                holder.item.setDrawLeftLine(true);
                holder.item.setlastValue((data.get(position - 1)));
            }
            holder.item.setCurrentValue((data.get(position)));


            if (position == data.size() - 1) {
                holder.item.setDrawRightLine(false);
            } else {
                holder.item.setDrawRightLine(true);
                holder.item.setNextValue((data.get(position + 1)));
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            Item item;

            public ViewHolder(View itemView) {
                super(itemView);
                this.item = (Item) itemView;
            }
        }
    }


}
