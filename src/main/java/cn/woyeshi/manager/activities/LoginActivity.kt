package cn.woyeshi.manager.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.entity.Constants
import cn.woyeshi.entity.beans.manager.UserInfo
import cn.woyeshi.entity.utils.Logger
import cn.woyeshi.entity.utils.MD5
import cn.woyeshi.manager.R
import cn.woyeshi.manager.utils.Navigation
import cn.woyeshi.presenterimpl.presenters.IUserView
import cn.woyeshi.presenterimpl.presenters.UserPresenter
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class LoginActivity : BaseActivity(), IUserView {
    override fun onRegisterSuccess(t: UserInfo) {


    }

    override fun onUpdateUserSuccess() {

    }

    companion object {
        val REQUEST_CODE_TO_FIND_PWD_ACTIVITY = 1001
        val REQUEST_CODE_TO_REGISTER_ACTIVITY = 1002
    }

    private var userPresenter: UserPresenter<IUserView> = UserPresenter(this)

    override fun getContentLayoutID(): Int {
        return R.layout.activity_login
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        title = getString(R.string.title_login_activity)
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
        btnLogin.setOnClickListener(View.OnClickListener {
            val userName = inputLayout1.getText().trim()
            val password = inputLayout2.getText().trim()
            if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                toast(getString(R.string.hint_input_username_and_pwd_first))
                return@OnClickListener
            }
            toLogin(userName, MD5.getMD5(password.toByteArray())!!)
        })
//        val loginInfo = readFromSP(Constants.SPKeys.KEY_LOGIN_USER_INFO, UserInfo::class.java)
//        if (loginInfo != null) {
//            toLogin(loginInfo.userName, loginInfo.password)
//        }
    }

    private fun toLogin(userName: String, pwd: String) {
        showLoading(getString(R.string.hint_login_ing), false)
        userPresenter.login(userName, pwd)
    }

    //登录成功
    override fun onLoginRequestSuccess(loginInfo: UserInfo) {
        Handler().postDelayed({
            Logger.i(TAG, "onLoginRequestSuccess() -> $loginInfo")
            saveToSP(Constants.SPKeys.KEY_LOGIN_USER_INFO, loginInfo)
            toast(getString(R.string.string_login_success))
            Navigation.toMainActivity(this)
            finish()
        }, 1000L)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_TO_FIND_PWD_ACTIVITY -> {          //找回密码界面回调

                }
                REQUEST_CODE_TO_REGISTER_ACTIVITY -> {          //注册界面回调
                    val userInfo = readFromSP(Constants.SPKeys.KEY_LOGIN_USER_INFO, UserInfo::class.java)
                    if (userInfo != null) {
                        userPresenter.login(userInfo.userName, userInfo.password)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        userPresenter.onDestroy()
        hideLoading()
        super.onDestroy()
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