package com.xiaoluogo.goodtochat.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by xiaoluogo on 2017/8/17.
 * Email: angel-lwl@126.com
 * 压缩图片工具类
 */
public class ZipImageUtil {
    /**
     * 传入要压缩的图片路径
     *
     * @param origin 要压缩的图片路径
     * @return 压缩过后的图片路径
     */
    public static String zipImage(String origin) {
        if (TextUtils.isEmpty(origin)) {
            return null;
        }
        String[] uri = origin.split("\\.");
        String path = uri[0] + "y.jpeg";

        Bitmap bitmap = BitmapFactory.decodeFile(origin);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int quality = 100;
        while (true) {
            out.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
            int size = out.size();
            if (size > 100 * 1024) {
                quality -= 2;
            } else {
                break;
            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        return path;
    }
}
