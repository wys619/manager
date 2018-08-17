package cn.woyeshi.manager.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.base.dialogs.BottomOptionDialog
import cn.woyeshi.manager.R
import cn.woyeshi.manager.utils.Navigation
import kotlinx.android.synthetic.main.activity_register_next.*


class RegisterNextActivity : BaseActivity() {

    private val REQUEST_PICK_UP_PHOTO = 1001
    private val REQUEST_TAKE_PHOTO = 1002

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
            BottomOptionDialog(this, listOf(getString(R.string.string_album), getString(R.string.string_take_photo))) {
                when (it) {
                    getString(R.string.string_album) -> {       //从相册选择
                        toAlbum()
                    }
                    getString(R.string.string_take_photo) -> {  //拍照
                        toCamera()
                    }
                }
            }.show()
        }
    }

    private fun toCamera() {
        val state = Environment.getExternalStorageState()// 获取内存卡可用状态
        if (state == Environment.MEDIA_MOUNTED) {
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            startActivityForResult(intent, 1)
        } else {
            toast(getString(R.string.string_sd_card_not_found))
        }

    }

    private fun toAlbum() {
        val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, REQUEST_PICK_UP_PHOTO)
    }

    //点击跳过
    override fun onEditBtnClick(view: TextView) {
        Navigation.toMainActivity(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PICK_UP_PHOTO -> {

                }
            }
        }
    }

}