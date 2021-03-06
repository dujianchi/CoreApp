package cn.dujc.coreapp.ui;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.ui.BaseListActivity;
import cn.dujc.core.ui.BaseWebFragment;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;

/**
 * @author du
 * date 2018/10/31 1:56 PM
 */
public class MainActivity extends BaseListActivity {

    private final List<String> mList = Arrays.asList("Toolbar控制"
            , "加载更多开关"
            , "刷新开关"
            , "权限控制"
            , "多种类型的adapter"
            , "可展开的adapter"
            , "startActivityForResult返回值处理/采用代码布局/关闭其他activity测试"
            , "跳转完关闭的使用/直接使用Fragment"
            , "coordinator"
            , "自定义控件测试"
            , "webFragment"
            , "showDialogFragment"
            , "getInt"
            , "title menu 0"
            , "title menu 1"
            , "title menu 2"
            , "dialog"
            , "edittext"
            , "textview"
            , "Section", "", "", "", "", "");

    private boolean mRefreshEnable = true;
    private boolean mMoreEnable = true;
    private DialogF mDialog;
//    private DialogP mDialog;
    private Dialog2 mDialog2;

    @Nullable
    @Override
    public BaseQuickAdapter initAdapter() {
        return new ItemAdapter(mList);
    }

    @Override
    public void recyclerViewOtherSetup() {
        super.recyclerViewOtherSetup();
        if (getAdapter() != null) getAdapter().setEnableLoadMore(false);
    }

    @Override
    public void onItemClick(int position) {
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
                //permissionKeeper().requestPermissions(starter().newRequestCode(MainActivity.class), "缺少关键权限", "请从设置中打开相关权限，以进一步使用", Manifest.permission.WRITE_EXTERNAL_STORAGE);
                permissionKeeper().requestPermissions(123
                        , "缺少短信权限", "需要短信权限才能用短信分享", Manifest.permission.SEND_SMS);
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
                byte[] bytes = new byte[1024*1024*3];
                System.out.println("-- 开始生成字符串  " + System.currentTimeMillis());
                String string = Arrays.toString(bytes);
                System.out.println("-- 生成字符串  " + System.currentTimeMillis() + "  " + string);
                starter().withLargeData(ReturnActivity.KEY, Arrays.toString(bytes)).go(ReturnActivity.class);
                System.out.println("-- 完成传值  " + System.currentTimeMillis());
                break;
            }
            case 7: {
                starter().go(MiddleActivity.class);
                break;
            }
            case 8: {
                /*starter()
                        .with(FragmentShellActivity.EXTRA_STATUS_DARK_MODE, true)
                        .with(FragmentShellActivity.EXTRA_STATUS_BAR_COLOR, Color.WHITE)
                        .with(FragmentShellActivity.EXTRA_TOOLBAR_STYLE, Style.NONE)
                        .goFragment(CoordinatorFragment.class);*/
                starter().go(CoordinatorActivity.class);
                break;
            }
            case 9: {
                starter().go(CustomViewActivity.class);
                break;
            }
            case 10: {
                //starter().go(WebActvity.class);
                starter().with(BaseWebFragment.EXTRA_URL, "https://m.baidu.com")
                        .goFragment(BaseWebFragment.class);
                break;
            }
            case 11: {
                if (mDialog == null) {
                    //mDialog = new DialogP(mActivity);
                    mDialog = new DialogF();
                }
                if (!mDialog.isShowing()) mDialog.showOnly(this);
                break;
            }
            case 12: {
                try {
                    final int i = extras().get("aaa");
                    ToastUtil.showToast(mActivity, "int = " + i);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    ToastUtil.showToast(mActivity, "error", Gravity.CENTER, 0, Toast.LENGTH_LONG);
                }
                break;
            }
            case 13: {
                setTitleMenuText("aaaa", 0, null);
                break;
            }
            case 14: {
                setTitleMenuText("bbbb", 1, null);
                break;
            }
            case 15: {
                setTitleMenuIcon(R.mipmap.ic_launcher, 2, null);
                break;
            }
            case 16: {
                if (mDialog2 == null) {
                    mDialog2 = new Dialog2(mActivity);
                }
                if (!mDialog2.isShowing()) mDialog2.show();
                break;
            }
            case 17: {
                starter().go(EditTextActivity.class);
                break;
            }
            case 18: {
                starter().go(TextActivity.class);
                break;
            }
            case 19: {
                starter().go(SectionActivity.class);
                break;
            }
            //case xx: {setTitleMenuText("bbbb", 1, onClickListener);break;}
            default: {
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //MainBackPressed.onBackPressed(this);
    }

    @Override
    public void loadMore() {
        ToastUtil.showToast(mActivity, "在这加载更多");
        notifyDataSetChanged(true, false);
    }

    @Override
    public void reload() {
        ToastUtil.showToast(mActivity, "在这刷新");
        notifyDataSetChanged(false, false);
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
