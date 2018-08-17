package cn.woyeshi.manager.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import cn.woyeshi.manager.activities.*

object Navigation {

    fun toMainActivity(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
    }

    fun toLoginActivity(activity: Activity) {
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
    }

    fun toFindPwdActivityForResult(requestCode: Int, activity: Activity) {
        val intent = Intent(activity, FindPasswordActivity::class.java)
        activity.startActivityForResult(intent, requestCode)
    }

    fun toRegisterActivityForResult(requestCode: Int, activity: Activity) {
        val intent = Intent(activity, RegisterActivity::class.java)
        activity.startActivityForResult(intent, requestCode)
    }

    fun toNextRegisterActivity(activity: Activity) {
        val intent = Intent(activity, RegisterNextActivity::class.java)
        activity.startActivity(intent)
    }

    fun toAppDefaultSetting(activity: Activity) {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        localIntent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivity(localIntent)
    }

}