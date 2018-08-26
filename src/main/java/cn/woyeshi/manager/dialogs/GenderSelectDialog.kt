package cn.woyeshi.manager.dialogs

import android.view.Gravity
import android.view.View
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.base.dialogs.BaseDialog
import cn.woyeshi.entity.utils.DensityUtil
import cn.woyeshi.manager.R

class GenderSelectDialog(activity: BaseActivity, val callback: ((String) -> Unit)) : BaseDialog(activity) {
    override fun isCancelable(): Boolean {
        return true
    }

    override fun getDialogGravity(): Int {
        return Gravity.BOTTOM
    }

    override fun getWidth(): Int {
        return DensityUtil.getScreenWidth(activity)
    }

    override fun getLayoutID(): Int {
        return R.layout.dialig_gender_select
    }

    override fun initViews(v: View?) {
        dialog.window.setWindowAnimations(R.style.dialogWindowAnim)
        findView<View>(R.id.ivMale).setOnClickListener {
            callback(activity.getString(R.string.string_male))
            cancel()
        }
        findView<View>(R.id.ivFemale).setOnClickListener {
            callback(activity.getString(R.string.string_female))
            cancel()
        }

    }

}