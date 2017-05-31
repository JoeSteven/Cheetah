package com.joey.cheetah.helper;

import android.text.TextUtils;
import android.widget.ImageView;

import com.joey.cheetah.uitls.CLog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * description - 图片加载帮助类
 * <p/>
 * author - Joe.
 * create on 16/8/2.
 * change
 * change on .
 */


public class PicassoHelper {
    public static RequestCreator basePicasso(String url, ImageView imageView){
        return Picasso.with(imageView.getContext())
                .load(url)
                .fit();
    }

    public static void load(String url, ImageView imageView){
        if(TextUtils.isEmpty(url)){
            CLog.w("PicassoHelper","url can't be empty!");
            return;
        }
        basePicasso(url,imageView)
                .into(imageView);
    }
    public static void load(String url, ImageView imageView, int width, int height){
        if(TextUtils.isEmpty(url)){
            CLog.w("PicassoHelper","url can't be empty!");
            return;
        }
        if(TextUtils.isEmpty(url)) return;
        basePicasso(url,imageView)
                .resize(width,height)
                .into(imageView);
    }

}
