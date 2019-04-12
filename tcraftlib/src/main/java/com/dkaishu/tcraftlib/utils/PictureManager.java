package com.dkaishu.tcraftlib.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.util.List;

/**
 * 拍照 、 相册选取照片 等
 * Created by dks on 2018/9/12.
 */

public class PictureManager {

    private PictureManager() {
    }

    public static void takePhotoForResult(@NonNull Activity activity, @Nullable File imageFile, int requestCode) {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (takePhotoIntent.resolveActivity(activity.getPackageManager()) != null) {
            if (imageFile != null) {
                // 默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // 照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
                // 可以通过data extra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
                // 如果没有指定uri，则data就返回有数据！
                if (!imageFile.getParentFile().exists()) {
                    imageFile.getParentFile().mkdir();
                }
                Uri uri;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                    uri = Uri.fromFile(imageFile);
                } else {
                    /**
                     * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                     * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                     */
                    String providerName = "";
                    try {
                        providerName = getFileProviderName(activity);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                        Log.e("PictureManager", e.getMessage());
                    }
                    uri = FileProvider.getUriForFile(activity, providerName, imageFile);
                    //加入uri权限 要不三星手机不能拍照
                    List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(takePhotoIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
        }
        activity.startActivityForResult(takePhotoIntent, requestCode);

    }

    /**
     * 与manifest文件中一致的唯一id
     * 用于解决provider冲突
     */
    public static String getFileProviderName(@NonNull Activity activity) throws PackageManager.NameNotFoundException {
        ApplicationInfo appInfo = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
        return appInfo.metaData.getString("provider_name_tcraft");
    }

    public static void selectPicFromAlbumForResult(@NonNull Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestCode);
    }

}
