package cn.woyeshi.manager.activities

import android.os.Bundle
import android.os.Handler
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.entity.utils.Logger
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

    private var isFirstClick = true
    override fun onBackBtnClick() {
        if (isFirstClick) {
            toast(getString(R.string.back_press_tips))
            isFirstClick = false
            postToMainDelayed(2500) {
                isFirstClick = true
                Logger.i(TAG, "resume firstClick")
            }
            return
        }
        isFirstClick = true
        moveTaskToBack(true)
    }

    private fun postToMainDelayed(i: Int, function: () -> Unit) {
        Handler().postDelayed({
            function()
        }, i.toLong())
    }

}
