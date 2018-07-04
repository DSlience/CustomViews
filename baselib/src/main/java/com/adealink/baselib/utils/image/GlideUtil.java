package com.adealink.baselib.utils.image;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.Target;

/**
 * 图片加载相关
 * Created by Xuefu_Du on 2017/6/13.
 */
public class GlideUtil {

    private static GlideUtil instance;

    public static GlideUtil getInstance() {
        if (instance == null) {
            synchronized (GlideUtil.class) {
                if (instance == null) {
                    instance = new GlideUtil();
                }
            }
        }
        return instance;
    }

    //加载图片类型列举
    public static final String LOAD_BITMAP = "GLIDEUTILS_GLIDE_LOAD_BITMAP";//加载bitmap，如果是GIF则显示第一帧
    public static final String LOAD_GIF = "GLIDEUTILS_GLIDE_LOAD_GIF";//加载gif动画

    /**
     * 以asBitmap形式加载图片;Glide请求将受activity生命周期控制
     *
     * @param activity  android.support.v4.app.FragmentActivity
     * @param path      图片路径
     * @param imageView 目标控件
     * @param placeId   占位
     * @param errorId   错误
     */
    public void loadAsBitmap(FragmentActivity activity, String path, ImageView imageView, int placeId, int errorId) {
        try {
            Glide.with(activity).load(path).asBitmap().placeholder(placeId).error(errorId).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 默认加载LOAD_BITMAP类型
     *
     * @param context
     * @param path      图片路径
     * @param imageView 目标控件
     */
    public void loadBitmapCenterCrop(Context context, String path, ImageView imageView) {
        loadPicCenterCrop(context, path, imageView, 0, 0, null);
    }

    /**
     * 默认加载LOAD_BITMAP类型
     *
     * @param context
     * @param path            图片路径
     * @param imageView       目标控件
     * @param placeAndErrorId 占位+错误资源-使用同一份
     */
    public void loadBitmapCenterCrop(Context context, String path, ImageView imageView, int placeAndErrorId) {
        loadPicCenterCrop(context, path, imageView, placeAndErrorId, placeAndErrorId, null);
    }

    /**
     * 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制
     *
     * @param context
     * @param path
     * @param imageView
     * @param placeId     占位
     * @param errorId     错误
     * @param bitmapOrGif 加载普通图片 或者GIF图片 ，GIF图片设置bitmap显示第一帧
     */
    public void loadPicCenterCrop(Context context, String path, ImageView imageView, int placeId, int errorId, String bitmapOrGif) {
        try {
            if (bitmapOrGif == null || bitmapOrGif.equals(LOAD_BITMAP)) {
                Glide.with(context).load(path).centerCrop().placeholder(placeId).error(errorId).crossFade().into(imageView);
            } else if (bitmapOrGif.equals(LOAD_GIF)) {
                Glide.with(context).load(path).asGif().crossFade().into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 默认加载LOAD_BITMAP类型；Glide请求将受activity生命周期控制
     *
     * @param activity  android.support.v4.app.FragmentActivity
     * @param path      图片路径
     * @param imageView 目标控件
     */
    public void loadBitmapCenterCrop(FragmentActivity activity, String path, ImageView imageView) {
        loadPicCenterCrop(activity, path, imageView, 0, 0, null);
    }

    /**
     * 默认加载LOAD_BITMAP类型；Glide请求将受activity生命周期控制
     *
     * @param activity        android.support.v4.app.FragmentActivity
     * @param path            图片路径
     * @param imageView       目标控件
     * @param placeAndErrorId 占位+错误资源-使用同一份
     */
    public void loadBitmapCenterCrop(FragmentActivity activity, String path, ImageView imageView, int placeAndErrorId) {
        loadPicCenterCrop(activity, path, imageView, placeAndErrorId, placeAndErrorId, null);
    }

    /**
     * Glide请求将受activity生命周期控制
     *
     * @param activity    android.support.v4.app.FragmentActivity
     * @param path
     * @param imageView
     * @param placeId     占位
     * @param errorId     错误
     * @param bitmapOrGif 加载普通图片 或者GIF图片 ，GIF图片设置bitmap显示第一帧
     */
    public void loadPicCenterCrop(FragmentActivity activity, String path, ImageView imageView, int placeId, int errorId, String bitmapOrGif) {
        try {
            if (bitmapOrGif == null || bitmapOrGif.equals(LOAD_BITMAP)) {
                Glide.with(activity).load(path).centerCrop().placeholder(placeId).error(errorId).crossFade().into(imageView);
            } else if (bitmapOrGif.equals(LOAD_GIF)) {
                Glide.with(activity).load(path).asGif().crossFade().into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 默认加载LOAD_BITMAP类型;
     * 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制
     *
     * @param context
     * @param path      图片路径
     * @param imageView 目标控件
     */
    public void loadBitmap(Context context, String path, ImageView imageView) {
        loadPic(context, path, imageView, 0, 0, null);
    }

    /**
     * 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制
     *
     * @param context
     * @param path
     * @param imageView
     * @param placeId     占位
     * @param errorId     错误
     * @param bitmapOrGif 加载普通图片 或者GIF图片 ，GIF图片设置bitmap显示第一帧
     */
    public void loadPic(Context context, String path, ImageView imageView, int placeId, int errorId, String bitmapOrGif) {
        try {
            if (bitmapOrGif == null || bitmapOrGif.equals(LOAD_BITMAP)) {
                Glide.with(context).load(path).placeholder(placeId).error(errorId).crossFade().into(imageView);
            } else if (bitmapOrGif.equals(LOAD_GIF)) {
                Glide.with(context).load(path).asGif().crossFade().into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 默认加载LOAD_BITMAP类型;Glide请求将受activity生命周期控制
     *
     * @param activity  Activity
     * @param path      图片路径
     * @param imageView 目标控件
     */
    public void loadBitmap(Activity activity, String path, ImageView imageView) {
        loadPic(activity, path, imageView, 0, 0, null);
    }

    /**
     * Glide请求将受activity生命周期控制
     *
     * @param activity    Activity
     * @param path
     * @param imageView
     * @param placeId     占位
     * @param errorId     错误
     * @param bitmapOrGif 加载普通图片 或者GIF图片 ，GIF图片设置bitmap显示第一帧
     */
    public void loadPic(Activity activity, String path, ImageView imageView, int placeId, int errorId, String bitmapOrGif) {
        try {
            if (bitmapOrGif == null || bitmapOrGif.equals(LOAD_BITMAP)) {
                Glide.with(activity).load(path).placeholder(placeId).error(errorId).crossFade().into(imageView);
            } else if (bitmapOrGif.equals(LOAD_GIF)) {
                Glide.with(activity).load(path).asGif().crossFade().into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 默认加载LOAD_BITMAP类型;Glide请求将受activity生命周期控制
     *
     * @param activity  android.support.v4.app.FragmentActivity
     * @param path      图片路径
     * @param imageView 目标控件
     */
    public void loadBitmap(FragmentActivity activity, String path, ImageView imageView) {
        loadPic(activity, path, imageView, 0, 0, null);
    }

    /**
     * Glide请求将受activity生命周期控制
     *
     * @param activity    android.support.v4.app.FragmentActivity
     * @param path
     * @param imageView
     * @param placeId     占位
     * @param errorId     错误
     * @param bitmapOrGif 加载普通图片 或者GIF图片 ，GIF图片设置bitmap显示第一帧
     */
    public void loadPic(FragmentActivity activity, String path, ImageView imageView, int placeId, int errorId, String bitmapOrGif) {
        try {
            if (bitmapOrGif == null || bitmapOrGif.equals(LOAD_BITMAP)) {
                Glide.with(activity).load(path).placeholder(placeId).error(errorId).crossFade().into(imageView);
            } else if (bitmapOrGif.equals(LOAD_GIF)) {
                Glide.with(activity).load(path).asGif().crossFade().into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 默认加载LOAD_BITMAP类型;Glide请求将受activity生命周期控制
     *
     * @param activity        android.support.v4.app.FragmentActivity
     * @param path            图片路径
     * @param target          目标控件
     * @param placeAndErrorId 占位+错误资源-使用同一份
     */
    public void loadBitmap(FragmentActivity activity, String path, int placeAndErrorId, Target target) {
        loadPic(activity, path, placeAndErrorId, placeAndErrorId, null, target);
    }

    /**
     * 默认加载LOAD_BITMAP类型;Glide请求将受activity生命周期控制
     *
     * @param activity android.support.v4.app.FragmentActivity
     * @param path     图片路径
     * @param target   目标控件
     * @param placeId  占位
     * @param errorId  错误资源
     */
    public void loadBitmap(FragmentActivity activity, String path, int placeId, int errorId, Target target) {
        loadPic(activity, path, placeId, errorId, null, target);
    }

    /**
     * Glide请求将不受activity生命周期控制
     *
     * @param activity
     * @param path
     * @param target
     * @param placeId     占位
     * @param errorId     错误
     * @param bitmapOrGif 加载普通图片 或者GIF图片 ，GIF图片设置bitmap显示第一帧
     */
    public void loadPic(FragmentActivity activity, String path, int placeId, int errorId, String bitmapOrGif, Target target) {
        try {
            if (bitmapOrGif == null || bitmapOrGif.equals(LOAD_BITMAP)) {
                Glide.with(activity).load(path).placeholder(placeId).error(errorId).crossFade().into(target);
            } else if (bitmapOrGif.equals(LOAD_GIF)) {
                Glide.with(activity).load(path).asGif().crossFade().into(target);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取某图的bitmap;Glide请求将受activity生命周期控制
     *
     * @param activity android.support.v4.app.FragmentActivity
     * @param path     图片路径
     * @param target   加载监听
     */
    public void genBitmap(FragmentActivity activity, String path, Target target) {
        try {
            Glide.with(activity).load(path).asBitmap().into(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取某图的bitmap;Glide请求将不受activity生命周期控制
     *
     * @param path   图片路径
     * @param target 加载监听
     */
    public void genBitmap(Context context, String path, Target target) {
        try {
            Glide.with(context).load(path).asBitmap().into(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-----圆形图片------------Start

    /**
     * 加载设置圆形; Glide请求将不受activity生命周期控制
     *
     * @param activity   android.support.v4.app.FragmentActivity
     * @param resourceId
     * @param imageView
     */
    @SuppressWarnings("unchecked")
    public void loadContextCircleBitmap(FragmentActivity activity, int resourceId, ImageView imageView) {
        try {
            Glide.with(activity).load(resourceId).bitmapTransform(new GlideCircleTransform(activity)).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载设置圆形图片
     * 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制
     *
     * @param context
     * @param path
     * @param imageView
     */
    @SuppressWarnings("unchecked")
    public void loadContextCircleBitmap(Context context, String path, ImageView imageView) {
        try {
            Glide.with(context).load(path).bitmapTransform(new GlideCircleTransform(context)).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载设置圆形图片; Glide请求将不受activity生命周期控制
     *
     * @param activity  android.support.v4.app.FragmentActivity
     * @param path
     * @param imageView
     */
    public void loadContextCircleBitmap(FragmentActivity activity, String path, ImageView imageView) {
        try {
            Glide.with(activity).load(path).bitmapTransform(new GlideCircleTransform(activity)).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载设置圆形图片
     * 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制
     * 使用activity 会受到Activity生命周期控制
     * 使用FragmentActivity 会受到FragmentActivity生命周期控制
     *
     * @param context
     * @param path
     * @param imageView
     * @param placeId
     * @param errorId
     */
    @SuppressWarnings("unchecked")
    public void loadContextCircleBitmap(Context context, String path, ImageView imageView, int placeId, int errorId) {
        try {
            Glide.with(context).load(path).placeholder(placeId).error(errorId).bitmapTransform(new GlideCircleTransform(context)).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载设置圆形图片; Glide请求将不受activity生命周期控制
     *
     * @param activity  android.support.v4.app.FragmentActivity
     * @param path
     * @param imageView
     * @param placeId
     * @param errorId
     */
    public void loadContextCircleBitmap(FragmentActivity activity, String path, ImageView imageView, int placeId, int errorId) {
        try {
            Glide.with(activity).load(path).placeholder(placeId).error(errorId).bitmapTransform(new GlideCircleTransform(activity)).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //-----圆形图片------------End


    //-----圆角图片-----------Start

    /**
     * 加载设置圆角图片
     * 使用Application上下文，Glide请求将不受Activity/Fragment生命周期控制
     * 使用activity 会受到Activity生命周期控制
     * 使用FragmentActivity 会受到FragmentActivity生命周期控制
     *
     * @param context
     * @param path
     * @param imageView
     * @param roundRadius 圆角大小（>0）
     */
    @SuppressWarnings("unchecked")
    public void loadContextRoundBitmap(Context context, String path, ImageView imageView, int roundRadius) {
        try {
            if (roundRadius < 0) {
                Glide.with(context).load(path).centerCrop().bitmapTransform(new GlideRoundTransform(context)).into(imageView);
            } else {
                Glide.with(context).load(path).bitmapTransform(new GlideRoundTransform(context, roundRadius)).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //非centerCrop类型
    public void loadContextRoundBitmap(Context context, String path, ImageView imageView, int roundRadius, int placeId, int errorId) {
        try {
            if (roundRadius < 0) {
                Glide.with(context).load(path).placeholder(placeId).error(errorId).centerCrop().bitmapTransform(new GlideRoundTransform(context)).into(imageView);
            } else {
                Glide.with(context).load(path).placeholder(placeId).error(errorId).bitmapTransform(new GlideRoundTransform(context, roundRadius)).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //centerCrop类型
    public void loadCenterCorpRoundBitmap(Context context, String path, ImageView imageView, int roundRadius, int placeId, int errorId) {
        try {
            if (roundRadius < 0) {
                Glide.with(context).load(path).placeholder(placeId).error(errorId).centerCrop().bitmapTransform(new GlideRoundTransform(context)).into(imageView);
            } else {
                Glide.with(context).load(path).placeholder(placeId).error(errorId).bitmapTransform(new CenterCrop(context), new GlideRoundTransform(context, roundRadius)).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //-----圆角图片-----------End

}