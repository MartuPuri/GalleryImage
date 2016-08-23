package masacre.galleryimage.model;


import android.os.Parcel;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.HashSet;

public class GalleryAlbum extends GalleryItem {
    private final File directory;
    private final HashSet<GalleryPhoto> selectedPhotos;
    private final HashSet<GalleryItem> children;

    public GalleryAlbum(@NonNull final File directory) {
        this.directory = directory;
        this.selectedPhotos = new HashSet<>();
        this.children = new HashSet<>();
    }

    protected GalleryAlbum(final Parcel in) {
        directory = (File) in.readSerializable();
        selectedPhotos = (HashSet<GalleryPhoto>) in.readSerializable();
        children = (HashSet<GalleryItem>) in.readSerializable();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeSerializable(directory);
        dest.writeSerializable(selectedPhotos);
        dest.writeSerializable(children);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GalleryAlbum> CREATOR = new Creator<GalleryAlbum>() {
        @Override
        public GalleryAlbum createFromParcel(final Parcel in) {
            return new GalleryAlbum(in);
        }

        @Override
        public GalleryAlbum[] newArray(final int size) {
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

    public void selectPhoto(GalleryPhoto galleryPhoto) {
        selectedPhotos.add(galleryPhoto);
    }

    public boolean hasPhotosSelected() {
        return !selectedPhotos.isEmpty();
    }

    public void unselectPhoto(GalleryPhoto galleryPhoto) {
        selectedPhotos.remove(galleryPhoto);
    }

    public void addChild(GalleryItem galleryItem) {
        this.children.add(galleryItem);
        galleryItem.setParent(this);
    }

    public boolean isPhotoSelected(GalleryPhoto galleryPhoto) {
        return selectedPhotos.contains(galleryPhoto);
    }
}
