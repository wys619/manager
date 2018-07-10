package cn.woyeshi.manager.activities

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.TextView
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.entity.Constants
import cn.woyeshi.entity.beans.manager.UserInfo
import cn.woyeshi.entity.utils.MD5
import cn.woyeshi.entity.utils.PhoneUtils
import cn.woyeshi.manager.R
import cn.woyeshi.manager.utils.Navigation
import cn.woyeshi.presenterimpl.presenters.IRegisterView
import cn.woyeshi.presenterimpl.presenters.RegisterPresenter
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*

class RegisterActivity : BaseActivity(), IRegisterView {

    private val registerPresenter: RegisterPresenter<IRegisterView> = RegisterPresenter(this)
    private var timerHandler: TimerHandler? = null

    companion object {

        class TimerHandler(private val tv: TextView) : Handler() {

            private var timer: Timer? = null
            private var timerTask: TimerTask? = null
            private var counter: Int = 59


            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                tv.text = "${counter}S"
                counter--
                if (counter == 0) {
                    tv.isEnabled = true
                    tv.text = tv.context.getString(R.string.string_get_verify_code)
                    stopTimer()
                }
            }

            fun startTimer() {
                if (timer != null) {
                    return
                }
                counter = 59
                tv.isEnabled = false
                timerTask = object : TimerTask() {
                    override fun run() {
                        sendEmptyMessage(0)
                    }
                }
                timer = Timer()
                timer?.schedule(timerTask, 0, 1000)
            }

            private fun stopTimer() {
                timerTask?.cancel()
                timer?.cancel()
                removeCallbacksAndMessages(null)
                timer = null
                timerTask = null
            }

        }
    }

    override fun getContentLayoutID(): Int {
        return R.layout.activity_register
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initViews()
    }

    override fun onBackBtnClick() {
        Navigation.toLoginActivity(this)
        finish()
    }


    private fun initViews() {
        title = "注册"

        //返回登录
        btnLogin.onClick {
            Navigation.toLoginActivity(this@RegisterActivity)
        }

        //提交注册
        btnNext.onClick {
            val phone = inputLayout1.getText()
            if (TextUtils.isEmpty(phone)) {
                toast("请先输入手机号码")
                return@onClick
            } else if (!PhoneUtils.isMobile(phone)) {
                toast("请输入正确的手机号码")
                return@onClick
            }
            val pwd = inputLayout2.getText()
            if (TextUtils.isEmpty(pwd)) {
                toast("请先设置密码")
                return@onClick
            }
            val code = inputLayout3.getText()
            if (TextUtils.isEmpty(code)) {
                toast("请先设置密码")
                return@onClick
            }
            val md5 = MD5.getMD5(pwd.toByteArray())
            registerPresenter.register(phone, md5!!, code)
        }

        //获取验证码
        btnGetVerifyCode.onClick {
            val phone = inputLayout1.getText()
            if (TextUtils.isEmpty(phone)) {
                toast("请先输入手机号码")
                return@onClick
            } else if (!PhoneUtils.isMobile(phone)) {
                toast("请输入正确的手机号码")
                return@onClick
            }
            registerPresenter.getVerifyCode(phone, "${Constants.CODE_TYPE_REGISTER}")
        }
        inputLayout1.addTextWatcher(textWatcher)
        inputLayout2.addTextWatcher(textWatcher)
        inputLayout3.addTextWatcher(textWatcher)
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val phone = inputLayout1.getText()
            val pwd = inputLayout2.getText()
            val code = inputLayout3.getText()
            btnNext.isEnabled = !(TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(code))
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onVerifyCodeGetSuccess() {
        toast("验证码发送成功")
        if (timerHandler == null) {
            timerHandler = TimerHandler(btnGetVerifyCode)
        }
        timerHandler?.startTimer()
    }

    override fun onRegisterSuccess(t: UserInfo) {
        toast("注册成功")
        saveToSP(Constants.SPKeys.KEY_LOGIN_USER_INFO, t)
        Navigation.toNextRegisterActivity(this)
        finish()
    }

}