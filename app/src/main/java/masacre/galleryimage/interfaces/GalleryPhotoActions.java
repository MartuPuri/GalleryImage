package masacre.galleryimage.interfaces;

import masacre.galleryimage.model.GalleryPhoto;

public interface GalleryPhotoActions {
    void selectPhoto(final GalleryPhoto galleryPhoto);

    void unselectPhoto(final GalleryPhoto galleryPhoto);

    boolean isPhotoSelected(final GalleryPhoto galleryPhoto);
}
