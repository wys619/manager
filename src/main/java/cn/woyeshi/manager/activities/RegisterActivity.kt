package cn.woyeshi.manager.activities

import android.os.Bundle
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.manager.R

class RegisterActivity : BaseActivity() {
    override fun getContentLayoutID(): Int {
        return R.layout.activity_register
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        title = "注册"
    }
}