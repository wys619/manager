package cn.woyeshi.manager.activities

import android.os.Bundle
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.manager.R

class FindPasswordActivity : BaseActivity() {
    override fun getContentLayoutID(): Int {
        return R.layout.activity_find_password
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        title = "找回密码"
    }
}