package com.tincher.tcraft.feature.main

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.tincher.tcraft.R
import com.tincher.tcraftlib.base.BaseHttpActivity
import com.tincher.tcraftlib.data.DataManager
import com.tincher.tcraftlib.network.download.DownloadListener
import com.tincher.tcraftlib.network.download.FileDownLoadObserver
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


/**
 *
 * Created by dks on 2018/9/12.
 */
class KMainActivity : BaseHttpActivity() {

    override fun addCheckPermissions(): Array<out String>? = arrayOf(Manifest.permission.CAMERA)

    override fun initLayout(): Int = R.layout.activity_main

    override fun initView() {
        tv_1.setOnClickListener {
            //            ImageTools.takePhotoForResult(this
//                , File(Environment.getExternalStorageDirectory().path, "66.jpg"), 2)

//            ImageTools.selectPicFromAlbumForResult(this, 1)
            download()
        }
    }

    override fun initData() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 2) {

            val d = data?.data
            if (d != null) {
                val d2 = data.extras?.get("data") as Bitmap
                iv_1.setImageBitmap(d2)

            } else {
                val bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().path + "/66.jpg")
                iv_1.setImageBitmap(bitmap)
            }
//            val d2 = data?.extras?.get("data") as Bitmap
//            iv_1.setImageBitmap(d2)


        }
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 1) {
            val d = data?.data
            iv_1.setImageURI(d)
        }

    }

    val downloadUrl = "http://ftp-new-apk.pconline.com.cn/417d498f47ad31e3f36e994c47ce20df/pub/download/201808/pconline1534815265261.apk"
    private fun download() {
        DataManager.download(downloadUrl, Environment.getExternalStorageDirectory().path, "66.apk"

            , object : DownloadListener {

            //            override fun onProgress(progress: Int, total: Long) {
//                LogUtils.d("onProgress: \ntotal: $total  \nprogress: $progress")
//
//            }
                //Todo int -->float #.00
            override fun onProgress(progress: Int) {
                LogUtils.d("onProgress:  \nprogress: $progress")

            }

        }

            , object : FileDownLoadObserver<File>() {
            override fun onDownLoadSuccess(t: File?) {
                LogUtils.e("onDownLoadSuccess ")

                AppUtils.installApp(t)

            }

            override fun onDownLoadFail(throwable: Throwable?) {
                throwable?.printStackTrace()
                LogUtils.e("onDownLoadFail ${throwable?.message}")
            }

            override fun onSaveProgress(progress: Int, total: Long?) {
//                LogUtils.d("onSaveProgress: \ntotal: $total  \nprogress: $progress")
            }

        }


        )
    }

}