package com.friend.swagger.common;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @Author ZhangWenXuan
 * @Date 2020-03-01 14:52
 **/
public class SystemUtil {
    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getIpAddressString() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }

    /**
     * 得到资源文件中图片的Uri
     *
     * @param context
     * @param drawableId
     * @return
     */
    public static Uri getUriToDrawable(Context context,
                                       int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId));
        return imageUri;
    }

    /**
     * drawable转为file
     *
     * @param mContext
     * @param drawableId
     * @param fileName
     * @return
     */
    public static File drawableToFile(Context mContext, int drawableId, String fileName) {
        Bitmap bitmap = small(BitmapFactory.decodeResource(mContext.getResources(), drawableId));
        String defaultPath = mContext.getFilesDir()
                .getAbsolutePath() + "/swaggerlogo";
        File file = new File(defaultPath);
        if (!file.exists()) {
            file.mkdirs();
        } else {
        }
        String defaultImgPath = defaultPath + "/" + fileName;
        file = new File(defaultImgPath);
        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private static Bitmap small(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(0.2f, 0.2f);//长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    /**
     * 获取字符串中的数字
     *
     * @param str
     * @return
     */
    public static int getNum(String str) {
        String dest = "";
        if (str != null) {
            dest = str.replaceAll("[^0-9]", "");
        }
        return Integer.parseInt(dest);
    }
}
