package masacre.galleryimage.utils;


import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

import masacre.galleryimage.R;

public final class ImageLoaderUtils {
    private static final int MAX_IMAGE_WIDTH_FOR_DISK_CACHE = 1080;
    private static final int MAX_IMAGE_HEIGHT_FOR_DISK_CACHE = 1080;
    private static final int CACHE_MAX_SIZE = 10 * 1024 * 1024;
    private static final int MAX_BITMAP_SIZES_SUM_IN_MEMORY = 10 * 1024 * 1024;
    private static final int MAX_DISK_CACHE_SIZE = 50 * 1024 * 1024;
    private static final int MAX_FILE_COUNT = 200;
    private static final int THREAD_POOL_SIZE = 5;
    private static final int FADE_IN_DURATION_MILLIS = 800;
    private static ImageLoader imageLoaderInstance = ImageLoader.getInstance();

    private ImageLoaderUtils() {
        throw new IllegalAccessError("This is an utility class");
    }

    public static void initilize(Context context) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .diskCacheExtraOptions(MAX_IMAGE_WIDTH_FOR_DISK_CACHE, MAX_IMAGE_HEIGHT_FOR_DISK_CACHE, null)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(MAX_BITMAP_SIZES_SUM_IN_MEMORY))
                .memoryCacheSize(CACHE_MAX_SIZE)
                .diskCacheSize(MAX_DISK_CACHE_SIZE)
                .diskCacheFileCount(MAX_FILE_COUNT)
                .threadPoolSize(THREAD_POOL_SIZE)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .defaultDisplayImageOptions(displayImageOptions)
                .build();

        imageLoaderInstance.init(config);
    }

    public static void displayImage(String imageUrl, ImageView view) {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.image_placeholder)
                .showImageForEmptyUri(R.drawable.image_placeholder)
                .showImageOnFail(R.drawable.image_placeholder)
                .resetViewBeforeLoading(true)
                .build();

        imageLoaderInstance.displayImage(imageUrl, view, displayImageOptions);
    }
}
