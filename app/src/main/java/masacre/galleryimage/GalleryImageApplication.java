package masacre.galleryimage;

import android.app.Application;

import masacre.galleryimage.utils.ImageLoaderUtils;


public class GalleryImageApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderUtils.initilize(this);
    }
}
