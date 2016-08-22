package masacre.galleryimage.utils;

import java.io.File;
import java.util.ArrayList;

import masacre.galleryimage.interfaces.OnGalleryItemAdded;
import masacre.galleryimage.model.GalleryAlbum;
import masacre.galleryimage.model.GalleryItem;
import masacre.galleryimage.model.GalleryPhoto;

public final class GalleryUtils {

    private GalleryUtils() {
        throw new IllegalAccessError("This is an utility class");
    }

    public static ArrayList<GalleryItem> retrieveFileFromDirectory(File dir) {
        return retrieveFileFromDirectory(dir, null);
    }

    public static ArrayList<GalleryItem> retrieveFileFromDirectory(File dir, OnGalleryItemAdded onGalleryItemAdded) {
        ArrayList<GalleryItem> galleryItems = new ArrayList<>();
        if (dir != null) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    File[] internalFiles = file.listFiles();
                    if (!file.isHidden() && internalFiles != null && internalFiles.length > 0) {
                        GalleryAlbum galleryAlbum = new GalleryAlbum(file);
                        if (onGalleryItemAdded != null) {
                            onGalleryItemAdded.onGalleryItemAdded(galleryAlbum);
                        }
                        galleryItems.add(galleryAlbum);
                    }
                } else {
                    GalleryPhoto galleryPhoto = new GalleryPhoto(file);
                    if (onGalleryItemAdded != null) {
                        onGalleryItemAdded.onGalleryItemAdded(galleryPhoto);
                    }
                    galleryItems.add(galleryPhoto);
                }
            }
        }
        return galleryItems;
    }
}
