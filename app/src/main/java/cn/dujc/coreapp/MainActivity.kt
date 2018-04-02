package cn.dujc.coreapp

import android.content.Intent
import android.os.Bundle
import cn.dujc.core.ui.BaseActivity
import cn.dujc.core.ui.BaseWebFragment
import cn.dujc.core.util.LogUtil
import cn.dujc.core.util.ToastUtil
import cn.dujc.core.widget.NormalMainCreator

class MainActivity : BaseActivity() {

    override fun getViewId() = R.layout.activity_main

    override fun initBasic(savedInstanceState: Bundle?) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, BaseWebFragment()).commit()

        //val go = go(Activity2::class.java)
        //println("main go = $go")

        val first = ItemView(mActivity, "aaa").setSelected(true)
        val second = ItemView(mActivity, "bbb")
        //rm_menu.addItem(first)
        //rm_menu.addItem(second)

        NormalMainCreator.create(R.id.fl_container, R.id.rm_menu)
                .add(first, Fragment0())
                .add(second, Fragment1())
                .into(this);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (starter().getRequestCode(Activity3::class.java) == requestCode){
            ToastUtil.showToast(mActivity, "return activity")
            LogUtil.d("return activity")
        }
    }

}
