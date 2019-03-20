package com.dkaishu.tcraft.feature.sample;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dkaishu.tcraft.R;
import com.dkaishu.tcraftlib.base.BaseActivity;
import com.dkaishu.tcraftlib.utils.PictureManager;

import java.io.File;

/**
 * 调用相机拍照、从相册选取图片
 * Created by dks on 2018/11/5.
 */

public class TakePhotoActivity extends BaseActivity {
    private static final String TAG = "TakePhotoActivity";
    private ImageView iv_img;

    private final static int REQUEST_CODE_TAKE_PHOTO = 1;
    private final static int REQUEST_CODE_SELECT_IMG = 2;

    private static final String PATH_FILE_Temp     = Environment.getExternalStorageDirectory().getPath() + "/TCraft";
    private static final String IMG_Name_Temp      = "IMG_TCraft_temp.jpg";

    @Override
    protected String[] addCheckPermissions() {
        return new String[]{Manifest.permission.CAMERA};
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_take_photo;
    }

    @Override
    protected void initView() {
        iv_img = findViewById(R.id.iv_img);
        Button bt_take_photo = findViewById(R.id.bt_take_photo);
        Button bt_select_img = findViewById(R.id.bt_select_img);
        bt_take_photo.setText("拍照");
        bt_select_img.setText("相册选取");
        bt_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PictureManager.takePhotoForResult(TakePhotoActivity.this, null, REQUEST_CODE_TAKE_PHOTO);//方式1
                PictureManager.takePhotoForResult(TakePhotoActivity.this, new File(PATH_FILE_Temp, IMG_Name_Temp), REQUEST_CODE_TAKE_PHOTO);//方式2

            }
        });
        bt_select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureManager.selectPicFromAlbumForResult(TakePhotoActivity.this, REQUEST_CODE_SELECT_IMG);

            }
        });

    }

    @Override
    protected void initData() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_CODE_TAKE_PHOTO:
                    if (data != null) {//方式1
                        Bundle bundle = data.getExtras();
                        Bitmap bitmap = null;
                        if (bundle != null) {
                            bitmap = (Bitmap) bundle.get("data");
                        }
                        iv_img.setImageBitmap(bitmap);
                    } else {//方式2
                        String path = PATH_FILE_Temp + "/" + IMG_Name_Temp;

                        BitmapFactory.Options opts = new BitmapFactory.Options();//太大可能导致无法显示
                        opts.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(path, opts);
                        opts.inSampleSize = 4;
                        opts.inJustDecodeBounds = false;
                        Bitmap img = BitmapFactory.decodeFile(path, opts);
                        iv_img.setImageBitmap(img);

//                        Uri uri = Uri.fromFile(new File(PATH_FILE_Temp, IMG_Name_Temp));
//                        iv_img.setImageURI(uri);
                    }
                    break;

                case REQUEST_CODE_SELECT_IMG:
                    try {
                        Uri      selectedImage  = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int    columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                        iv_img.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
