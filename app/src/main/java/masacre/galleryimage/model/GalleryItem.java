package masacre.galleryimage.model;


import android.os.Parcelable;

public abstract class GalleryItem implements Parcelable{
    private GalleryAlbum parent;

    public abstract boolean isAlbum();

    void setParent(GalleryAlbum parent) {
        this.parent = parent;
    }

    public GalleryAlbum getParent() {
        return this.parent;
    }
}
