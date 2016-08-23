package masacre.galleryimage.interfaces;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import masacre.galleryimage.model.GalleryAlbum;
import masacre.galleryimage.viewholder.GalleryPhotoViewHolder;

public interface OnGalleryItemClick {
    void onAlbumClick(final View view, final GalleryAlbum galleryAlbum);

    void onPhotoLongClickListener();

    void onPhotoClickListener(final GalleryPhotoViewHolder viewHolder, final ImageView photoImageView, final Bitmap bitmap);
}
