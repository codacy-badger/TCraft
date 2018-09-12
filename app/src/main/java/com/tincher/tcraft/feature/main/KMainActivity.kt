package com.tincher.tcraft.feature.main

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import com.tincher.tcraft.R
import com.tincher.tcraftlib.base.BaseHttpActivity
import com.tincher.tcraftlib.utils.ImageTools
import kotlinx.android.synthetic.main.activity_main.*


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

            ImageTools.selectPicFromAlbumForResult(this, 1)
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

}