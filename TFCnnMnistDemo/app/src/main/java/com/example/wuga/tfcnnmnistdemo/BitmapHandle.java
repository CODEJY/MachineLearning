package com.example.wuga.tfcnnmnistdemo;

import android.graphics.Bitmap;

/**
 * Created by wuga on 2017/10/9.
 */

public class BitmapHandle {
    public Bitmap bitmap;
    BitmapHandle(Bitmap bitmap_) {
        bitmap = bitmap_;
    }
    public Bitmap handle() {
        //先调用reshape，再调用灰度处理
        return bitmap;
    }
    private void reshape() {
        //将输入的bitmap转换成28*28
    }
    private void getGrayScaleImg() {
        //转换成灰度图
    }
}
