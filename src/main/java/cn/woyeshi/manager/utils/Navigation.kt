package cn.woyeshi.manager.utils

import android.app.Activity
import android.content.Intent
import cn.woyeshi.manager.activities.*

class Navigation {
    companion object {

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

    }
}