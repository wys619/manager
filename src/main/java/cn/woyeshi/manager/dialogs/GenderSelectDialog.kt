package cn.woyeshi.manager.dialogs

import android.view.View
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.base.dialogs.BaseDialog
import cn.woyeshi.manager.R

class GenderSelectDialog(activity: BaseActivity) : BaseDialog(activity) {
    override fun isCancelable(): Boolean {
        return false
    }

    override fun getLayoutID(): Int {
        return R.layout.dialig_gender_select
    }

    override fun initViews(v: View?) {


    }

}