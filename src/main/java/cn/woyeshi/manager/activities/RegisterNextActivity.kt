package cn.woyeshi.manager.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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

    private val REQUEST_PERMISSION_CAMERA = 1003

    override fun getContentLayoutID(): Int {
        return R.layout.activity_register_next
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setEditBtnVisibility(true)
        setEditBtnText(getString(R.string.hint_skip))
        setBackBtnVisibility(false)
        title = getString(R.string.title_complete)
        initView()
    }

    override fun onBackBtnClick() {
        toast(getString(R.string.hint_take_1_minute_to_complete))
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
        if (!isMOrLater()) {
            openCamera()
        } else {
            if (checkPermission(Manifest.permission.CAMERA)) {
                openCamera()
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION_CAMERA)
            }
        }

    }

    private fun openCamera() {
        val state = Environment.getExternalStorageState()// 获取内存卡可用状态
        if (state == Environment.MEDIA_MOUNTED) {
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            startActivityForResult(intent, REQUEST_TAKE_PHOTO)
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
                REQUEST_TAKE_PHOTO -> {

                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    showAlert(getString(R.string.hint_camera_permission_first), getString(R.string.text_to_open)) {
                        Navigation.toAppDefaultSetting(this)
                    }
                }
            }
        }
    }

}