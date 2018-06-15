package cn.woyeshi.manager.activities

import android.os.Bundle
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.manager.R

class MainActivity : BaseActivity() {

    override fun getContentLayoutID(): Int {
        return R.layout.activity_main
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initViews()
        showLoading("正在处理……", true)
    }

    private fun initViews() {
        title = "Gogogo管理终端"
        setBackBtnVisibility(false)
    }

}
