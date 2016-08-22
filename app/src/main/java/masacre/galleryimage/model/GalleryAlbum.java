package masacre.galleryimage.model;


import android.os.Parcel;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class GalleryAlbum extends GalleryItem {
    private final File directory;
    private Set<GalleryPhoto> selectedPhotos = new HashSet<>();

    public GalleryAlbum(final File directory) {
        this.directory = directory;
    }

    protected GalleryAlbum(Parcel in) {
        directory = (File) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(directory);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GalleryAlbum> CREATOR = new Creator<GalleryAlbum>() {
        @Override
        public GalleryAlbum createFromParcel(Parcel in) {
            return new GalleryAlbum(in);
        }

        @Override
        public GalleryAlbum[] newArray(int size) {
            return new GalleryAlbum[size];
        }
    };

    @Override
    public boolean isAlbum() {
        return true;
    }

    public String getAlbumName() {
        return directory.getName();
    }

    public File getFile() {
        return directory;
    }

    public void addPhotoSelected(GalleryPhoto galleryPhoto) {
        selectedPhotos.add(galleryPhoto);
    }

    public boolean hasPhotoSelected() {
        return !selectedPhotos.isEmpty();
    }

    public void removePhotoSelected(GalleryPhoto galleryPhoto) {
        selectedPhotos.remove(galleryPhoto);
    }
}
