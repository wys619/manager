package cn.woyeshi.manager.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.text.Editable
import android.text.TextUtils
import android.widget.TextView
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.base.dialogs.BottomOptionDialog
import cn.woyeshi.datePicker.DatePicker
import cn.woyeshi.entity.beans.manager.UserInfo
import cn.woyeshi.entity.utils.UriToFile
import cn.woyeshi.manager.R
import cn.woyeshi.manager.dialogs.GenderSelectDialog
import cn.woyeshi.manager.utils.MyTextWatcher
import cn.woyeshi.manager.utils.Navigation
import cn.woyeshi.presenterimpl.presenters.FileUploadPresenter
import cn.woyeshi.presenterimpl.presenters.IFileUploadView
import cn.woyeshi.presenterimpl.presenters.IUserView
import cn.woyeshi.presenterimpl.presenters.UserPresenter
import com.example.lib_map.GaoDeLocation
import com.example.lib_map.GaoDeLocationResult
import com.example.lib_map.ILocationCallback
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_register_next.*
import java.io.File
import java.net.URLDecoder


class RegisterNextActivity : BaseActivity(), IFileUploadView, IUserView {
    private val REQUEST_PICK_UP_PHOTO = 1001
    private val REQUEST_TAKE_PHOTO = 1002

    private val REQUEST_PERMISSION_CAMERA = 1003

    private var tempFile: File? = null                    //拍照的uri
    private var cropFile: File? = null                    //拍照的uri

    private val fileUploadPresenter by lazy { FileUploadPresenter(this) }
    private val userPresenter by lazy { UserPresenter(this) }
    private var location: GaoDeLocationResult? = null

    override fun getContentLayoutID(): Int {
        return R.layout.activity_register_next
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setEditBtnVisibility(true)
        setEditBtnText(getString(R.string.hint_skip))
        setBackBtnVisibility(false)
        title = getString(R.string.title_complete)
        initView()
        getLocation()
    }

    private fun getLocation() {
        GaoDeLocation.getInstance().startOneTimeLocation(this, object : ILocationCallback {
            override fun onLocationResult(location: GaoDeLocationResult?) {
                if (location != null) {
                    this@RegisterNextActivity.location = location
                }
            }
        })
    }

    override fun onBackBtnClick() {
        toast(getString(R.string.hint_take_1_minute_to_complete))
    }

    private fun initView() {
        sdvHeader.setOnClickListener {
            BottomOptionDialog(this, listOf(getString(R.string.string_album), getString(R.string.string_take_photo))) { result ->
                when (result) {
                    getString(R.string.string_album) -> {       //从相册选择
                        toAlbum()
                    }
                    getString(R.string.string_take_photo) -> {  //拍照
                        toCamera()
                    }
                }
            }.show()
        }
        llSelectBirthday.setOnClickListener {
            DatePicker.showPicker(this) { y, m, d ->
                tvBirthday.text = String.format("%d-%02d-%02d", y, m, d)
                btnLogin.isEnabled = isCommitBtnShouldEnabled()
            }
        }
        llSelectGender.setOnClickListener {
            GenderSelectDialog(this) { gender ->
                tvGender.text = gender
                btnLogin.isEnabled = isCommitBtnShouldEnabled()
            }.show()
        }
        inputLayout1.addTextWatcher(object : MyTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                btnLogin.isEnabled = isCommitBtnShouldEnabled()
            }
        })
        btnLogin.setOnClickListener {
            if (cropFile != null && cropFile!!.exists()) {
                fileUploadPresenter.uploadImage(cropFile!!)
            }
        }
    }

    private fun isCommitBtnShouldEnabled(): Boolean {
        if (cropFile == null) {
            return false
        }
        if (TextUtils.isEmpty(inputLayout1.getText())) {
            return false
        }
        if (tvGender.text == getString(R.string.string_to_choose)) {
            return false
        }
        if (tvBirthday.text == getText(R.string.string_to_choose)) {
            return false
        }
        return true
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
            val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            if (!picturesDir.exists()) {
                picturesDir.mkdirs()
            }
            tempFile = File(picturesDir, "photo_${System.currentTimeMillis()}.jpg")
            val tempUri = FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileProvider", tempFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
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
        toMainActivity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PICK_UP_PHOTO -> {
                    if (data != null) {
                        val uri = data.data
                        val file = UriToFile.getFileByUri(this, uri)
                        if (file != null && file.exists()) {
                            cropPhoto(Uri.fromFile(file))
                        } else {
                            val str = uri.toString()
                            val path = str.substring(str.lastIndexOf('/') + 1)
                            val f = File(URLDecoder.decode(path))
                            if (f.exists()) {
                                cropPhoto(Uri.fromFile(f))
                            }
                        }
                    }
                }
                REQUEST_TAKE_PHOTO -> {
                    if (tempFile != null && tempFile!!.exists()) {
                        cropPhoto(Uri.fromFile(tempFile))
                    }
                }
                UCrop.REQUEST_CROP -> {
                    if (data != null) {
                        val resultUri: Uri? = UCrop.getOutput(data)
                        if (resultUri != null) {
                            sdvHeader.setImageURI(resultUri, this)
                            cropFile = UriToFile.getFileByUri(this, resultUri)
                            btnLogin.isEnabled = isCommitBtnShouldEnabled()
                        } else {
                            onImageError()
                        }
                    } else {
                        onImageError()
                    }
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            onImageError()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onUploadSuccess(url: String) {
        val userInfo = getLoginUserInfo()
        if (userInfo != null) {
            userInfo.avartar = url
            val nickname = inputLayout1.getText()
            val gender = tvGender.text
            val birthday = "${tvBirthday.text} 00:00:00"
            userInfo.nickname = nickname
            userInfo.gender = if (gender == "男") {
                "1"
            } else {
                "2"
            }
            if (location != null) {
                userInfo.city = location!!.city
            }
            userInfo.birthday = birthday.toString()
            userPresenter.updateUser(userInfo)
        }
    }

    override fun onUploadFail() {

    }

    private fun onImageError() {
        toast(getString(R.string.string_get_image_error))
    }

    private fun cropPhoto(tempUri: Uri) {
        UCrop.of(tempUri, Uri.fromFile(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "photo_${System.currentTimeMillis()}.jpg")))
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(512, 512)
                .start(this)
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

    override fun onLoginRequestSuccess(loginInfo: UserInfo) {

    }

    override fun onRegisterSuccess(t: UserInfo) {

    }

    override fun onUpdateUserSuccess() {
        toast("用户信息提交成功！")
        toMainActivity()
    }

    fun toMainActivity() {
        Navigation.toMainActivity(this)
        finish()
    }
}