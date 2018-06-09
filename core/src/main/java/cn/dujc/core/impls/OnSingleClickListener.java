package cn.dujc.core.impls;

import android.util.SparseArray;
import android.view.View;

/**
 * @author du
 * date 2018/6/9 下午1:37
 */
public abstract class OnSingleClickListener implements View.OnClickListener {

    public static final int DEFAULT_INTERVAL = 1000;
    private final SparseArray<Long> clickTimes = new SparseArray<Long>();
    private int interval = DEFAULT_INTERVAL;
    private boolean updateTime = false;

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        final Long timeL = clickTimes.get(id);
        final long current = System.currentTimeMillis();
        if (timeL == null || timeL < current - interval) {
            onSingleClick(v);
            clickTimes.put(id, current);
        }else if (updateTime){
            clickTimes.put(id, current);
        }
    }

    public int getInterval() {
        return interval;
    }

    /**
     * 设置单击的间隔时间，在此间隔内的点击都会被忽略
     * @param interval 间隔时间，单位毫秒，1秒=1000
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean isUpdateTime() {
        return updateTime;
    }

    /**
     * 设置是否每次点击都更新时间，即点击后不管触不触发单击事件，事件都会往后延长一个间隔时间
     * @param updateTime 是否在不触发的时候也更新时间
     */
    public void setUpdateTime(boolean updateTime) {
        this.updateTime = updateTime;
    }

    public abstract void onSingleClick(View v);
}
