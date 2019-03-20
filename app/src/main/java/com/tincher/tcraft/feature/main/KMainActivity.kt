package com.tincher.tcraft.feature.main

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.tincher.tcraft.R
import com.tincher.tcraftlib.base.BaseHttpActivity
import com.tincher.tcraftlib.network.download.FileDownLoadObserver
import com.tincher.tcraftlib.network.networkstatus.NetInfo
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


/**
 *
 * Created by dks on 2018/9/12.
 */
class KMainActivity : BaseHttpActivity() {


    override fun addCheckPermissions(): Array<out String>? = arrayOf(Manifest.permission.CAMERA)

    override fun setLayoutRes(): Int = R.layout.activity_main

    override fun initView() {
        tv_1.setOnClickListener {
            //            PictureManager.takePhotoForResult(this
//                , File(Environment.getExternalStorageDirectory().path, "66.jpg"), 2)

//            PictureManager.selectPicFromAlbumForResult(this, 1)
            download()

//            showLoadingFailed()
        }
        tv_2.setOnClickListener {
            //            observer.cancel()
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun initData() {

    }

    override fun onNetworkStateChanged(isNetworkAvailable: Boolean, netInfo: NetInfo?) {

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

    val observer = object : FileDownLoadObserver<File>() {

        override fun onDownLoadSuccess(t: File?) {
            LogUtils.e("onDownLoadSuccess ")
            Log.d("download", "onDownLoadSuccess: ${Looper.getMainLooper() == Looper.myLooper()}")
            dismissLoadingDialog()
            AppUtils.installApp(t)

        }

        override fun onDownLoadFail(throwable: Throwable?) {
            showLoadingFailed()
            throwable?.printStackTrace()
            LogUtils.e("onDownLoadFail ${throwable?.message}")
            Log.d("download", "onDownLoadFail: ${Looper.getMainLooper() == Looper.myLooper()}")

        }

        override fun onSaveProgress(progress: Int, total: Long?) {
            Log.d("download", "onSaveProgress: \ntotal: $total  \nprogress: $progress")
//            TextView(this@KMainActivity)
            runOnUiThread(Runnable {

            })
        }

    }


    private fun download() {
        showLoadingDialog()

//        NetworkUtil.download(downloadUrl, Environment.getExternalStorageDirectory().path, "66.apk"
//
//            , object : DownloadListener {
//
//                override fun onProgress(progress: Float, total: Long) {
//
//                    val df = DecimalFormat("0.00")
//                    setLoadingText(df.format(progress) + " %")
//                    runOnUiThread(Runnable {
//                        tv_1.text = df.format(progress) + " %"
//
//                    })
//
//                }
//            }
//
//            , observer
//
//
//        )
    }

    override fun onDialogDismissed() {
//        observer.cancel()
    }

}