package cn.dujc.coreapp.ui;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.ui.BaseListActivity;
import cn.dujc.core.util.ToastUtil;

/**
 * @author du
 * date 2018/10/31 1:56 PM
 */
public class MainActivity extends BaseListActivity {

    private final List<String> mList = Arrays.asList(
            "Toolbar控制"
            , "加载更多开关"
            , "刷新开关"
            , "权限控制"
            , "多种类型的adapter"
            , "可展开的adapter"
            , "startActivityForResult返回值处理/采用代码布局"
            , "跳转完关闭的使用/直接使用Fragment"
            , "coordinator"
            , ""
            , ""
            , ""
            , ""
            , ""
            , ""
            , ""
            , ""
            , ""
            , ""
            , ""
            , ""
            , ""
    );

    private boolean mRefreshEnable = true;
    private boolean mMoreEnable = true;

    @Nullable
    @Override
    protected BaseQuickAdapter initAdapter() {
        return new ItemAdapter(mList);
    }

    @Override
    protected void onItemClick(int position) {
        switch (position) {
            case 0: {
                starter().go(ToolbarCtrlActivity.class);
                break;
            }
            case 1: {
                getAdapter().setEnableLoadMore(mMoreEnable = !mMoreEnable);
                break;
            }
            case 2: {
                getSwipeRefreshLayout().setEnabled(mRefreshEnable = !mRefreshEnable);
                break;
            }
            case 3: {
                permissionKeeper().requestPermissions(starter().newRequestCode(MainActivity.class)
                        , "缺少关键权限", "请从设置中打开相关权限，以进一步使用"
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
            }
            case 4: {
                starter().go(MultiTypeListActivity.class);
                break;
            }
            case 5: {
                starter().go(ExpendListActivity.class);
                break;
            }
            case 6: {
                starter().with(ReturnActivity.KEY, "From MainActivity")
                        .go(ReturnActivity.class);
                break;
            }
            case 7: {
                starter().go(MiddleActivity.class);
                break;
            }
            case 8: {
                starter().go(CoordinatorActivity.class);
                break;
            }
            //case xx: {break;}
            default: {break;}
        }
    }

    @Override
    protected void loadMore() {
        ToastUtil.showToast(mActivity, "在这加载更多");
        loadDone(true, true);
    }

    @Override
    protected void reload() {
        ToastUtil.showToast(mActivity, "在这刷新");
        refreshDone();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK && requestCode == starter().getRequestCode(ReturnActivity.class)) {
            final String extra = data.getStringExtra(ReturnActivity.KEY);
            ToastUtil.showToast(mActivity, extra);
        }
    }

    @Override
    public void onGranted(int requestCode, List<String> permissions) {
        ToastUtil.showToast(mActivity, "有权限");
    }

    @Override
    public void onDenied(int requestCode, List<String> permissions) {
        ToastUtil.showToast(mActivity, "无权限");
    }

    public static class ItemAdapter extends BaseAdapter<String> {
        public ItemAdapter(@Nullable List<String> data) {
            super(data);
            mLayoutResId = android.R.layout.simple_list_item_1;
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(android.R.id.text1, item);
        }
    }
}