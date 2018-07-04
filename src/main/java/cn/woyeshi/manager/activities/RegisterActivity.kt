package cn.woyeshi.manager.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.entity.annotations.Autowired
import cn.woyeshi.manager.R
import cn.woyeshi.presenterimpl.presenters.IRegisterPresenter
import cn.woyeshi.presenterimpl.presenters.IRegisterView
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class RegisterActivity : BaseActivity(), IRegisterView {

    @Autowired
    private var registerPresenter: IRegisterPresenter<IRegisterView>? = null

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
        btnNext.onClick {

        }

        //获取验证码
        btnGetVerifyCode.onClick {
            val phone = inputLayout1.getText()
            if (TextUtils.isEmpty(phone)) {
                toast("请先输入手机号码")
                return@onClick
            }
            registerPresenter?.getVerifyCode(phone)
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

}