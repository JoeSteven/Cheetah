package com.joey.cheetah.core.media.unused;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import com.bumptech.glide.request.transition.ViewPropertyTransition;

import java.io.File;

import com.joey.cheetah.core.utils.CLog;
import com.joey.cheetah.core.utils.Global;
import com.joey.cheetah.core.utils.UIUtil;

/**
 * Description: unused
 * author:Joey
 * date:2018/7/30
 */
public class ImageConfig {
    private Context context;
    private boolean ignoreCertificateVerify; //https是否忽略校验
    private String url;

    private float thumbnail; //缩略图缩放倍数
    private String filePath; //文件路径

    private File file; //文件路径
    private int resId;  //资源id
    private String rawPath;  //raw路径
    private String assetsPath;  //asserts路径
    private String contentProvider; //内容提供者
    private boolean isGif; //是否是GIF图
    private View target;
    private int resizeWidth;
    private int resizeHeight;

    private int priority;

    private AnimationMode animationMode;
    private int animationId;
    private Animation animation;

    private ViewPropertyTransition.Animator animator;

    private int placeHolderResId;
    private int errorResId;

    private ShapeMode shapeMode;//默认矩形,可选直角矩形,圆形/椭圆
    private int rectRoundRadius;//圆角矩形时圆角的半径
    private ScaleMode scaleMode;//填充模式,默认centercrop,可选fitXY,centerInside...

    public ImageConfig(Builder builder) {
        this.url = builder.url;
        this.thumbnail = builder.thumbnail;
        this.filePath = builder.filePath;
        this.file = builder.file;
        this.resId = builder.resId;
        this.rawPath = builder.rawPath;
        this.assetsPath = builder.assetsPath;
        this.contentProvider = builder.contentProvider;

        this.ignoreCertificateVerify = builder.ignoreCertificateVerify;

        this.target = builder.target;

        this.resizeWidth = builder.resizeWidth;
        this.resizeHeight = builder.resizeHeight;

        this.shapeMode = builder.shapeMode;
        if (shapeMode == ShapeMode.RECT_ROUND) {
            this.rectRoundRadius = builder.rectRoundRadius;
        }
        this.scaleMode = builder.scaleMode;

        this.animationId = builder.animationId;
        this.animationMode = builder.animationMode;
        this.animator = builder.animator;
        this.animation = builder.animation;

        this.priority = builder.priority;
        this.placeHolderResId = builder.placeHolderResId;

        this.asBitmap = builder.asBitmap;
        this.isGif = builder.isGif;
        this.errorResId = builder.errorResId;
    }

    public boolean isAsBitmap() {
        return asBitmap;
    }

    private boolean asBitmap;//只获取bitmap

    public Context getContext() {
        if (context == null) {
            context = Global.context();
        }
        return context;
    }

    public int getErrorResId() {
        return errorResId;
    }

    public String getContentProvider() {
        return contentProvider;
    }

    public String getFilePath() {
        return filePath;
    }

    public File getFile() {
        return file;
    }

    public int getPlaceHolderResId() {
        return placeHolderResId;
    }

    public int getRectRoundRadius() {
        return rectRoundRadius;
    }

    public int getResId() {
        return resId;
    }

    public String getRawPath() {
        return rawPath;
    }

    public String getAssetsPath() {
        return assetsPath;
    }

    public ScaleMode getScaleMode() {
        return scaleMode;
    }

    public ShapeMode getShapeMode() {
        return shapeMode;
    }

    public View getTarget() {
        return target;
    }

    public String getUrl() {
        return url;
    }

    public int getResizeWidth() {
        return resizeWidth;
    }

    public int getResizeHeight() {
        return resizeHeight;
    }

    public AnimationMode getAnimationMode() {
        return animationMode;
    }

    public int getAnimationId() {
        return animationId;
    }

    public Animation getAnimation() {
        return animation;
    }

