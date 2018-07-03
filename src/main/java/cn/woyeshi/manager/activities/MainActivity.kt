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
    }

    private fun initViews() {
        title = "go购够"
        setBackBtnVisibility(false)
    }

}
