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
import android.widget.TextView
import cn.woyeshi.base.activities.BaseActivity
import cn.woyeshi.base.dialogs.BottomOptionDialog
import cn.woyeshi.entity.utils.UriToFile
import cn.woyeshi.manager.R
import cn.woyeshi.manager.utils.Navigation
import cn.woyeshi.presenterimpl.presenters.FileUploadPresenter
import cn.woyeshi.presenterimpl.presenters.IFileUploadView
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_register_next.*
import java.io.File
import java.net.URLDecoder


class RegisterNextActivity : BaseActivity(), IFileUploadView {

    private val REQUEST_PICK_UP_PHOTO = 1001
    private val REQUEST_TAKE_PHOTO = 1002

    private val REQUEST_PERMISSION_CAMERA = 1003

    private var tempFile: File? = null                    //拍照的uri

    private val fileUploadPresenter by lazy { FileUploadPresenter(this) }

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
        Navigation.toMainActivity(this)
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
                            val file = UriToFile.getFileByUri(this, resultUri)
                            if (file != null && file.exists()) {
                                fileUploadPresenter.uploadImage(file)
                            }
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
        toast(url)
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

}