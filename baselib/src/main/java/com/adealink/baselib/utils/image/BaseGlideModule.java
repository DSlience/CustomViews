package com.adealink.baselib.utils.image;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

/**
 * 图片加载的Glide的配置
 * Created by Xuefu_Du on 2017/6/27.
 */
public class BaseGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置磁盘缓存目录
        File storageDirectory = Environment.getExternalStorageDirectory();
        String downloadDirectoryPath = storageDirectory + "/fragmentvisibledemo/image/cache/";
        //设置缓存的大小为100M
        int cacheSize = 100 * 1024 * 1024;//DiskLruCacheFactory.DEFAULT_DISK_CACHE_SIZE
        builder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath, cacheSize));
//        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
//        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
//        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
//        int customMemoryCacheSize = (int) (0.2 * defaultMemoryCacheSize);
//        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);
//        builder.setMemoryCache(new LruResourceCache(10240));
//        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
    }


    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
