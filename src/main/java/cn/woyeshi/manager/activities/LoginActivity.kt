package cn.woyeshi.manager.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.entity.Constants
import cn.woyeshi.entity.annotations.Autowired
import cn.woyeshi.entity.beans.manager.LoginInfo
import cn.woyeshi.entity.utils.Logger
import cn.woyeshi.manager.R
import cn.woyeshi.manager.utils.Navigation
import cn.woyeshi.presenterimpl.presenters.ILoginPresenter
import cn.woyeshi.presenterimpl.presenters.ILoginView
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity(), ILoginView {

    companion object {
        val REQUEST_CODE_TO_FIND_PWD_ACTIVITY = 1001
        val REQUEST_CODE_TO_REGISTER_ACTIVITY = 1002
    }

    @Autowired
    private var loginPresenter: ILoginPresenter<ILoginView>? = null

    override fun getContentLayoutID(): Int {
        return R.layout.activity_login
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        title = "欢迎登录"
        setBackBtnVisibility(false)
        inputLayout1.addTextWatcher(textWatcher)
        inputLayout2.addTextWatcher(textWatcher)

        //忘记密码
        btnForgetPwd.onClick {
            Navigation.toFindPwdActivityForResult(REQUEST_CODE_TO_FIND_PWD_ACTIVITY, this@LoginActivity)
        }

        //注册
        btnNext.onClick {
            Navigation.toRegisterActivityForResult(REQUEST_CODE_TO_REGISTER_ACTIVITY, this@LoginActivity)
        }

        //登录
        btnLogin.onClick {
            val userName = inputLayout1.getText().trim()
            val password = inputLayout2.getText().trim()
            loginPresenter?.login(userName, password)
        }
    }

    override fun onLoginRequestSuccess(loginInfo: LoginInfo) {
        Logger.i(TAG, "onLoginRequestSuccess() -> $loginInfo")
        when (loginInfo.code) {
            Constants.RESULT_CODE_SUCCESS -> {
                toast("登录成功")
                Navigation.toMainActivity(this)
                finish()
            }
        }
    }

    override fun onLoginRequestError(e: Throwable) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_TO_FIND_PWD_ACTIVITY -> {          //找回密码界面回调

                }
                REQUEST_CODE_TO_REGISTER_ACTIVITY -> {          //注册界面回调

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loginPresenter?.onDestroy()
        loginPresenter = null
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val userName = inputLayout1.getText()
            val password = inputLayout2.getText()
            btnLogin.isEnabled = !TextUtils.isEmpty(userName.trim()) && !TextUtils.isEmpty(password.trim())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }

}