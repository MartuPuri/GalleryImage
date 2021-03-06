package masacre.galleryimage.model;


import android.os.Parcel;

import java.io.File;

public class GalleryPhoto extends GalleryItem {
    private final File imageFile;

    public GalleryPhoto(final File imageFile) {
        this.imageFile = imageFile;
    }

    protected GalleryPhoto(Parcel in) {
        imageFile = (File) in.readSerializable();
    }

    @Override
    public boolean isAlbum() {
        return false;
    }

    public String getImagePath() {
        return imageFile.getAbsolutePath();
    }

    public File getFile() {
        return imageFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GalleryPhoto that = (GalleryPhoto) o;

        return imageFile != null ? imageFile.getAbsolutePath().equals(that.imageFile.getAbsolutePath()) : that.imageFile == null;

    }

    @Override
    public int hashCode() {
        return imageFile != null ? imageFile.hashCode() : 0;
    }
}
