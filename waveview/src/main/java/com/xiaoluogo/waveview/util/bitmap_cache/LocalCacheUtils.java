package com.xiaoluogo.waveview.util.bitmap_cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 本地缓存工具类
 * Created by Administrator on 2017/7/9.
 */

public class LocalCacheUtils {
    /**
     * 内存缓存工具类
     */
    private MemoryCacheUtils memoryCacheUtils;

    public LocalCacheUtils(MemoryCacheUtils memoryCacheUtils) {
        this.memoryCacheUtils = memoryCacheUtils;
    }

    /**
     * 根据Url获取bitmap
     *
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmapFromUrl(String imageUrl) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //判断一下sdcard是否为挂载状态
            //保存图片到sdcard目录下的//mnt/sdcard/beijingnews/
            //最终问文件是//mnt/sdcard/beijingnews/fkalfiowjfksad---->需要进行MD5加密
            try {
                String fileName = MD5Encoder.encode(imageUrl);
                //mnt/sdcard/beijingnews/fkalfiowjfksad
                File file = new File(Environment.getExternalStorageDirectory()+"/beijingnews",fileName);
                //mnt/sdcard/beijingnews/
                File parentFile = file.getParentFile();

                if(file.exists()){
                    FileInputStream is = new FileInputStream(file);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    if(bitmap != null){
                        //缓存到内存一份
                        memoryCacheUtils.putBitmap(imageUrl,bitmap);
                        is.close();
                        return bitmap;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public void putBitmap(String imageUrl, Bitmap bitmap) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //判断一下sdcard是否为挂载状态
            //保存图片到sdcard目录下的//mnt/sdcard/beijingnews/
            //最终问文件是//mnt/sdcard/beijingnews/fkalfiowjfksad---->需要进行MD5加密
            try {
                String fileName = MD5Encoder.encode(imageUrl);
                //mnt/sdcard/beijingnews/fkalfiowjfksad
                File file = new File(Environment.getExternalStorageDirectory()+"/beijingnews",fileName);
                //mnt/sdcard/beijingnews/
                File parentFile = file.getParentFile();

                if(!parentFile.exists()){
                    parentFile.mkdirs();//不存在就创建路径
                }
                if(!file.exists()){
                    file.createNewFile();
                }
                //保存图片
                bitmap.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(file));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