    public ViewPropertyTransition.Animator getAnimator() {
        return animator;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isIgnoreCertificateVerify() {
        return ignoreCertificateVerify;
    }

    public float getThumbnail() {
        return thumbnail;
    }

//    private void show() {
//        ImageHelper.getLoader().request(this);
//    }

    public boolean isGif() {
        return isGif;
    }


    public static class Builder {
        private Context context;

        private boolean ignoreCertificateVerify = false;

        /**
         * 图片源
         * 类型	SCHEME	示例
         * 远程图片	http://, https://	HttpURLConnection 或者参考 使用其他网络加载方案
         * 本地文件	file://	FileInputStream
         * Content provider	content://	ContentResolver
         * asset目录下的资源	asset://	AssetManager
         * res目录下的资源	  res://	Resources.openRawResource
         * Uri中指定图片数据	data:mime/type;base64,	数据类型必须符合 rfc2397规定 (仅支持 UTF-8)
         *
         * @param config
         * @return
         */
        private String url;
        private float thumbnail;
        private String filePath;
        private File file;
        private int resId;
        private String rawPath;
        private String assetsPath;
        private String contentProvider;
        private boolean isGif = false;

        private View target;
        private boolean asBitmap;//只获取bitmap

        private int resizeWidth; //选择加载分辨率的宽
        private int resizeHeight; //选择加载分辨率的高

        //UI:
        private int placeHolderResId;

        private int errorResId;

        private ShapeMode shapeMode;//默认矩形,可选直角矩形,圆形/椭圆
        private int rectRoundRadius;//圆角矩形时圆角的半径

        private ScaleMode scaleMode;//填充模式,默认centercrop,可选fitXY,centerInside...

        private int priority; //请求优先级


        public int animationId; //动画资源id
        public AnimationMode animationMode; //动画资源Type
        public Animation animation; //动画资源
        public ViewPropertyTransition.Animator animator; //动画资源id

        public Builder(Context context) {
            this.context = context;
        }

        public Builder ignoreCertificateVerify(boolean ignoreCertificateVerify) {
            this.ignoreCertificateVerify = ignoreCertificateVerify;
            return this;
        }

        /**
         * 缩略图
         *
         * @param thumbnail
         * @return
         */
        public Builder thumbnail(float thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        /**
         * error图
         *
         * @param errorResId
         * @return
         */
        public Builder error(int errorResId) {
            this.errorResId = errorResId;
            return this;
        }

        /**
         * 设置网络路径
         *
         * @param url
         * @return
         */
        public Builder url(String url) {
            this.url = url;
            if (url.contains("gif")) {
                isGif = true;
            }
            return this;
        }

        /**
         * 加载SD卡资源
         *
         * @param filePath
         * @return
         */
        public Builder file(String filePath) {
            if (filePath.startsWith("file:")) {
                this.filePath = filePath;
                return this;
            }

            if (!new File(filePath).exists()) {
                CLog.e("imageloader", "file not exist");
                return this;
            }

            this.filePath = filePath;
            if (filePath.contains("gif")) {
                isGif = true;
            }
            return this;
        }

        /**
         * 加载SD卡资源
         *
         * @param file
         * @return
         */
        public Builder file(File file) {
            this.file = file;

            return this;
        }

        /**
         * 加载drawable资源
         *
         * @param resId
         * @return
         */
        public Builder res(int resId) {
            this.resId = resId;
            return this;
        }

        /**
         * 加载ContentProvider资源
         *
         * @param contentProvider
         * @return
         */
        public Builder content(String contentProvider) {
            if (contentProvider.startsWith("content:")) {
                this.contentProvider = contentProvider;
                return this;
            }

            if (contentProvider.contains("gif")) {
                isGif = true;
            }

            return this;
        }

        /**
         * 加载raw资源
         *
         * @param rawPath
         * @return
         */
        public Builder raw(String rawPath) {

            this.rawPath = rawPath;

            if (rawPath.contains("gif")) {
                isGif = true;
            }

            return this;
        }

        /**
         * 加载asserts资源
         *
         * @param assertspath
         * @return
         */
        public Builder assets(String assertspath) {
            this.assetsPath = assertspath;

            if (assertspath.contains("gif")) {
                isGif = true;
            }

            return this;
        }

        public Builder asBitmap() {
            this.asBitmap = true;
            return this;
        }

        public Builder asGif() {
            this.isGif = true;
            return this;
        }

        public void into(View targetView) {
            this.target = targetView;
//            new ImageConfig(this).show();
        }


        /**
         * 加载图片的分辨率
         *
         * @param widthDp dp
         * @param heightDp dp
         * @return
         */
        public Builder resizeWithDp(int widthDp, int heightDp) {
            this.resizeWidth = UIUtil.dip2px(widthDp);
            this.resizeHeight = UIUtil.dip2px(heightDp);
            return this;
        }

        /**
         * @param widthPx px
         * @param heightPx px
         * @return
         */
        public Builder resizeWithPx(int widthPx, int heightPx) {
            this.resizeWidth = widthPx;
            this.resizeHeight = heightPx;
            return this;
        }

        /**
         * 占位图
         *
         * @param placeHolderResId
         * @return
         */
        public Builder placeHolder(int placeHolderResId) {
            this.placeHolderResId = placeHolderResId;
            return this;
        }



        /**
         * 圆角
         * @return
         */
        public Builder asCircle() {
            this.shapeMode = ShapeMode.OVAL;
            return this;
        }

        /**
         * 形状为圆角矩形时的圆角半径
         *
         * @param rectRoundRadius
         * @return
         */
        public Builder rectRoundCorner(int rectRoundRadius) {
            this.rectRoundRadius = UIUtil.dip2px(rectRoundRadius);
            this.shapeMode = ShapeMode.RECT_ROUND;
            return this;
        }


        /**
         * 正方形
         *
         * @return
         */
        public Builder asSquare() {
            this.shapeMode = ShapeMode.SQUARE;
            return this;
        }


        /**
         * 拉伸/裁剪模式
         *
         * @param scaleMode 取值ScaleMode
         * @return
         */
        public Builder scale(ScaleMode scaleMode) {
            this.scaleMode = scaleMode;
            return this;
        }


        public Builder animate(int animationId) {
            this.animationMode = AnimationMode.ANIMATION_ID;
            this.animationId = animationId;
            return this;
        }

        public Builder animate(ViewPropertyTransition.Animator animator) {
            this.animationMode = AnimationMode.ANIMATOR;

            this.animator = animator;
            return this;
        }

        public Builder animate(Animation animation) {
            this.animationMode = AnimationMode.ANIMATION;
            this.animation = animation;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

    }

    public enum  ShapeMode {
        RECT,
        RECT_ROUND,
        OVAL,
        SQUARE
    }

    public enum  ScaleMode {
        CENTER_CROP,
        FIT_CENTER,
        CENTER_INSIDE

    }

    private enum AnimationMode {
        ANIMATOR,
        ANIMATION_ID,
        ANIMATION
    }
}
