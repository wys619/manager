package cn.woyeshi.manager.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.manager.R
import cn.woyeshi.manager.utils.Navigation

/**
 * Created by wys on 2017/11/8.
 */
class SplashActivity : BaseActivity() {

    private val REQUEST_CODE_PERMISSIONS = 1001
    private val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE)
    private var backFromSettingActivity = false

    override fun getContentLayoutID(): Int {
        return R.layout.activity_splash
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        checkPermissionOrSkip()
    }

    override fun onResume() {
        super.onResume()
        if (backFromSettingActivity) {
            backFromSettingActivity = false
            checkPermissionOrSkip()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionsResult()")
        when (requestCode) {
            REQUEST_CODE_PERMISSIONS -> {
                onPermissionsResult(permissions, grantResults)
            }
        }
    }

    private fun onPermissionsResult(permissions: Array<out String>, grantResults: IntArray) {
        for (i in permissions.indices) {
            when (permissions[i]) {
                Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                    if (checkStoragePermission(grantResults, i)) return
                }
                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    if (checkLocationPermission(grantResults, i)) return
                }
                Manifest.permission.READ_PHONE_STATE -> {
                    if (checkPhoneStatePermission(grantResults, i)) return
                }
            }
        }
        checkToSkip()
    }

    private fun checkPhoneStatePermission(grantResults: IntArray, i: Int): Boolean {
        if (PackageManager.PERMISSION_GRANTED != grantResults[i]) {
            showAlert(getString(R.string.string_manage_phone_permission), getString(R.string.string_to_open)) {
                Navigation.toAppDefaultSetting(this)
                backFromSettingActivity = true
            }
            return true
        }
        return false
    }

    private fun checkLocationPermission(grantResults: IntArray, i: Int): Boolean {
        if (PackageManager.PERMISSION_GRANTED != grantResults[i]) {
            showAlert(getString(R.string.string_location_permission_hint), getString(R.string.string_to_open)) {
                Navigation.toAppDefaultSetting(this)
                backFromSettingActivity = true
            }
            return true
        }
        return false
    }

    private fun checkStoragePermission(grantResults: IntArray, i: Int): Boolean {
        if (PackageManager.PERMISSION_GRANTED != grantResults[i]) {
            showAlert(getString(R.string.string_write_sd_card), getString(R.string.string_to_open)) {
                Navigation.toAppDefaultSetting(this)
                backFromSettingActivity = true
            }
            return true
        }
        return false
    }


    private fun checkPermissionOrSkip() {
        if (isMOrLater() && !checkPermissions(this, permissions)) {       //如果是6.0或以上，并且没有全部权限授予，就请求权限
            requestPermissions(permissions, REQUEST_CODE_PERMISSIONS)
        } else {                                                                                                //安卓6.0一下直接执行跳转逻辑
            checkToSkip()
        }
    }

    override fun isHaveTitleBar(): Boolean {
        return false
    }

    private fun checkToSkip() {
        Handler().postDelayed({
            if (isFinishing) {
                return@postDelayed
            }
            Navigation.toLoginActivity(this)
            finish()
        }, 1000)
    }


}