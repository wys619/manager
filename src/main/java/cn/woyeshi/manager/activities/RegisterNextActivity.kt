package cn.woyeshi.manager.activities

import android.os.Bundle
import android.widget.TextView
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.manager.R
import cn.woyeshi.manager.utils.Navigation
import kotlinx.android.synthetic.main.activity_register_next.*

class RegisterNextActivity : BaseActivity() {

    override fun getContentLayoutID(): Int {
        return R.layout.activity_register_next
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setEditBtnVisibility(true)
        setEditBtnText("跳过")
        title = "完善资料"
        initView()
    }

    private fun initView() {
        sdvHeader.setOnClickListener {

        }
    }

    //点击跳过
    override fun onEditBtnClick(view: TextView) {
        Navigation.toMainActivity(this)
    }

}