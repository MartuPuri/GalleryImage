package masacre.galleryimage.model;


import java.io.Serializable;

public abstract class GalleryItem implements Serializable {
    private GalleryAlbum parent;

    public abstract boolean isAlbum();

    void setParent(GalleryAlbum parent) {
        this.parent = parent;
    }

    public GalleryAlbum getParent() {
        return this.parent;
    }
}
