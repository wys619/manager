package cn.woyeshi.manager.activities

import android.os.Bundle
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.manager.R
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class RegisterActivity : BaseActivity() {
    override fun getContentLayoutID(): Int {
        return R.layout.activity_register
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        title = "注册"

        //返回登录
        btnLogin.onClick {
            finish()
        }

        //提交注册
        btnRegister.onClick {

        }

    }
}