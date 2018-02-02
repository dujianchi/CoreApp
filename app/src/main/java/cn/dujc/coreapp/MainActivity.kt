package cn.dujc.coreapp

import android.os.Bundle
import cn.dujc.core.ui.BaseActivity
import cn.dujc.core.ui.BaseWebFragment

class MainActivity : BaseActivity() {

    override fun getViewId() = R.layout.activity_main

    override fun initBasic(savedInstanceState: Bundle?) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, BaseWebFragment()).commit()
    }

}
