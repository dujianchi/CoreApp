package cn.dujc.coreapp

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import cn.dujc.core.ui.BaseActivity
import cn.dujc.core.ui.TitleCompat
import cn.dujc.core.util.ToastUtil

class MainActivity : BaseActivity() {

    override fun getViewId() = R.layout.activity_main

    override fun initToolbar(parent: ViewGroup?): Toolbar? {
        val toolbar = layoutInflater.inflate(R.layout.toolbar, parent, false)
        val toolbar1 = toolbar as? Toolbar
        return toolbar1
    }

    override fun initBasic(savedInstanceState: Bundle?) {

    }

    override fun initTransStatusBar(): TitleCompat? {
        return TitleCompat.setStatusBar(mActivity, tOn, fOn)//.setFakeStatusBarColorId(R.color.colorPrimaryDark)
    }



    private var tOn = true
    private var fOn = false
    private var lOn = true

    fun translateSwitch(v: View) {
        tOn = !tOn
        titleCompat?.setTranslucentStatus(tOn)
    }

    fun lightSwitch(v: View) {
        lOn = !lOn
        ToastUtil.showToast(mActivity, lOn)
        titleCompat?.setStatusBarMode(lOn)
    }
    fun fitSwitch(v: View) {
        fOn = !fOn
        titleCompat?.setContentFits(fOn)
    }
}
