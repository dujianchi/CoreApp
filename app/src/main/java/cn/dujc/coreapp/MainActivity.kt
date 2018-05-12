package cn.dujc.coreapp

import android.Manifest
import android.os.Bundle
import android.view.View
import cn.dujc.core.permission.AppSettingsDialog
import cn.dujc.core.ui.BaseActivity
import cn.dujc.core.ui.TitleCompat
import cn.dujc.core.util.ToastUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun getViewId() = R.layout.activity_main_fragment//activity_main

    override fun initBasic(savedInstanceState: Bundle?) {
        setTitle("asdf")
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment())
                .commit()
    }

    override fun initTransStatusBar(): TitleCompat? {
        return TitleCompat.setStatusBar(mActivity, tOn, fOn)//.setFakeStatusBarColorId(R.color.colorPrimaryDark)
    }

    /*override fun initToolbar(parent: ViewGroup?): Toolbar? {
        return (layoutInflater.inflate(R.layout.toolbar, parent, false) as? Toolbar)
    }*/

    private var tOn = false
    private var fOn = false
    private var lOn = true
    private var pOn = true

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

    fun placeholderSwitch(v: View) {
        pOn = !pOn
        sbp_placeholder.placeholder(pOn)
    }

    fun permissionSwitch(v: View) {
        //1 检查权限
//        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        if (permission == PackageManager.PERMISSION_GRANTED) {
//            ToastUtil.showToast(mActivity, "有权限")
//        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true
//            AppSettingsDialog.Builder(this).build().show()
//        } else {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), starter().newRequestCode(AppSettingsDialogHolderActivity::class.java))
//        }

        permissionKeeper().requestPermissions(123, "权限设置", "需要一些权限才能正常使用", Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun onGranted(requestCode: Int, permission: MutableList<String>?) {
        super.onGranted(requestCode, permission)
        ToastUtil.showToast(mActivity, "granted: ", permission)
    }

    override fun onDenied(requestCode: Int, permissions: MutableList<String>?) {
        super.onDenied(requestCode, permissions)
        ToastUtil.showToast(mActivity, "denied: ", permissions)
    }

    fun settingsSwitch(v: View) {
        AppSettingsDialog.Builder(this).setRationale("123321").build().show()
    }
}
