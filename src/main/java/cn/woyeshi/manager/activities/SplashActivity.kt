package cn.woyeshi.manager.activities

import android.os.Bundle
import android.os.Handler
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.manager.R
import cn.woyeshi.manager.utils.Navigation

/**
 * Created by wys on 2017/11/8.
 */
class SplashActivity : BaseActivity() {

    override fun getContentLayoutID(): Int {
        return R.layout.activity_splash
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Handler().postDelayed({
            if (isFinishing) {
                return@postDelayed
            }
            Navigation.toLoginActivity(this)
            finish()
        }, 2000)
    }

    override fun isHaveTitleBar(): Boolean {
        return false
    }


}