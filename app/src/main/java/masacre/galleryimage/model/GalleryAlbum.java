package masacre.galleryimage.model;


import android.support.annotation.NonNull;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class GalleryAlbum extends GalleryItem {
    private final File directory;
    private final Set<GalleryPhoto> selectedPhotos;
    private final Set<GalleryItem> children;

    public GalleryAlbum(@NonNull final File directory) {
        this.directory = directory;
        this.selectedPhotos = new HashSet<>();
        this.children = new HashSet<>();
    }

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

    public int getAmountOfImages() {
        return children == null ? 0 : children.size();
    }

    public int getAmountOfSelectedPhotos() {
        return selectedPhotos == null ? 0 : selectedPhotos.size();
    }
}
